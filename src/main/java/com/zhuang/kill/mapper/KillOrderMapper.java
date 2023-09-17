package com.zhuang.kill.mapper;

import com.zhuang.kill.entity.KillOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 秒杀订单表 Mapper 接口
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Mapper
public interface KillOrderMapper extends BaseMapper<KillOrder> {
    int insert(KillOrder killOrder);
}
