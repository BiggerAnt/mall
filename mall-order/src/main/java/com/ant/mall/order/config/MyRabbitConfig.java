package com.ant.mall.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * RabbitMQ消息转换配置类
 * 将消息转为json收发
 * 默认时jdk的序列化机制
 */
@Configuration
public class MyRabbitConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter(){
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        return messageConverter;
    }

    /**
     * 生产者确认
     */
    @PostConstruct
    public void initRabbitTemplate(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *  服务器收到消息的回调，只要服务器收到消息，那么ack=true
             * @param correlationData   当前消息的唯一关联数据
             * @param ack   消息是否成功收到
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            }
        });

        //设置消息抵达队列的回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            //当消息投递队列失败才触发这个回调
            @Override
            public void returnedMessage(ReturnedMessage returned) {

            }
        });
    }

}
