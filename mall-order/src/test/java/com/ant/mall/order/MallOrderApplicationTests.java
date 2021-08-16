package com.ant.mall.order;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallOrderApplicationTests {

    @Autowired
    //创建交换机
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 交换机
     */
    @Test
    void createExchange() {
        //直接交换机 durable:是否持久化   autoDelete:是否自动删除
        DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        System.out.println("Done!");
    }

    /**
     * 队列
     */
    @Test
    void createQueue(){
        //exclusive:是否排它(独占)
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        System.out.println("Done!");
    }

    /**
     * 交换机和队列的绑定关系
     */
    @Test
    void createBinding(){
        /**
         * String destination, 目的地
         * DestinationType destinationType,目的地类型
         * String exchange,交换机
         * String routingKey,路由键
         * @Nullable Map<String, Object> arguments参数
         *
         * 将exchange与destination绑定
         */
        Binding binding = new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,"hello-java-exchange",
                "hello.java",null);
        amqpAdmin.declareBinding(binding);
        System.out.println("Done!");
    }

    /**
     * 发送消息
     */
    @Test
    void sendMessage(){
        User user = new User();
        user.setName("张三");
        user.setAge(24);
        rabbitTemplate.convertAndSend("hello-java-exchange","hello.java","Hello World");
        System.out.println("Done!");
    }

    @Data
    class User{
        private String name;
        private Integer age;
    }
}
