package com.zhuang.kill.mapper;

import com.zhuang.kill.entity.KillItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuang.kill.entity.KillItemDetailVO;
import com.zhuang.kill.entity.KillItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀商品表 Mapper 接口
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Mapper
public interface KillItemMapper extends BaseMapper<KillItem> {
    List<KillItemVO> listKillItemVO();
    List<KillItemDetailVO> listKillItemDetailVO();
    KillItem getKillItemByKillId(Long killId);
    KillItemDetailVO getKillItemDetailVOByKillId(Long killId);
    int decreaseStock(Map<String, Object> map);
}
