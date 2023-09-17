package com.zhuang.kill.service;

import com.rabbitmq.client.Channel;
import com.zhuang.kill.entity.OrderInfo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

public interface RabbitmqConsumer {
    void handleMessage(Message message, Channel channel) throws IOException;
}
