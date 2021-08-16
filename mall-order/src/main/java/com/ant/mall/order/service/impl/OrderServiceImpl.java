package com.ant.mall.order.service.impl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ant.common.utils.PageUtils;
import com.ant.common.utils.Query;

import com.ant.mall.order.dao.OrderDao;
import com.ant.mall.order.entity.OrderEntity;
import com.ant.mall.order.service.OrderService;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
@RabbitListener(queues = {"hello-java-queue"})
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @RabbitHandler
    public void getMessage(Message message, String s,Integer i, Channel channel) {
        message.getBody();
        System.out.println("接收到了消息: "+s+i);
        //channel内按顺序自增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //回复MQ服务器，消息已经成功接收
        //multiple：是否批量确认
        try {
            channel.basicAck(deliveryTag,false);
            //拒绝接收消息，requeue是否重新进入队列，如果时false则丢弃
            channel.basicNack(deliveryTag,false,true);
            channel.basicReject(deliveryTag,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}