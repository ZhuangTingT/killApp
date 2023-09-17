package com.zhuang.kill.service;

import com.zhuang.kill.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo loginCheck(String userId, String password);
    int register(UserInfo userInfo);
    UserInfo getUserInfoById(Long userId);
}
