package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.client.redisManager.goods.GoodsRedisManager;
import com.tqmall.monkey.dal.dao.commodity.CommodityBrandDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zxg on 15/8/17.
 */
@Service
public class CommodityBrandServiceImpl implements CommodityBrandService {

    @Autowired
    private CommodityBrandDao commodityBrandDao;
    @Autowired
    private GoodsRedisManager goodsRedisManager;

    @Override
    public Integer insertBrand(CommodityBrandDO commodityBrandDO) {
        try {
            if(commodityBrandDO.getCode() == null){
                commodityBrandDO.setCode("");
            }
            if(commodityBrandDO.getNameEn() == null){
                commodityBrandDO.setNameEn("");
            }
            if(commodityBrandDO.getNameCh() == null){
                commodityBrandDO.setNameCh("");
            }
            return goodsRedisManager.insertBrand(commodityBrandDO);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public boolean updateBrand(CommodityBrandDO commodityBrandDO) {
        try {
            return goodsRedisManager.updateBrand(commodityBrandDO);
        }catch (Exception e){
            try {
                throw new Exception("更新失败，已存在唯一的中文名、英文名、编码");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean deleteBrand(CommodityBrandDO commodityBrandDO) {
        try {
            return goodsRedisManager.deleteBrand(commodityBrandDO);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Page<CommodityBrandDO> getPageByCountroyKey(Integer index, Integer pageSize, Integer country, String nameKey) {
        return commodityBrandDao.getPageByCountroyKey(index, pageSize, country, nameKey);
    }

    @Override
    public CommodityBrandDO getCommodityBrandDOByName(String nameKey) {
        if(null == nameKey){
            return null;
        }
        String searchKey = nameKey.trim().replace("\\","");
        if("".equals(searchKey)){
            return null;
        }

        if(nameKey.contains("/")){
            String[] nameArray = nameKey.split("/");
            searchKey = nameArray[0];
        }
        CommodityBrandDO commodityBrandDO = commodityBrandDao.getCommodityBrandDOByNameCh(searchKey);
        if(null == commodityBrandDO){
            commodityBrandDO = commodityBrandDao.getCommodityBrandDOByNameEn(searchKey);
        }

        return commodityBrandDO;
    }

    @Override
    public CommodityBrandDO getCommodityBrandDOByPrimaryId(Integer id) {
        return goodsRedisManager.getBrandByPrimaryKey(id);
    }
}
