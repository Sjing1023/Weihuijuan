package com.ouweicong.order.dao;

import com.ouweicong.common.pojo.Order;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDao {
    void addOrder(@Param("order") Map<String,Object> orderMap);

    Order getOrderInfoByOrderId(@Param("orderId") String orderId);
    Order getOrderInfoByQRcode(@Param("QRcode") String QRcode);

    void paySuccess(@Param("orderId") String orderId);

    Order[] getOrderList(@Param("userId") String userId);

    int WriteOffGoods(@Param("QRcode") String QRcode);
}
