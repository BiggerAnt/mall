package com.ant.mall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 交换机、队列、绑定关系创建配置类
 * 服务启动的时候，就会创建这些交换机、队列、绑定关系
 */
@Configuration
public class MyMQConfig {

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","order-event-exchange");
        arguments.put("x-dead-letter-routing-key","order.release.order");
        arguments.put("x-message-ttl",60000);
        Queue queue = new Queue("order.delay.queue",true,false,false,arguments);
        return queue;
    }

    /**
     * 接收到达过期时间的队列
     * @return
     */
    @Bean
    public Queue orderReleaseOrderQueue(){
        Queue queue = new Queue("order.release.order.queue",true,false,false);
        return queue;
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public Exchange orderEventExchange(){
        TopicExchange topicExchange = new TopicExchange("order-event-exchange",true,false);
        return topicExchange;
    }

    /**
     * 死信队列和交换机的绑定
     * @return
     */
    @Bean
    public Binding orderCreateOrderBinding(){
        Binding binding = new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order-create-order",
                null);
        return binding;
    }

    /**
     * 接收到达过期时间的队列和交换机的绑定
     * @return
     */
    @Bean
    public Binding orderReleaseOrderBinding(){
        Binding binding = new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order-release-order",
                null);
        return binding;
    }
}
