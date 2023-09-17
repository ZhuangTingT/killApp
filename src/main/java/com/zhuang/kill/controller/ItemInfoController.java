package com.zhuang.kill.controller;

import com.zhuang.kill.entity.ItemInfo;
import com.zhuang.kill.service.ItemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Controller
@RequestMapping("/item")
public class ItemInfoController {
    @Autowired
    private ItemInfoService itemInfoService;

    /**
     * 获取商品列表
     * @return
     */
    @GetMapping("/main")
    public List<ItemInfo> getItemInfoList() {
        return itemInfoService.listItem();
    }

    @GetMapping("/showItem")
    public ItemInfo getItemInfo(Long itemId) {
        return itemInfoService.getItemInfoById(itemId);
    }


}
