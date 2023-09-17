package com.zhuang.kill.service;

import com.zhuang.kill.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
public interface OrderInfoService extends IService<OrderInfo> {
    boolean insert(OrderInfo orderInfo);
}
