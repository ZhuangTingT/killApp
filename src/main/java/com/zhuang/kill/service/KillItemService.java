package com.zhuang.kill.service;

import com.zhuang.kill.entity.KillItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuang.kill.entity.KillItemVO;
import com.zhuang.kill.entity.KillItemDetailVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 秒杀商品表 服务类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
public interface KillItemService extends IService<KillItem> {
    List<KillItemVO> listKillItemVO();
    List<KillItemDetailVO> listKillItemDetailVO();
    KillItemDetailVO getKillItemDetailVOByKillId(Long killId);
    KillItem getKillItemByKillId(Long killId);
    boolean decreaseStock(Long killId, Integer amount);
    int insert(KillItem killItem);
}
