package com.zhuang.kill.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zhuang.kill.config.RedisConfig;
import com.zhuang.kill.entity.*;
import com.zhuang.kill.mapper.KillOrderMapper;
import com.zhuang.kill.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuang.kill.utils.JSONUtil;
import com.zhuang.kill.utils.MD5Util;
import com.zhuang.kill.config.RedisExecutor;
import com.zhuang.kill.utils.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * 秒杀订单表 服务实现类
 * @author ztt
 * @since 2023-07-18
 */
@Service
public class KillOrderServiceImpl extends ServiceImpl<KillOrderMapper, KillOrder> implements KillOrderService {
    @Autowired
    private KillOrderMapper killOrderMapper;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private KillItemService killItemService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource(name="RabbitmqOrderProducer")
    private RabbitmqProducer rabbitmqProducer;
    @Autowired
    private RedisExecutor redisExecutor;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String createPath(Long userId, Long killId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 核查是否有库存
        if("0".equals(stringRedisTemplate.opsForValue().get(RedisConfig.KILLITEM_STOCK_PREFIX + killId))) {
            return null;
        }
        //随机生成一串UUID并加密,根据用户和商品生成路径信息存入redis，设置1分钟失效
        String str = MD5Util.md5(UUID.randomUUID().toString() + "1a3c6f");
        redisTemplate.opsForValue().set("seckillPath:" + userId + ":" + killId, str, 60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(Long userId, Long killId, String path) {
        if (userId == null || killId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + userId + ":" + killId);
        return path.equals(redisPath);
    }

    @Override
    public OrderInfo createKillOrder(Long userId, Long itemId, Long killId, Integer amount) throws IOException, TimeoutException {
         // 缓存中商品是否存在
        KillItemDetailVO killItemDetailVO = killItemService.getKillItemDetailVOByKillId(killId);

        if(killItemDetailVO == null)
            return null;
        ItemInfo itemInfo = itemInfoService.getItemInfoById(itemId);

        // 缓存中用户是否存在
        UserInfo userInfo = userInfoService.getUserInfoById(userId);
        if(userInfo == null)
            return null;

        // 数量是否正确
        if(amount <=0 || amount > 5)
            return null;

        // 校验活动时间
        Date curDate = new Date();
        if(killItemDetailVO.getStartDate().compareTo(curDate) == 1 || killItemDetailVO.getEndDate().compareTo(curDate) == -1)
            return null;

        // redis预减库存，防止同一件商品多次售卖
        boolean flagRedis = redisExecutor.decreaseStock(killId, amount);
        if(!flagRedis)
            return null;

        // 生成订单对象
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setItemId(itemId);
        orderInfo.setItemName(itemInfo.getItemName());
        orderInfo.setOrderNum(amount);
        orderInfo.setOrderPrice(killItemDetailVO.getKillPrice().multiply(new BigDecimal(amount.intValue())));
        orderInfo.setOrderChannel(1);
        orderInfo.setOrderStatus(0);
        orderInfo.setCreateDate(curDate);
        orderInfo.setPayDate(null);

        String message = JSON.toJSONString(new KillIdOrderInfo(killId, orderInfo));
        rabbitmqProducer.publishMessageAsync(message);

        return orderInfo;
    }

    @Override
    public boolean insert(KillOrder killOrder) {
        int res = killOrderMapper.insert(killOrder);
        if(res > 0)
            return true;
        else
            return false;
    }
}
