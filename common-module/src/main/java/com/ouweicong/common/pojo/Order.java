package com.ouweicong.common.pojo;

import lombok.Data;

@Data
public class Order extends System{
    String orderId;
    String QRcode;
    Float payMoney;
    String couponId;
    String goodsId;
    Integer goods_count;
    Float total_money;
    String userId;
    Integer status;
    String pay_time;
    String use_time;
}
