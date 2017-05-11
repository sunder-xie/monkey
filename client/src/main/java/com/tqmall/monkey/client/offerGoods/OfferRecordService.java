package com.tqmall.monkey.client.offerGoods;


import com.tqmall.monkey.domain.entity.car.GoodsCarInfoDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/6/2.
 */
public interface OfferRecordService {
    Integer insertRecord(OfferRecordDO recordDO);

    Integer updateRecord(OfferRecordDO recordDO);

    List<GoodsCarInfoDO> findGoodsCarInfo(Integer status);

    void modifyOfferRecord(OfferRecordDO record);

    /**
     * 根据下列属性找到唯一的商品记录
     * @param goodsId 商品表的主键Id
     * @param recordName 记录表的名称
     * @param providerName 供应商名称
     * @param status 该报价单的状态
     * @return
     */
    OfferRecordDO findByGoodsIdRecordNameProviderNameStatus(Integer goodsId,String recordName,String providerName,Integer status);

    /**
     * 根据record中的数据，获得列表
     * @param goodsId 商品ID  可为inull
     * @param status  报价单状态 可为null
     * @return
     */
    List<OfferRecordDO> findRecordListByGoodsIdAndStatus(Integer goodsId, Integer status,String provider_name);

    //获得供应商的名单，groupBy
    List<String> findProviderNameByStatus(Integer status);

    //根据状态获得报价单记录数
    Integer getRecordSum(Integer status);
}
