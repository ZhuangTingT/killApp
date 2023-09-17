package com.zhuang.kill.service.impl;

import com.zhuang.kill.entity.ItemInfo;
import com.zhuang.kill.mapper.ItemInfoMapper;
import com.zhuang.kill.mapper.UserInfoMapper;
import com.zhuang.kill.service.ItemInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Service
public class ItemInfoServiceImpl extends ServiceImpl<ItemInfoMapper, ItemInfo> implements ItemInfoService {
    @Autowired
    private ItemInfoMapper itemInfoMapper;

    @Override
    public List<ItemInfo> listItem() {
        List<ItemInfo> list = itemInfoMapper.listItem();
        return list;
    }

    @Override
    public ItemInfo getItemInfoById(Long itemId) {
        return itemInfoMapper.getItemInfoById(itemId);
    }
}
