package com.tqmall.monkey.client.redisManager.goods;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tqmall.monkey.component.helper.SearchServiceHelp;
import com.tqmall.monkey.component.redis.RedisClientTemplate;
import com.tqmall.monkey.component.redis.RedisKeyBean;
import com.tqmall.monkey.dal.dao.commodity.CommodityBrandDao;
import com.tqmall.monkey.dal.dao.online.OnlineGoodsDao;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxg on 15/9/28.
 * 商品缓存类
 */
@Service
@Slf4j
public class GoodsRedisManagerImpl implements GoodsRedisManager {


    //品牌主键
    private static final String brandByPrimaryKey = RedisKeyBean.brandByPrimaryKey;
    //所有品牌
    private static final String AllBrandKey = RedisKeyBean.AllBrandKey;
    //线上所需商品map
    private static final String OnlineGoodsByBrandFormat = RedisKeyBean.OnlineGoodsByBrandFormat;

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Autowired
    private CommodityBrandDao commodityBrandDao;
    @Autowired
    private OnlineGoodsDao onlineGoodsDao;


    //=================================brand=================
    public CommodityBrandDO getBrandByPrimaryKey(Integer brandId){
        String key = String.format(brandByPrimaryKey,brandId);
        CommodityBrandDO commodityBrandDO = redisClientTemplate.lazyGet(key,CommodityBrandDO.class);
        if(null == commodityBrandDO){
            commodityBrandDO = commodityBrandDao.getCommodityBrandDOMapper().selectByPrimaryKey(brandId);
            if(null != commodityBrandDO){
                redisClientTemplate.lazySet(key,commodityBrandDO,RedisKeyBean.RREDIS_EXP_WEEK);
            }
        }

        return commodityBrandDO;
    }

    public Integer insertBrand(CommodityBrandDO commodityBrandDO) {
        try {
            // 查询该数据是否还存在在数据库，存在就恢复
            Integer id = commodityBrandDao.getCommodityBrandDOMapper().selectByNameCode(commodityBrandDO.getNameEn(),commodityBrandDO.getNameCh(),commodityBrandDO.getCode());
            if(null == id) {
                commodityBrandDao.getCommodityBrandDOMapper().insertSelective(commodityBrandDO);
                id = commodityBrandDO.getId();
            }else{
                CommodityBrandDO changeBrandDO = new CommodityBrandDO();
                changeBrandDO.setIsdelete(0);
                changeBrandDO.setId(id);
                changeBrandDO.setModifier(commodityBrandDO.getModifier());
                commodityBrandDao.getCommodityBrandDOMapper().changeIsDelete(changeBrandDO);
            }

            String key = String.format(brandByPrimaryKey, id);
            redisClientTemplate.lazySet(key,commodityBrandDO,RedisKeyBean.RREDIS_EXP_DAY);

            redisClientTemplate.delKey(AllBrandKey);
            return id;
        }catch (Exception e){
            log.error(e.getMessage(),e);

            return 0;
        }
    }

    public boolean updateBrand(CommodityBrandDO commodityBrandDO) {
        try {
            Integer id = commodityBrandDO.getId();
            if(null == id){
                return false;
            }
            String key = String.format(brandByPrimaryKey,id);
            commodityBrandDao.getCommodityBrandDOMapper().updateByPrimaryKeySelective(commodityBrandDO);
            redisClientTemplate.lazySet(key, commodityBrandDO, RedisKeyBean.RREDIS_EXP_DAY);

            redisClientTemplate.delKey(AllBrandKey);

            return true;
        }catch (Exception e){
            log.error(e.getMessage(),e);

            return false;
        }
    }

    public boolean deleteBrand(CommodityBrandDO commodityBrandDO) {
        try {
            Integer id = commodityBrandDO.getId();
            if(null == id){
                return false;
            }
            String key = String.format(brandByPrimaryKey,id);
            commodityBrandDO.setIsdelete(1);
            commodityBrandDao.getCommodityBrandDOMapper().changeIsDelete(commodityBrandDO);

            redisClientTemplate.delKey(key);
            redisClientTemplate.delKey(AllBrandKey);
            return true;
        }catch (Exception e){
            log.error(e.getMessage(),e);

            return false;
        }
    }

    @Override
    public List<CommodityBrandDO> getAllBrand() {
        try {
            List<CommodityBrandDO> list = redisClientTemplate.lazyGetList(AllBrandKey, CommodityBrandDO.class);
            if(null == list || list.isEmpty()){
                list = commodityBrandDao.getAllCommodityBrand();
                redisClientTemplate.lazySet(AllBrandKey,list,null);
            }
            return list;
        }catch (Exception e){
            log.error(e.getMessage(),e);

            return new ArrayList<>();
        }
    }


    @Override
    public Multimap<String, Integer> getGoodsListOfFormatBrand() {
        List<HashMap<String, Object>> list = redisClientTemplate.lazyGetList(OnlineGoodsByBrandFormat, HashMap.class);
        if(null == list || list.isEmpty()){
            list = onlineGoodsDao.getGoodsListOfFormatBrand();
            redisClientTemplate.lazySet(OnlineGoodsByBrandFormat,list,RedisKeyBean.RREDIS_EXP_WEEK);
        }

        Multimap<String, Integer> brandFormatMap = ArrayListMultimap.create();
        for(HashMap<String, Object> resultMap : list){
            String format = ((String)resultMap.get("format")).trim().replaceAll(" +","");
            String brandName = ((String)resultMap.get("brandName")).trim().replaceAll(" +", "").toUpperCase();
            Integer id = Integer.parseInt(resultMap.get("goodsId").toString());

            String key = brandName+"_"+format;

            brandFormatMap.put(key,id);
        }

        return brandFormatMap;
    }
}
