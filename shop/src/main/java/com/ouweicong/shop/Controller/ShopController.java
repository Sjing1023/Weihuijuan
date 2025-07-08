package com.ouweicong.shop.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ouweicong.common.controller.Result;
import com.ouweicong.common.pojo.Goods;
import com.ouweicong.common.pojo.Shop;
import com.ouweicong.common.pojo.User;
import com.ouweicong.common.utils.JwtUtil;
import com.ouweicong.common.utils.ObjectToMap;
import com.ouweicong.common.utils.Upload_to_Gitee;
import com.ouweicong.shop.service.Iml.ShopServiceImpl;
import com.ouweicong.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopServiceImpl service;

    //新增店铺
    @PostMapping("/add")
    public String addShop(String shopData, @RequestBody(required = false) String shopData2, MultipartFile imageData, @CookieValue("access_token") String token) {
        Shop shop = JSON.parseObject(shopData == null ? shopData2 : shopData, Shop.class);
        log.info("新增店铺，店铺信息：{}", shop.toString());
        try {
            if (imageData != null) {
                String url = Upload_to_Gitee.upload(imageData);
                shop.setShopImage(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        shop.setShopId(UUID.randomUUID().toString());
        User user = JwtUtil.verify(token);
        if (user != null) {
            shop.setUserId(user.getUserId());
        }
        shopService.save(shop);
        return Result.success("新增店铺成功");
    }


    //查询店铺
    @GetMapping("/query")
    public String queryShop(@RequestParam(required = false) Integer pages, @RequestParam(required = false) Integer pageSize, String name, @CookieValue("access_token") String token) {
        log.info("page = {},pageSize = {},shop = {}", pages, pageSize, name);
        User user = JwtUtil.verify(token);
        if (user == null) {
            return Result.fail("token有问题");
        }
        //根据uerId过滤shop的全部信息
        //构造条件构造器
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", user.getUserId()).ne("sys_state", 1);
        if (pages != null && pageSize != null) {
            //构造分页构造器
            Page pageInfo = new Page(pages, pageSize);
            //添加过滤条件
            queryWrapper.like(StringUtils.isNotEmpty(name), "shopName", name);
            Page shopList = shopService.page(pageInfo, queryWrapper);
            return Result.success(shopList.getRecords());
        }
        List<Shop> shop = shopService.list(queryWrapper);
        return Result.success(shop);
    }

    //删除店铺
    @DeleteMapping("/delete")
    public String delete(@RequestBody Shop shop) {
            log.info("用户：{}", shop.getShopId());
        UpdateWrapper<Shop> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("shopId", shop.getShopId()).set("sys_state", 1);
        shopService.update(updateWrapper);
        return Result.success("删除成功");
    }

    //根据id修改店铺信息
    @PostMapping("/update")
    public String updateById(String shopData, @RequestBody(required = false) String shopData2, MultipartFile imageData, @CookieValue("access_token") String token) {
        Shop shop = JSON.parseObject(shopData == null ? shopData2 : shopData, Shop.class);
        try {
            if (imageData != null) {
                String url = Upload_to_Gitee.upload(imageData);
                shop.setShopImage(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(shop.toString());
        User user = JwtUtil.verify(token);
        if (user == null) {
            return Result.fail("token有问题");
        }
        //根据shopId查询userId
        Shop diJia = shopService.getById(shop.getShopId());

        if (!diJia.getUserId().equals(user.getUserId())) {
            return Result.fail("该用户无法更改店铺");
        }
        //修改信息
        UpdateWrapper<Shop> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("shopId", shop.getShopId());
        for (Map.Entry entry : ObjectToMap.convertObjectToMap(shop).entrySet()) {
            if (entry.getValue() == null || entry.getKey().toString().equals("shopId")) continue;
            updateWrapper.set(entry.getKey().toString(), entry.getValue());
        }

//        if (StringUtils.isNotBlank(shop.getShopName())) {
//            updateWrapper.eq("shopId", shop.getShopId()).set("shopName", shop.getShopName());
//        }
//        if (StringUtils.isNotBlank(shop.getShopImage())) {
//            updateWrapper.eq("shopId", shop.getShopId()).set("shopImage", shop.getShopImage());
//        }
//        if (StringUtils.isNotBlank(shop.getUserId())) {
//            updateWrapper.eq("shopId", shop.getShopId()).set("shopUserId", shop.getUserId());
//        }
        shopService.update(updateWrapper);
        return Result.success("店铺信息修改成功");
    }

    //查询店铺列表包含商品数据
    @GetMapping("/shopListWithGoods")
    public String shopListWithGoods() {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("sys_state", 1).orderByDesc("create_time");
        List<Shop> shops = shopService.list(queryWrapper);
        List<Goods> goods = service.getGoodsJoinShop();
        List<Map<String, Object>> resList = new ArrayList<>();
        Map<String, List<Object>> pointer = new HashMap<>();
        //处理商店列表，初始化goodsList
        for (Shop shop : shops) {
            Map<String, Object> shopMap = ObjectToMap.convertObjectToMap(shop);
            List list = new ArrayList<>();
            shopMap.put("goodsList",list);
            pointer.put(shop.getShopId(), list);
            resList.add(shopMap);
        }
        //给goods分配到对应的shop里
        for (Goods goods1 : goods) {
            if(goods1.getStatus() == 1) continue;   //下架商品不用展示
            pointer.get(goods1.getShopId()).add(goods1);
        }
        return Result.success(resList);
    }

    //根据shopId查询商品信息
    @GetMapping("/getShopInfoByShopId")
    public String getShopInfoByShopId(String shopId){
        QueryWrapper<Shop> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("shopId",shopId).ne("sys_state",1);
        Shop ShopInfo = shopService.getOne(queryWrapper);
        return Result.success(ShopInfo);
    }

}
