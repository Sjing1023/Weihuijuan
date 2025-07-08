package com.ouweicong.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class Shop extends System {
    @TableId
    private String shopId;

    private String shopName;

    private String shopImage;

    private String address;

    private String telephone;
    //营业时间
    private String businessHours;

    private String userId;

    private int status;
}
