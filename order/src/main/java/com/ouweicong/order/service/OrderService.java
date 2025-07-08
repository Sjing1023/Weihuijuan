package com.ouweicong.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ouweicong.common.pojo.Coupon;
import com.ouweicong.common.pojo.Goods;
import com.ouweicong.common.pojo.Order;
import com.ouweicong.common.pojo.Shop;
import com.ouweicong.common.pojo.User;
import com.ouweicong.common.utils.ObjectToMap;
import com.ouweicong.common.utils.RandomNumberGenerator;
import com.ouweicong.order.dao.OrderDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@Service
public class OrderService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    OrderDao orderDao;

    //下订单
    public Order handleAddOrder(String goodsId, Integer goods_count, String couponId, User user) {
        Order order = new Order();
        order.setUserId(user.getUserId());   //赋值下单用户Id
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        order.setOrderId(String.valueOf(snowflake.nextId()));     //赋值订单ID
        order.setCouponId(couponId);    //设置优惠券
        order.setGoodsId(goodsId);      //设置商品ID
        order.setGoods_count(goods_count);  //设置商品数量

        //获取商品信息
        Map goodsMap = (Map) restTemplate.getForObject("http://localhost:10086/shopService/queryGoodsInfoByGoodsId?goodsId=" + goodsId, Map.class).get("data");
        Goods goods = JSON.toJavaObject(new JSONObject(goodsMap), Goods.class);
        //获取优惠券信息
        Coupon coupon = null;
        if (couponId != null) {

        }
        //计算总价
        Float total_money = goods.getGoodsPrice() * goods_count;
        //应付金额
        Float payMoney = total_money - 0;   //减去优惠卷金额

        order.setTotal_money(total_money);  //赋值总价
        order.setPayMoney(payMoney);    //赋值应付金额

        order.setStatus(0); //设置状态为待付款

        String qrCode = RandomNumberGenerator.generateRandomNumber(12);
        order.setQRcode(qrCode);

        orderDao.addOrder(ObjectToMap.convertObjectToMap(order));   //数据库新增订单
        order = orderDao.getOrderInfoByOrderId(order.getOrderId());
        return order;
    }

    //根据订单Id查询订单信息
    public Order getOrderInfoByOrderId(String orderId) {
        if (orderId == null) return null;
        return orderDao.getOrderInfoByOrderId(orderId);
    }

    //成功支付
    public Boolean paySuccess(String orderId) {
        if (orderId == null) return false;

        orderDao.paySuccess(orderId);
        return true;
    }

    //查询订单列表
    public Map<String, List> getOrderList(Integer type, String userId) {
        //获取商品信息
        List<Map> goodsList = (List<Map>) restTemplate.getForObject("http://localhost:10086/shopService/getGoods", Map.class).get("data");
        Map goodsMap = new HashMap<>();
        for (Map goods : goodsList) {
            goodsMap.put(goods.get("goodsId"), goods);
        }

        List<Order> orderList = Arrays.asList(orderDao.getOrderList(userId));
        Map<String, List> returnMap = new HashMap<>();
        if (type == null && orderList.size() > 0) {
            returnMap.put("all", new ArrayList<>());
        }
        for (Order order : orderList) {
            Map<String, Object> orderMap = ObjectToMap.convertObjectToMap(order);

            //处理金额小数点
            orderMap.put("payMoney", String.format("%.2f", orderMap.get("payMoney")));
            orderMap.put("total_money", String.format("%.2f", orderMap.get("total_money")));
            //放入商品信息
            orderMap.put("goodsData", goodsMap.get(order.getGoodsId()));

            returnMap.get("all").add(JSON.parse(JSON.toJSONString(orderMap)));

            if (type != null && order.getStatus() != type) continue;
            if (!returnMap.containsKey(order.getStatus().toString())) {
                returnMap.put(order.getStatus().toString(), new ArrayList<>());
            }
            returnMap.get(order.getStatus().toString()).add(JSON.parse(JSON.toJSONString(orderMap)));
        }

        return returnMap;
    }

    //核销订单
    public Map WriteOffGoods(String qrcode, String userId) {
        Map map = new HashMap<>();
        Order order = orderDao.getOrderInfoByQRcode(qrcode);
        Goods goods = JSON.toJavaObject(new JSONObject((Map) restTemplate.getForObject("http://localhost:10086/shopService/queryGoodsInfoByGoodsId?goodsId=" + order.getGoodsId(), Map.class).get("data")), Goods.class);

        Shop shop = JSON.toJavaObject(new JSONObject((Map) restTemplate.getForObject("http://localhost:10086/shopService/shop/getShopInfoByShopId?shopId=" + goods.getShopId(), Map.class).get("data")), Shop.class);

        if (!shop.getUserId().equals(userId)) {
            map.put("status", false);
            map.put("msg", "核销的非本店铺商品");
        } else if (order.getStatus() == 2) {
            map.put("status", false);
            map.put("msg", "订单已核销！核销时间：" + order.getUse_time());
        } else if (order.getStatus() == 1) {
            int changeCount = orderDao.WriteOffGoods(qrcode);
            if (changeCount > 0) {
                map.put("status", true);
                map.put("msg", "核销成功");
            }
        } else {
            map.put("status", false);
            map.put("msg", "核销失败");
        }
        return map;
    }
}
