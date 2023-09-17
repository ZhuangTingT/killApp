package com.zhuang.kill.controller;

import com.zhuang.kill.config.RedisBloomFilter;
import com.zhuang.kill.entity.KillItemDetailVO;
import com.zhuang.kill.entity.KillItemVO;
import com.zhuang.kill.service.KillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 秒杀商品表 前端控制器
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */

@Controller
@RequestMapping("/killItem")
public class KillItemController {
    @Autowired
    private KillItemService killItemService;

    @GetMapping("/main.html")
    public String getKillItemVOList(Model model) {
        model.addAttribute("killItemList", killItemService.listKillItemVO());
        return "killItemPage";
    }

    @GetMapping("/killItemDetails.html")
    public String getKillItemDetailVO(@RequestParam("id") Long killId,
                                      Model model) {
        KillItemDetailVO killItemDetailVO = killItemService.getKillItemDetailVOByKillId(killId);
        if(killItemDetailVO == null) {
            return "forward:/killItem/main.html";
        }
        else {
            model.addAttribute("killItem", killItemDetailVO);
            return "killItemDetails";
        }
    }
}
