package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 秒杀订单表
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Getter
@Setter
@ToString
@TableName("kill_order")
@ApiModel(value = "KillOrder对象", description = "秒杀订单表")
@NoArgsConstructor
@AllArgsConstructor
public class KillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "kill_order_id", type = IdType.ASSIGN_ID)
    private Long killOrderId;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("商品ID")
    @TableField("item_id")
    private Long itemId;



}
