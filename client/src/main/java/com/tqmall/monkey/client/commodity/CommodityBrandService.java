package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;

/**
 * Created by zxg on 15/8/14.
 */
public interface CommodityBrandService {
    //返回自增id
    Integer insertBrand(CommodityBrandDO commodityBrandDO);

    boolean updateBrand(CommodityBrandDO commodityBrandDO);

    boolean deleteBrand(CommodityBrandDO commodityBrandDO);

    //=================自定义get=========

    Page<CommodityBrandDO> getPageByCountroyKey(Integer index,Integer pageSize,Integer country,String nameKey);

    /**
     * 中文名获得对象，若没有，则通过英文搜
     * 支持 中文 、英文、中文/英文、英文/中文
     * @param nameKey
     * @return
     */
    CommodityBrandDO getCommodityBrandDOByName(String nameKey);

    CommodityBrandDO getCommodityBrandDOByPrimaryId(Integer id);
}
