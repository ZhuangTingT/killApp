package com.zhuang.kill.service.impl;

import com.zhuang.kill.entity.OrderInfo;
import com.zhuang.kill.mapper.OrderInfoMapper;
import com.zhuang.kill.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public boolean insert(OrderInfo orderInfo){
        int res = orderInfoMapper.insert(orderInfo);
        System.out.println(res);
        if(res > 0)
            return true;
        else
            return false;
    }
}
