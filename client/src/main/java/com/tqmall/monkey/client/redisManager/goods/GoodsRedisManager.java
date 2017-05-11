package com.tqmall.monkey.client.redisManager.goods;

import com.google.common.collect.Multimap;
import com.tqmall.monkey.component.helper.SearchServiceHelp;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.dal.dao.commodity.CommodityBrandDao;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zxg on 15/9/28.
 * 商品缓存类
 */
public interface GoodsRedisManager {


    //=================================brand=================
    CommodityBrandDO getBrandByPrimaryKey(Integer brandId);

    Integer insertBrand(CommodityBrandDO commodityBrandDO) ;

    boolean updateBrand(CommodityBrandDO commodityBrandDO) ;

    boolean deleteBrand(CommodityBrandDO commodityBrandDO);

    //获得所有品牌
    List<CommodityBrandDO> getAllBrand();

    //==============online===============
    Multimap<String, Integer> getGoodsListOfFormatBrand();


}
