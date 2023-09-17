package com.zhuang.kill.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.util.concurrent.RateLimiter;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.zhuang.kill.CommonResponse;
import com.zhuang.kill.config.BloomFilterHelper;
import com.zhuang.kill.config.RedisBloomFilter;
import com.zhuang.kill.entity.*;
import com.zhuang.kill.service.KillItemService;
import com.zhuang.kill.service.KillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 秒杀订单表 前端控制器
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
// @RestController
@Controller
@RequestMapping("/killOrder")
@SessionAttributes("orderInfo")
public class KillOrderController {
    @Autowired
    private KillOrderService killOrderService;
    @Autowired
    private KillItemService killItemService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 令牌桶限流器
    private final RateLimiter smoothBurstyPath = RateLimiter.create(10);
    private final RateLimiter smoothBurstyOrder = RateLimiter.create(10);
    // RateLimiter warmUpRateLimiter = RateLimiter.create(20, 10, TimeUnit.SECONDS);

    @PostMapping(value = "/getKillPath")
    @ResponseBody
    public ResponseEntity<CommonResponse> getPath(@RequestParam("id2") String killId,
                                                  HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 非阻塞限流
        boolean permitCheck = smoothBurstyPath.tryAcquire(1);
        // boolean permitCheck = smoothBursty.tryAcquire(1, 1, TimeUnit.SECONDS); // 1秒阻塞
        // 阻塞限流
        // boolean permitCheck = smoothBursty.acquire(1);
        if(permitCheck == false) {
            return ResponseEntity.ok(CommonResponse.create(0, "业务繁忙，请重试"));
        }

        Long userId = Long.parseLong((String) request.getSession().getAttribute("userId"));
        if(userId == null) {
            return ResponseEntity.ok(CommonResponse.create(0, "请登录后下单"));
        }
        else {
            // 核查活动时间
            KillItemDetailVO killItem = killItemService.getKillItemDetailVOByKillId(Long.parseLong(killId));
            Date now = new Date();
            if(now.before(killItem.getStartDate()))
                return ResponseEntity.ok(CommonResponse.create(0, "活动未开始"));
            else if (now.after(killItem.getEndDate())) {
                return ResponseEntity.ok(CommonResponse.create(0, "活动已结束"));
            }

            // 创建秒杀令牌
            String path = killOrderService.createPath(userId, Long.parseLong(killId));
            if(path == null)
                return ResponseEntity.ok(CommonResponse.create(0, "商品已售罄"));
            else
                return ResponseEntity.ok(CommonResponse.create(1, path));
        }
    }

    // 隐藏接口的秒杀
    @PostMapping("/createOrder")
    public ResponseEntity<CommonResponse> createOrder(@RequestParam("amount") String amount,
                              @RequestParam("id1") String itemId,
                              @RequestParam("id2") String killId,
                              @RequestParam("path") String path,
                              HttpServletRequest request,
                              Model model) throws IOException, TimeoutException {
        // 非阻塞限流
        boolean permitCheck = smoothBurstyOrder.tryAcquire(1);
        // boolean permitCheck = smoothBursty.tryAcquire(1, 1, TimeUnit.SECONDS); // 1秒阻塞
        // 阻塞限流
        // boolean permitCheck = smoothBursty.acquire(1);
        if(permitCheck == false) {
            return ResponseEntity.ok(CommonResponse.create(0, "业务繁忙，请稍后再试"));
        }

        Long userId = Long.parseLong((String) request.getSession().getAttribute("userId"));
        boolean check = killOrderService.checkPath(userId, Long.parseLong(killId), path);

        if(check == true) {
            OrderInfo orderInfo = killOrderService.createKillOrder(userId, Long.parseLong(itemId), Long.parseLong(killId), Integer.parseInt(amount));
            if(orderInfo == null){
                return ResponseEntity.ok(CommonResponse.create(0, "下单失败"));
            }
            else {
                // 丢弃秒杀令牌
                redisTemplate.delete("seckillPath:" + userId + ":" + killId);
                model.addAttribute("orderInfo", orderInfo);
                return ResponseEntity.ok(CommonResponse.create(1, "/killOrder/details"));
            }
        }
        else {
            return ResponseEntity.ok(CommonResponse.create(0, "超过下单时限，请重新下单"));
        }
    }

    @GetMapping("/details")
    public String showDetails() {
        return "killOrderDetails";
    }

}
