package com.zhuang.kill.service.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.zhuang.kill.config.RabbitmqConfig;
import com.zhuang.kill.service.RabbitmqProducer;
import com.zhuang.kill.utils.RabbitmqUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式：
 * 1、单个确认模式
 * 2、批量确认模式
 * 3、异步批量确认模式
 */
@Service("RabbitmqOrderProducer")
public class RabbitmqOrderProducer implements RabbitmqProducer {

    //异步发布确认
    @Override
    public void publishMessagesAsync(String messages[]) {
        Channel channel = null;
        try{
            channel = RabbitmqUtil.getChannel();
            /**
             * 创建队列
             * 第一个参数：队列名称
             * 第二个参数：服务器重启后队列是否还存在，即队列是否持久化, true为是，false为否，默认false，即消息存储在内存中而不是硬盘中
             * 第三个参数：该队列是否只供一个消费者进行消费，是否进行消息共享，true为只允许一个消费者进行消费，false为允许多个消费者对队列进行消费，默认false
             * 第四个参数：是否自动删除，最后一个消费者断开连接后该队列是否自动删除，true自动删除，false不自动删除
             * 第五个参数：其他参数
             */
            channel.queueDeclare(RabbitmqConfig.CREATEORDER_QUEUE_NAME,true,false,false,null);

            //开启发布确认
            channel.confirmSelect();

            //开始时间
            //long begin = System.currentTimeMillis();

            /**
             * 消息确认成功回调函数
             * 第一个参数：消息的标记
             * 第二个参数：是否确立确认
             */
            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
                //System.out.println("确认的消息：" + deliveryTag);
            };

            /**
             * 消息确认失败回调函数
             * 第一个参数：消息的标记
             * 第二个参数：是否确立确认
             */
            ConfirmCallback nackCallback = (deliveryTag,  multiple) -> {
                //System.out.println("未确认的消息：" + deliveryTag);
            };

            /**
             * 消息监听器，用于监听消息发送是否成功
             * 第一个参数：消息确认成功回调函数
             * 第二个参数：消息确认失败回调函数
             */
            channel.addConfirmListener(ackCallback, nackCallback);

            //批量发消息
            //int MESSAGE_COUNT = messages.length;
            for (String msg : messages) {
                channel.basicPublish("", RabbitmqConfig.CREATEORDER_QUEUE_NAME,null, msg.getBytes());
            }

            //结束时间
            //long end = System.currentTimeMillis();
            //System.out.println("发布" + MESSAGE_COUNT + "个异步发布确认消息，耗时" + (end - begin) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishMessageAsync(String message) throws IOException, TimeoutException {
        publishMessagesAsync(new String[]{message});
    }
}