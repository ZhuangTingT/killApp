package com.zhuang.kill.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Component
public class RabbitmqUtil {
    @Autowired
    private static ConnectionFactory connectionFactory;

    static private String host = "139.159.185.90";
    static private int port = 5672;
    static private String username = "ztt";
    static private String password = "82387999aA@";
    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        System.out.println("链接成功");
    }

    public static Connection getConnection() throws IOException, TimeoutException {
        // 创建连接
        return connectionFactory.newConnection();
    }

    public static Channel getChannel() throws IOException, TimeoutException {
        Connection connection = getConnection();
        return connection.createChannel();
    }
}