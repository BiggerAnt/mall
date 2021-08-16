package com.ant.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.StringUtils;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.Query;
import com.ant.mall.product.dao.CategoryDao;
import com.ant.mall.product.entity.CategoryEntity;
import com.ant.mall.product.service.CategoryBrandRelationService;
import com.ant.mall.product.service.CategoryService;
import com.ant.mall.product.vo.Catalog3Vo;
import com.ant.mall.product.vo.Catelog2Vo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> getCategoriesWithTree() {
        //查出所有分类
        List<CategoryEntity> list = baseMapper.selectList(null);
        //一级分类
        List<CategoryEntity> level1Menus = list.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
                    menu.setChildren(getChildrenMenu(menu, list));
                    return menu;
                })
                .sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());
        return level1Menus;
    }

    @Override
    public void removeMenusByIds(List<Long> catIds) {
        //TODO 检查当前删除的菜单，是否又被其它地方引用
        baseMapper.deleteBatchIds(catIds);
    }

    /**
     * 获取子级菜单的全路径
     *
     * @param catelogId
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        findParent(catelogId, paths);
        Collections.reverse(paths);
        return paths.toArray(new Long[0]);
    }

    /**
     * 更新所有关联的数据
     *
     * @param category
     */
    @Transactional()
    @Override
    @CacheEvict(value = {"category"}, key = "'getLevel1Categorys'")

/*    @Caching(evict = {
            @CacheEvict(value = {"category"}, key = "'getLevel1Categorys'"),
            @CacheEvict(value = {"category"}, key = "'getLevel1Categorys'")
    },cacheable = {

    })*/
    public void updateAllPlace(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    //指定将缓存放入哪个分区,key是给数据设置对应的key，这里key是一个spel表达式，所以字符串要加单引号
    @Cacheable(value = {"category"}, key = "#root.method.name")      //代表当前方法的结果需要缓存，如果缓存中有，方法不用调用。缓存中没有，调用方法再将结果缓存。
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        //先查缓存
        String data = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(data)) {
            //查不到
            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDbWithRedissonLock();
            //TODO 缓存没命中时，需要查询数据库，然后再往缓存中存，存入缓存的步骤应该锁住，而不是在释放锁之后才存入缓存
            //redisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(catelogJsonFromDb));
            return catelogJsonFromDb;
        }
        //查到
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(data, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    /**
     * Redis分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock() {

        RLock lock = redisson.getLock("CatalogJsonLock");
        //加锁成功才执行
        lock.lock();
        Map<String, List<Catelog2Vo>> stringListMap;
        try {
            stringListMap = getStringListMap();
        } finally {
            lock.unlock();
        }
        return stringListMap;
    }

    /**
     * Redisson分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedissonLock() {
        //占用分布式锁，并设置过期时间
        String uuid = UUID.randomUUID().toString();
        //Boolean absent = redisTemplate.opsForValue().setIfAbsent("lock", "hahaha");
        Boolean absent = redisTemplate.opsForValue().setIfAbsent("lock", "hahaha",
                30, TimeUnit.SECONDS);
        //不存在，再使用分布式锁
        if (absent) {
            //加锁成功才执行
            //给锁设置过期时间,在这里不是原子操作
            //redisTemplate.expire("lock",30, TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> stringListMap;
            try {
                stringListMap = getStringListMap();
            } finally {
                //执行完业务后，需要删除锁
                //删除锁也需要原子操作，需要用redis官方推荐的lua脚本
                //redisTemplate.delete("lock");
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                //删除锁的原子操作
                redisTemplate.execute(new DefaultRedisScript<Integer>(script, Integer.class),
                        Arrays.asList("lock"), uuid);
            }
            return stringListMap;
        } else {
            //加锁失败......重试
            //休眠500ms再重试
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.getMessage();
            }
            return getCatelogJsonFromDbWithRedisLock();
        }
    }

    private Map<String, List<Catelog2Vo>> getStringListMap() {
        //双重校验，判断数据是否存在
        String data = redisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(data)) {
            //存在，直接返回
            return JSON.parseObject(data, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        // 查询所有一级分类
        List<CategoryEntity> level1 = getCategoryEntities(entityList, 0L);
        Map<String, List<Catelog2Vo>> parent_cid = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 拿到每一个一级分类 然后查询他们的二级分类
            List<CategoryEntity> entities = getCategoryEntities(entityList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (entities != null) {
                catelog2Vos = entities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                    // 找当前二级分类的三级分类
                    List<CategoryEntity> level3 = getCategoryEntities(entityList, l2.getCatId());
                    // 三级分类有数据的情况下
                    if (level3 != null) {
                        List<Catalog3Vo> catalog3Vos = level3.stream().map(l3 -> new Catalog3Vo(l3.getCatId().toString(), l3.getName(), l2.getCatId().toString())).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        //往缓存中放值
        redisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(parent_cid));
        return parent_cid;
    }

    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDb() {

        List<CategoryEntity> entityList = baseMapper.selectList(null);
        // 查询所有一级分类
        List<CategoryEntity> level1 = getCategoryEntities(entityList, 0L);
        Map<String, List<Catelog2Vo>> parent_cid = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 拿到每一个一级分类 然后查询他们的二级分类
            List<CategoryEntity> entities = getCategoryEntities(entityList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (entities != null) {
                catelog2Vos = entities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                    // 找当前二级分类的三级分类
                    List<CategoryEntity> level3 = getCategoryEntities(entityList, l2.getCatId());
                    // 三级分类有数据的情况下
                    if (level3 != null) {
                        List<Catalog3Vo> catalog3Vos = level3.stream().map(l3 -> new Catalog3Vo(l3.getCatId().toString(), l3.getName(), l2.getCatId().toString())).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }

    /**
     * 第一次查询的所有 CategoryEntity 然后根据 parent_cid去这里找
     */
    private List<CategoryEntity> getCategoryEntities(List<CategoryEntity> entityList, Long parent_cid) {

        return entityList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
    }

    private void findParent(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParent(byId.getParentCid(), paths);
        }
    }

    /**
     * 获取所有的子分类
     *
     * @param root 根菜单
     * @param all  所有的菜单
     * @return
     */
    private List<CategoryEntity> getChildrenMenu(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> {
                    //当前菜单的parentId是否等于传入菜单的catId
                    return categoryEntity.getParentCid() == root.getCatId();
                }).map(categoryEntity -> {
                    //递归查询所有菜单以及其子菜单
                    categoryEntity.setChildren(getChildrenMenu(categoryEntity, all));
                    return categoryEntity;
                }).sorted((menu1, menu2) -> {
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
        return children;
    }
}