package com.ouweicong.shop.dao;

import com.ouweicong.common.pojo.Goods;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsDao {
    //查询商品列表
    Goods[] getGoodsList(@Param("shopId") String shopId);
    //添加商品
    public void addGoods(@Param("goods") Map<String , Object> goods);

    //根据商品id获取所属商品详细
    Goods queryGoodsInfoByGoodsId(@Param("goodsId") String goodsId);

    //删除商品
    public void delGoods(@Param("goodsId") String goodsId);

    //修改商品
    public void  modifyGoods(@Param("goods") Map<String , Object> goods);

    //根据商店id获取所属商品
    Goods[] queryGoodsInfoByShopId(@Param("shopId") String shopId);

//    //查询商品是否存在
//    public String goodsIsExists(@Param("goodsId") String goodsId);
}
