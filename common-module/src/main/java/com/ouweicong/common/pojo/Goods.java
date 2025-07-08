package com.ouweicong.common.pojo;

import lombok.Data;

@Data
public class Goods extends System {

    private String goodsId;

    private String goodsName;

    private Float goodsPrice;

    private String goodsImage;

    private String goodsDescribe;

    private String shopId;

    private int status;


}
