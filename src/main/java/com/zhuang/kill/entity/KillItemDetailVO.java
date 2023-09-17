package com.zhuang.kill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
public class KillItemDetailVO {

    static private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private String itemId;
    private String killId;
    private String itemName;
    private String itemDetail;
    private BigDecimal itemPrice;
    private BigDecimal killPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public void setItemId(Long itemId) {
        this.itemId = Long.toString(itemId);
    }
    public void setKillId(Long killId) {
        this.killId = Long.toString(killId);
    }

    private void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail.replace("<br>", "\r\n");
    }

//    public void setStartDate(Date startDate) {
//        this.startDate = fmt.format(startDate);
//    }
//    public void setEndDate(Date endDate) {
//        this.endDate = fmt.format(endDate);
//    }

    // 重载setter，否则RedisTemplate反序列化会出错
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
