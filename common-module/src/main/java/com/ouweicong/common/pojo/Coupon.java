package com.ouweicong.common.pojo;

import lombok.Data;

@Data
public class Coupon extends System{
    private String couponId;

    private String couponNumber;

    private String couponDescribe;

    private String userId;

    private String couponEnd;

    private int status;
}
