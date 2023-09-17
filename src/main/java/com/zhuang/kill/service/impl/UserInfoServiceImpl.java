package com.zhuang.kill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhuang.kill.entity.UserInfo;
import com.zhuang.kill.mapper.UserInfoMapper;
import com.zhuang.kill.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 * @author ztt
 * @since 2023-07-18
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    @Transactional
    public UserInfo loginCheck(String userId, String password) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(UserInfo::getUserId, userId)
                .eq(UserInfo::getPassword, password);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional
    public int register(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo getUserInfoById(Long userId) {
        return userInfoMapper.getUserInfoById(userId);
    }
}
