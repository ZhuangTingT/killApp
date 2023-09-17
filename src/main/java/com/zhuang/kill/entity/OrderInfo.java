package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zhuang.kill.utils.ObjectMapperUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ztt
 * @since 2023-07-18
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_info")
@ApiModel(value = "OrderInfo对象", description = "订单表")
public class OrderInfo implements Serializable {
    static private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.ASSIGN_ID)
    private Long orderId;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("商品ID")
    @TableField("item_id")
    private Long itemId;

    @ApiModelProperty("冗余的商品名称，用于避免多表连接")
    @TableField("item_name")
    private String itemName;

    @ApiModelProperty("购买的商品数量")
    @TableField("order_num")
    private Integer orderNum;

    @ApiModelProperty("购买价格")
    @TableField("order_price")
    private BigDecimal orderPrice;

    @ApiModelProperty("渠道：1、PC, 2、Android, 3、iOS")
    @TableField("order_channel")
    private Integer orderChannel;

    @ApiModelProperty("订单状态,0新建未支付, 1已支付,2已发货, 3已收货, 4已退款,5已完成")
    @TableField("order_status")
    private Integer orderStatus;

    @ApiModelProperty("订单的创建时间")
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty("支付时间")
    @TableField("pay_date")
    private Date payDate;

    public String getCreateDateString() {
        return fmt.format(createDate);
    }

    public String getOrderStatusString() {
        if(orderStatus == 0)
            return "未支付";
        else if(orderStatus == 1)
            return "待发货";
        else if(orderStatus == 2)
            return "待收货";
        else if(orderStatus == 3)
            return "已收货";
        else if(orderStatus == 4)
            return "已退款";
        else
            return "已完成";
    }
}
