package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsAttrDO;

import java.util.List;

/**
 * Created by zxg on 15/8/22.
 */
public interface CommodityGoodsAttrService {

    boolean insertAttr(CommodityGoodsAttrDO commodityGoodsAttrDO);

    boolean updateAttr(CommodityGoodsAttrDO commodityGoodsAttrDO);

    boolean insertBatchAttr(List<CommodityGoodsAttrDO> list);

    //大数据
    boolean insertBatchAttrByLoadData(List<CommodityGoodsAttrDO> list);

    boolean deleteBatchAttr(List<String> goodsUuIdList,Integer userId);

    //===================详细的查找
    List<CommodityGoodsAttrDO> getListByGoodsUuId(String goodsUuId);

    CommodityGoodsAttrDO getByUuIdAttrId(String uuId,Integer attrId);
}
