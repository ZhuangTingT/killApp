package com.zhuang.kill.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    // 交换机和队列全部在消费者端定义
    public static final String CREATEORDER_QUEUE_NAME = "CREATEORDER_QUEUE";
    public static final String CREATEORDER_EXCHANGE_NAME = "CREATEORDER_EXCHANGE";
    public static final String CREATEORDER_ROUTINE_KEY = "CREATEORDER";

    //1.声明交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(CREATEORDER_EXCHANGE_NAME, true, false);
    }

    //2.声明队列 sms.fanout.queue/email.fanout.queue
    @Bean
    public Queue createOrderQueue() {
        return new Queue(CREATEORDER_QUEUE_NAME, true);
    }

    //3.绑定关系
    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(createOrderQueue()).to(fanoutExchange());
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        //SimpleRabbitListenerContainerFactory发现消息中有content_type有text就会默认将其转换成string类型的
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }


}
