package com.ant.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ant.common.utils.PageUtils;
import com.ant.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author lic
 * @email 18340032515@163.com
 * @date 2021-06-12 14:14:13
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

