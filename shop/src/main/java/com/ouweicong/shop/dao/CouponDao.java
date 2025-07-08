package com.ouweicong.shop.dao;

import com.ouweicong.common.pojo.Coupon;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Repository
public interface CouponDao {

    //查询优惠劵列表
    Coupon[] getCouponList();

    //添加优惠券
    public void addCoupon(@Param("coupon") Map<String , Object> coupon);

    //删除优惠券
    public void delCoupon(@Param("couponId") String couponId);
}
