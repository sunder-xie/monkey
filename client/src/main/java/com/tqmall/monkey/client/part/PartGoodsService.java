package com.tqmall.monkey.client.part;

import com.tqmall.monkey.domain.entity.part.BasePartGoodDO;
import com.tqmall.monkey.domain.model.BasePartGoodsParams;

import java.util.List;

/**
 * 配件商品的service
 * Created by zxg on 15/7/25.
 */
public interface PartGoodsService {

/*==================================================base==============================================================*/



    /** 插入baseGoods。若无firstCateId,则调用方法填充，返回自增Id
     * @param basePartGoodDO
     */
    Integer insertBaseGoodsWithoutExist(BasePartGoodDO basePartGoodDO);


    /**
     * 根据uuId列表，批量删除该数据
     */
    boolean deleteBatchGoodsBaseByUuId(List<String> uuIdList);

    /**
     * 判断是否存在此base,若存在，返回对象列表
     */
    List<BasePartGoodDO> getBaseGoodsByParams(BasePartGoodsParams basePartGoodsParams);

    /**
     * 若存在对象，返回uuid,否则返回Null
     * @param basePartGoodDO
     * @return
     */
    BasePartGoodDO getBaseGoodsUUIdWithExist(BasePartGoodDO basePartGoodDO);

    Boolean updateByOeNumber(BasePartGoodDO basePartGoodDO);

     /*=================================特定更新===========================================================*/

    Boolean updateByPart(String oldPartSumCode, Integer newPartId);

    Integer getPartGoodsBaseOeNum();
}
