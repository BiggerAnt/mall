package com.ant.mall.product.service;

import com.ant.mall.product.vo.Catelog2Vo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ant.common.utils.PageUtils;
import com.ant.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author lic
 * @email lic@gmail.com
 * @date 2021-06-10 16:51:02
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> getCategoriesWithTree();

    void removeMenusByIds(List<Long> catIds);

    /**
     * 找到catelogId的完整路径
     * [爷/父/子]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    void updateAllPlace(CategoryEntity category);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatelogJson();
}

