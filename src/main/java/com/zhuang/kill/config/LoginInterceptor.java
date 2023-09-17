package com.zhuang.kill.config;

import com.zhuang.kill.entity.UserInfo;
import com.zhuang.kill.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 先检查session
        HttpSession session = request.getSession();
        if(session.getAttribute("userId") != null)
            return true;

        // 再检查cookie
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies){
                //遍历cookie，如果找到登录状态则返回true 继续执行原来的请求
                if("userId".equals(cookie.getName())){
                    String userId = cookie.getValue();
                    //是否存在对应的userInfo对象
                    UserInfo userInfo = userInfoService.getUserInfoById(Long.parseLong(userId));

                    // 补充缺失的session
                    if(userInfo != null) {
                        session.setAttribute("userId", userId);
                        session.setAttribute("userInfo", userInfo);
                    }
                    return true;
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/user/login");
        return false;
    }
}
