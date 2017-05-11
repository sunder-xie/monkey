package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsOeDO;

import java.util.List;

/**
 * Created by zxg on 15/8/22.
 */
public interface CommodityGoodsOeService {
    /**
     * 插入oe码，若存在数据，则将删除状态置为0；
     * @param commodityGoodsOeDO 插入数据
     * @return
     */
    boolean insertOe(CommodityGoodsOeDO commodityGoodsOeDO);

    boolean insertBatchOe(List<CommodityGoodsOeDO> list);

    boolean deleteOeByGoodsUuId(String goodsUuId,Integer userId);

    //大数据
    boolean insertBatchOeByLoadData(List<CommodityGoodsOeDO> list);

    boolean deleteBatchOeByGoodsUuId(List<String> goodsUuIdList,Integer userId);

    //=====update===========
    boolean updataBatchById(Integer isdetele, List<Integer> idsList, Integer updateUserId);

    //====================get============
    List<CommodityGoodsOeDO> getListByUuId(String uuId);

    CommodityGoodsOeDO getByUuIdOe(String uuId,String oe);
}
