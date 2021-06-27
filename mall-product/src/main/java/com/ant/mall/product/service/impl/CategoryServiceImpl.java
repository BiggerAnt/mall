package com.ant.mall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.Query;

import com.ant.mall.product.dao.CategoryDao;
import com.ant.mall.product.entity.CategoryEntity;
import com.ant.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


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
                .map( menu -> {
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
     * 获取所有的子分类
     * @param root  根菜单
     * @param all   所有的菜单
     * @return
     */
    private List<CategoryEntity> getChildrenMenu(CategoryEntity root, List<CategoryEntity> all){
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