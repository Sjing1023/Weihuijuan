package com.ouweicong.shop.Controller;

import com.alibaba.fastjson.JSON;
import com.ouweicong.common.controller.Result;
import com.ouweicong.common.pojo.Goods;
import com.ouweicong.common.pojo.Shop;
import com.ouweicong.common.pojo.User;
import com.ouweicong.common.utils.JwtUtil;
import com.ouweicong.common.utils.ObjectToMap;
import com.ouweicong.common.utils.Upload_to_Gitee;
import com.ouweicong.shop.dao.GoodsDao;

import com.ouweicong.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    //查询商品列表
    @GetMapping("/getGoods")
    public String getGoodsList(@RequestParam(required = false) String shopId) {
        String str = goodsService.getGoodsList(shopId);
        return str;
    }

    //添加商品
    @PostMapping("/addGoods")
    public String addGoods(String goodsData, @RequestBody(required = false) String goodsData2, MultipartFile imageData) {
        Goods goods = JSON.parseObject(goodsData == null ? goodsData2 : goodsData, Goods.class);
        try {
            if (imageData != null) {
                String url = Upload_to_Gitee.upload(imageData);
                goods.setGoodsImage(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        goods.setGoodsId(UUID.randomUUID().toString());
        Map<String, Object> goodsMap = ObjectToMap.convertObjectToMap(goods);
        return goodsService.addGoods(goodsMap);
    }

    //根据商品id获取所属商品详细
    @GetMapping("/queryGoodsInfoByGoodsId")
    public String queryGoodsInfoByGoodsId(@RequestParam String goodsId) {
        String str = goodsService.queryGoodsInfoByGoodsId(goodsId);
        return str;
    }

    //删除商品
    @DeleteMapping("/delGoods")
    public String delGoods(@RequestParam String goodsId) {
        String str;
        str = goodsService.delGoods(goodsId);
        return str;
    }

    //修改商品
    @PostMapping("/modifyGoods")
    public String modifyGoods(String goodsData, @RequestBody(required = false) String goodsData2, MultipartFile imageData) {
        Goods goods = JSON.parseObject(goodsData == null ? goodsData2 : goodsData, Goods.class);
        try {
            if (imageData != null) {
                String url = Upload_to_Gitee.upload(imageData);
                goods.setGoodsImage(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> goodsMap = ObjectToMap.convertObjectToMap(goods);
        String str = goodsService.modifyGoods(goodsMap);
        return str;
    }

    //根据商店id获取所属商品
    @GetMapping("/queryGoodsInfoByShopId")
    public String queryGoodsInfoByShopId(@RequestParam String shopId) {
        return goodsService.queryGoodsInfoByShopId(shopId);
    }

//    //查询商品是否存在
//    @GetMapping("/goodsIsExists")
//    public String goodsIsExists(@RequestParam String goodsId){
//        return goodsService.goodsIsExists(goodsId);
//    }

}
