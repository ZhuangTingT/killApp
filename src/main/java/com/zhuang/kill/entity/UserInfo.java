package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Getter
@Setter
@AllArgsConstructor
@TableName("user_info")
@ApiModel(value = "UserInfo对象", description = "用户表")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号码作为用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("保存加盐加密后的密码：MD5(密码, salt)")
    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @ApiModelProperty("头像地址")
    @TableField("head")
    private String head;

    @ApiModelProperty("注册时间")
    @TableField("register_date")
    private Date registerDate;

    @ApiModelProperty("上次登录时间")
    @TableField("last_login_date")
    private Date lastLoginDate;

    @ApiModelProperty("登录次数")
    @TableField("login_count")
    private Integer loginCount;
}
