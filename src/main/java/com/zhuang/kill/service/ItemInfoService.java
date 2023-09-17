package com.zhuang.kill.service;

import com.zhuang.kill.entity.ItemInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuang.kill.mapper.ItemInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
public interface ItemInfoService extends IService<ItemInfo> {
    List<ItemInfo> listItem();
    ItemInfo getItemInfoById(Long itemId);
}
