package com.zhuang.kill.mapper;

import com.zhuang.kill.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("select * from user_info where user_id=#{userId}")
    UserInfo getUserInfoById(Long userId);
}
