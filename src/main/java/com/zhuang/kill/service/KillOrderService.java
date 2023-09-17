package com.zhuang.kill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhuang.kill.entity.KillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuang.kill.entity.OrderInfo;
import com.zhuang.kill.entity.UserInfo;
import com.zhuang.kill.service.impl.KillOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
public interface KillOrderService extends IService<KillOrder> {
    String createPath(Long userId, Long killId) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    boolean checkPath(Long userId, Long killId, String path);
    OrderInfo createKillOrder(Long userId, Long itemId, Long killId, Integer amount) throws IOException, TimeoutException;
    boolean insert(KillOrder killOrder);
}
