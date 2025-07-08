package com.ouweicong.shop.service.Iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ouweicong.common.pojo.Goods;
import com.ouweicong.common.pojo.Shop;
import com.ouweicong.shop.dao.ShopDao;
import com.ouweicong.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ShopServiceImpl extends ServiceImpl<ShopDao, Shop> implements ShopService {
    @Autowired
    private ShopDao shopDao;

    public List<Goods> getGoodsJoinShop(){
        return shopDao.selectGoodsJoinShop();
    }

}
