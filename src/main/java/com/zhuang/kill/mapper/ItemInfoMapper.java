package com.zhuang.kill.mapper;

import com.zhuang.kill.entity.ItemInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Mapper
public interface ItemInfoMapper extends BaseMapper<ItemInfo> {
    @Select("select * from item_info")
    List<ItemInfo> listItem();

    @Select("select * from item_info where item_id=#{itemId}")
    ItemInfo getItemInfoById(Long itemId);
}
