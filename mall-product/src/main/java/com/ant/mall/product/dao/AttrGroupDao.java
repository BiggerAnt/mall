package com.ant.mall.product.dao;

import com.ant.mall.product.entity.AttrGroupEntity;
import com.ant.mall.product.vo.SpuItemAttrGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 属性分组
 * 
 * @author lic
 * @email lic@gmail.com
 * @date 2021-06-10 16:51:02
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    List<SpuItemAttrGroup> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}
