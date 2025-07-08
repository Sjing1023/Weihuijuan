package com.ouweicong.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ouweicong.common.pojo.Goods;
import com.ouweicong.common.pojo.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ShopDao extends BaseMapper<Shop> {
    List<Goods> selectGoodsJoinShop();
}
