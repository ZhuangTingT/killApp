package com.zhuang.kill.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KillIdOrderInfo {
    Long killId;
    OrderInfo orderInfo;
}
