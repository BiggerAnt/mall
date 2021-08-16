package com.ant.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ant.mall.product.entity.CategoryEntity;
import com.ant.mall.product.service.CategoryService;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.R;



/**
 * 商品三级分类
 *
 * @author lic
 * @email lic@gmail.com
 * @date 2021-06-10 17:45:37
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类和子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R list(){
        List<CategoryEntity> categories = categoryService.getCategoriesWithTree();

        return R.ok().put("data", categories);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("mall_product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mall_product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mall_product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateAllPlace(category);

        return R.ok();
    }

    /**
     * 批量修改
     */
    @RequestMapping("/update/sort")
    //@RequiresPermissions("mall_product:category:update")
    public R updateSort(@RequestBody List<CategoryEntity> category){
        //categoryService.updateById(category);
        //category.stream().forEach(x -> System.out.println(x));
        categoryService.updateBatchById(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mall_product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		//categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenusByIds(Arrays.asList(catIds));
        return R.ok();
    }
}
