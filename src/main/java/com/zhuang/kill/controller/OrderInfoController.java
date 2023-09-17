package com.zhuang.kill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Controller
@RequestMapping("/order")
public class OrderInfoController {
    @GetMapping("/details")
    public String showDetails() {
        return "killOrderDetails";
    }
}
