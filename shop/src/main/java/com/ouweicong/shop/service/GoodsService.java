package com.ouweicong.shop.service;

import com.ouweicong.common.controller.Result;
import com.ouweicong.common.pojo.Goods;
import com.ouweicong.common.pojo.Shop;
import com.ouweicong.shop.dao.GoodsDao;
import com.ouweicong.shop.dao.ShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ShopDao shopDao;

    //查询商品列表
    public String getGoodsList(String shopId) {
        Goods[] goodsList = goodsDao.getGoodsList(shopId);
        String res = Result.success(goodsList, "查询成功");
        return res;
    }

    //添加商品
    public String addGoods(Map<String, Object> goods) {
        goodsDao.addGoods(goods);
        return Result.success("添加成功");
    }

    //根据店铺id获取所属商品
    public String queryGoodsInfoByGoodsId(String goodsId) {
        String res;
        Goods goods = goodsDao.queryGoodsInfoByGoodsId(goodsId);
        if (goods == null) return res = Result.fail("商品id不存在");
        return res = Result.success(goods, "查询成功");
    }

    //删除商品
    public String delGoods(String goodsId) {
        goodsDao.delGoods(goodsId);
        return Result.success("删除成功");
    }

    //修改商品
    public String modifyGoods(Map<String, Object> goods) {
        goodsDao.modifyGoods(goods);
        return Result.success("修改成功");
    }

    //根据商店id获取所属商品
    public String queryGoodsInfoByShopId(String shopId) {
        Goods[] goodsList = goodsDao.queryGoodsInfoByShopId(shopId);
        String res = Result.success(goodsList, "查询成功");
        return res;
    }

//    //查询商品是否存在
//    public String goodsIsExists(String goodsId) {
//        if (Objects.equals(goodsDao.goodsIsExists(goodsId), goodsId)) return Result.fail("商品不存在");
//        else return Result.success("商品存在");
//    }
}

