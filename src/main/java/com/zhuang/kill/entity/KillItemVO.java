package com.zhuang.kill.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import org.springframework.data.annotation.TypeAlias;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class KillItemVO {

    private Long itemId;
    private Long killId;
    private String itemName;
    private BigDecimal itemPrice;
    private BigDecimal killPrice;
    private Integer stockCount;

}
