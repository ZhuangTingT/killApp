package com.zhuang.kill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisExecutor redisExecutor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        /**
         * 根据实际情况设置QPS
         */
        String url = request.getRequestURI();
        String ip = getIpAdd(request);
        //QPS设置为5，手动刷新接口可以测试出来
        if (!redisExecutor.tryAccess(ip+url, 5, 1000)) {
            throw new RuntimeException("调用频繁");
        }
        else {
            return true;
        }
    }

    public static final String getIpAdd(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    return null;
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 如果通过代理访问,可能获取2个IP,这时候去第二个(代理服务端IP)
        if (ipAddress.split(",").length > 1) {
            ipAddress = ipAddress.split(",")[1].trim();
        }
        return ipAddress;
    }
}