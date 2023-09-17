package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Getter
@Setter
@TableName("item_info")
@ApiModel(value = "ItemInfo对象", description = "商品表")
public class ItemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    @ApiModelProperty("商品名称")
    @TableField("item_name")
    private String itemName;

    @ApiModelProperty("商品标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("商品的图片")
    @TableField("item_img")
    private String itemImg;

    @ApiModelProperty("商品的详情介绍")
    @TableField("item_detail")
    private String itemDetail;

    @ApiModelProperty("商品单价")
    @TableField("item_price")
    private BigDecimal itemPrice;

    @ApiModelProperty("商品库存,-1表示没有限制")
    @TableField("stock_num")
    private Integer stockNum;
}
