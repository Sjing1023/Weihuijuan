package com.ouweicong.order.controller;

import com.google.zxing.WriterException;
import com.ouweicong.common.controller.Result;
import com.ouweicong.common.pojo.Order;
import com.ouweicong.common.pojo.User;
import com.ouweicong.common.utils.JwtUtil;
import com.ouweicong.common.utils.ObjectToMap;
import com.ouweicong.order.service.OrderService;
import com.ouweicong.order.utils.QRCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    //下订单
    @PostMapping("/addOrder")
    public String addOrder(@RequestBody Map<String, String> orderData, @CookieValue("access_token") String token) {
        User user = JwtUtil.verify(token);
        if (user == null) {
            return Result.fail("token有问题");
        }

        String couponId = orderData.get("couponId");
        String goodsId = orderData.get("goodsId");
        Integer goods_count = Integer.valueOf(orderData.get("goods_count"));
        if (goodsId == null || goods_count == null) return Result.fail("下单错误，参数有误");
        Order newOrder = orderService.handleAddOrder(goodsId, goods_count, couponId, user);

        return Result.success(newOrder, "下订单成功");
    }

    //根据订单Id查询订单信息
    @GetMapping("/getOrderInfoByOrderId")
    public String getOrderInfoByOrderId(String orderId) {
        Order order = orderService.getOrderInfoByOrderId(orderId);
        Map<String, Object> orderMap = ObjectToMap.convertObjectToMap(order);
        orderMap.put("payMoney", String.format("%.2f", orderMap.get("payMoney")));
        orderMap.put("total_money", String.format("%.2f", orderMap.get("total_money")));
        return Result.success(orderMap, "查询成功");
    }

    //支付接口
    @PostMapping("/paySuccess")
    public String paySuccess(@RequestParam String orderId) {
        Boolean isSuccess = orderService.paySuccess(orderId);
        if (isSuccess) {
            return Result.success("支付成功");
        } else {
            return Result.fail("支付失败");
        }
    }

    @Autowired
    private QRCodeService qrCodeService;

    //根据订单Id生成二维码
    @GetMapping("/QRCodeByOrderId")
    public String getQRCodeImage(String orderId) throws WriterException, IOException {
        Order order = orderService.getOrderInfoByOrderId(orderId);
        if (order != null) {

            byte[] qrCodeImage = qrCodeService.generateQRCodeImage(order.getQRcode());

            return Result.success(Base64.getEncoder().encodeToString(qrCodeImage), "二维码生成成功");
        } else {
            return Result.fail("二维码生成失败");
        }
    }

    //查询订单列表
    @GetMapping("/getOrderList")
    public String getOrderList(@RequestParam(required = false) Integer type, @CookieValue("access_token") String token) {
        User user = JwtUtil.verify(token);
        if (user == null) {
            return Result.fail("token有问题");
        }

        Map<String, List> orderMap = orderService.getOrderList(type, user.getUserId());

        return Result.success(orderMap);
    }

    @PostMapping("/writeOffGoods")
    public String WriteOffGoods(@RequestParam String qrcode, @CookieValue("access_token") String token) {
        User user = JwtUtil.verify(token);
        if (user == null) {
            return Result.fail("token有问题");
        }
        Map map = orderService.WriteOffGoods(qrcode, user.getUserId());

        return map.get("status").toString().equals("true") ? Result.success("核销成功") : Result.fail(map.get("msg").toString());
    }
}
