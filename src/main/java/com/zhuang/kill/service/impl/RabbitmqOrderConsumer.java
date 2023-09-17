package com.zhuang.kill.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zhuang.kill.config.RabbitmqConfig;
import com.zhuang.kill.config.RedisExecutor;
import com.zhuang.kill.entity.KillIdOrderInfo;
import com.zhuang.kill.entity.KillOrder;
import com.zhuang.kill.entity.OrderInfo;
import com.zhuang.kill.mapper.KillOrderMapper;
import com.zhuang.kill.service.KillItemService;
import com.zhuang.kill.service.KillOrderService;
import com.zhuang.kill.service.OrderInfoService;
import com.zhuang.kill.service.RabbitmqConsumer;
import com.zhuang.kill.utils.ObjectMapperUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service("RabbitmqOrderConsumer")
public class RabbitmqOrderConsumer implements RabbitmqConsumer {
    @Autowired
    private KillOrderService killOrderService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private KillItemService killItemService;

    @Override
    @RabbitListener(queues = {RabbitmqConfig.CREATEORDER_QUEUE_NAME},
                    containerFactory = "rabbitListenerContainerFactory",
                    ackMode = "MANUAL")
    // 具一致性的消费者：监听消息队列
    // 1. 更新数据库的库存信息
    // 2. 去除下单token
    // 3. 生成订单信息
    public void handleMessage(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        KillIdOrderInfo killIdOrderInfo = JSON.parseObject(msg, KillIdOrderInfo.class);

        try{
            if( !insertOrder(killIdOrderInfo) ) // 插入失败
                throw new Exception();
            //手动确认
            System.out.println("已消费");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        catch (Exception e) {
            // 已重发过则丢弃该消息
            if(message.getMessageProperties().isRedelivered()) {
                System.out.println("重消费失败");
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            }
            else {
                System.out.println("重消费");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }

    @Transactional()
    public boolean insertOrder(KillIdOrderInfo killIdOrderInfo) {
        try {
            // 普通订单插入数据库
            OrderInfo orderInfo = killIdOrderInfo.getOrderInfo();
            boolean flag = orderInfoService.insert(orderInfo);
            if(flag == false)
                throw new Exception();

            KillOrder killOrder = new KillOrder();
            killOrder.setOrderId(orderInfo.getOrderId());
            killOrder.setUserId(orderInfo.getUserId());
            killOrder.setItemId(orderInfo.getItemId());
            flag = killOrderService.insert(killOrder);
            if(flag == false)
                throw new Exception();

            flag = killItemService.decreaseStock(killIdOrderInfo.getKillId(), orderInfo.getOrderNum());
            if(flag == false)
                throw new Exception();

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
