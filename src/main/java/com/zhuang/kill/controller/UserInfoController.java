package com.zhuang.kill.controller;

import com.zhuang.kill.entity.UserInfo;
import com.zhuang.kill.service.UserInfoService;
import com.zhuang.kill.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
// @RestController // @RestController = @ResponseBody + @Controller
@Controller
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/login")
    public String index(){
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/doLogin")
    public String doLogin(
            @RequestParam("phoneNumber") String userId,
            @RequestParam("password") String password,
            HttpSession session,
            HttpServletResponse response,
            Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 空入参检查
//        System.out.println("userId = "+userId);
//        System.out.println("password = "+ password);
        if("".equals(userId) || "".equals(password)) {
            model.addAttribute("msg","用户名或密码不可为空");
            return "index";
        }

        //是否存在对应的userInfo对象
//        UserInfo userInfo = userInfoService.loginCheck(userId, MD5Util.md5(password));
        UserInfo userInfo = userInfoService.loginCheck(userId, password);

        if(userInfo != null){
            session.setAttribute("userId", userId);
            session.setAttribute("userInfo", userInfo);
            // 设置session 10秒后失效，模拟session失效，使用cookie通过拦截器验证
            // session.setMaxInactiveInterval(10);
            Cookie cookie = new Cookie("userId", userId);
            response.addCookie(cookie);
            return "redirect:/killItem/main.html";
        }
        else{
            model.addAttribute("msg","用户名或密码错误");
            return "index";
        }
    }

    /**
     * 用户注册控制器 /user/doRegister
     * @param userId
     * @param nickName
     * @param password
     * @param response
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/doRegister")
    public String doRegister(
            @RequestParam("phoneNumber") String userId,
            @RequestParam("nickName") String nickName,
            @RequestParam("password") String password,
            HttpServletResponse response,
            HttpSession session,
            Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 空入参检查
        if("".equals(userId) || "".equals(password)){
            model.addAttribute("msg","用户名或密码不可为空"); // todo: 配置报错
            return "index";
        }

        Date curDate = new Date();
        UserInfo userInfo = new UserInfo(Long.parseLong(userId), nickName, MD5Util.md5(password), "lkjhvsa", null, curDate, curDate, 1);
        int res = userInfoService.register(userInfo);

        if(res > 0){
            session.setAttribute("userId", userId);
            session.setAttribute("userInfo", userInfo);
            Cookie cookie = new Cookie("userId", userId);
            response.addCookie(cookie);
            return "redirect:/killItem/main.html";
        }
        else{
            model.addAttribute("msg","该用户已注册"); // todo: 配置报错
            return "index";
        }
    }
}
