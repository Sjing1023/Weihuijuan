package com.ouweicong.shop.service;

import com.ouweicong.common.controller.Result;
import com.ouweicong.common.pojo.Coupon;
import com.ouweicong.shop.dao.CouponDao;
import com.ouweicong.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.channels.Pipe;
import java.util.Map;

@Service
public class CouponService {
    @Autowired
    CouponDao couponDao;


    //查询优惠劵列表
    public String getCouponList(){
        Coupon[] couponList = couponDao.getCouponList();
        String res = Result.success(couponList,"查询成功");
        return res;
    }

    //添加优惠券
    public String addCoupon(Map<String,Object> coupon,String userId){
        coupon.put("userId",userId);
        couponDao.addCoupon(coupon);
        return Result.success("添加成功");
    }

    //删除优惠券
    public String delCoupon(String couponId) {
        couponDao.delCoupon(couponId);
        return Result.success("删除成功");
    }
}
