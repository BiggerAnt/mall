package com.ant.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)     //开启动态代理
@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit       //开启消息队列
@MapperScan("com.ant.mall.order.dao")
public class MallOrderApplication {

    public static void main(String[] args) {

        SpringApplication.run(MallOrderApplication.class, args);
    }

}
