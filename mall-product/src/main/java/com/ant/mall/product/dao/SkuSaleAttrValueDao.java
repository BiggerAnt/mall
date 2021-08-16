package com.ant.mall.product.dao;

import com.ant.mall.product.entity.SkuSaleAttrValueEntity;
import com.ant.mall.product.vo.ItemSaleAttrVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author lic
 * @email lic@gmail.com
 * @date 2021-06-10 16:51:01
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<ItemSaleAttrVo> getSaleAttrsBuSpuId(Long spuId);
}
