package com.ant.mall.product.service.impl;

import com.ant.mall.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.Query;

import com.ant.mall.product.dao.BrandDao;
import com.ant.mall.product.entity.BrandEntity;
import com.ant.mall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = String.valueOf(params.get("key"));
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if(!ObjectUtils.isEmpty(key)){
            wrapper.eq("brand_id",key).or().like("name",key);
            new LambdaQueryWrapper<BrandEntity>().eq(BrandEntity::getBrandId,key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateAllPlace(BrandEntity brand) {
        //保证数据一致性
        this.updateById(brand);
        if(!ObjectUtils.isEmpty(brand.getName())){
            //同步更新其它关联表中的数据
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
        }
    }

}