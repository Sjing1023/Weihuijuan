package com.ouweicong.shop.Controller;

import com.ouweicong.common.pojo.Coupon;
import com.ouweicong.common.pojo.User;
import com.ouweicong.common.utils.JwtUtil;
import com.ouweicong.common.utils.ObjectToMap;
import com.ouweicong.shop.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class CouponController {
    @Autowired
    CouponService couponService;

    //查询优惠劵列表
    @GetMapping("/getCoupon")
    public String getCouponList(){
        String str = couponService.getCouponList();
        return str;
    }

    //添加优惠劵
    @PostMapping("/addCoupon")
    public String addCoupon(@RequestBody Coupon coupon,@CookieValue("access_token") String token){

        User user = JwtUtil.verify(token);
        coupon.setCouponId(UUID.randomUUID().toString());
        Map<String, Object> goodsMap = ObjectToMap.convertObjectToMap(coupon);
        return couponService.addCoupon(goodsMap,user.getUserId());
    }

    //删除优惠券
    @DeleteMapping("/delCoupon")
    public String delCoupon(@RequestParam String couponId){
        return couponService.delCoupon(couponId);
    }

//    //根据userId去添加优惠券
//    @PostMapping("/queryCouponInfoByUserId")
//    public String queryCouponInfoByUserId(@RequestBody Coupon coupon,String userId){
//        return null;
//    }
}
