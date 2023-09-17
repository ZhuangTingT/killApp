package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class KillOrderVO {
    private Long killOrderId;
    static private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Long orderId;
    private Long userId;
    private Long itemId;
    private String itemName;
    private Integer orderNum;
    private BigDecimal orderPrice;
    private Integer orderChannel;
    private Integer orderStatus;
    private Date createDate;
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
