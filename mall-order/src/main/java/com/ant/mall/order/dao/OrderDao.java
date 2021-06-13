package com.ant.mall.order.dao;

import com.ant.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lic
 * @email 18340032515@163.com
 * @date 2021-06-12 14:14:13
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
