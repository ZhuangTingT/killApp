package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 秒杀商品表
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("kill_item")
@ApiModel(value = "KillItem对象", description = "秒杀商品表")
public class KillItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀的商品表")
    @TableId(value = "kill_id", type = IdType.AUTO)
    private Long killId;

    @ApiModelProperty("商品ID")
    @TableField("item_id")
    private Long itemId;

    @ApiModelProperty("秒杀价")
    @TableField("kill_price")
    private BigDecimal killPrice;

    @ApiModelProperty("库存数量")
    @TableField("stock_count")
    private Integer stockCount;

    @ApiModelProperty("秒杀开始时间")
    @TableField("start_date")
    private Date startDate;

    @ApiModelProperty("秒杀结束时间")
    @TableField("end_date")
    private Date endDate;
}
