package com.tqmall.monkey.client.offerGoods;


import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsCarDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;

import java.util.List;

/**
 * Created by zxg on 2015/4/26.
 * 报价单处理Service
 */
public interface OfferGoodsService {

    //根据id获得报价单表所有数据
    OfferGoodsDO selectById(Integer id);


    //插入数据，返回主键（若OE存在，直接返回主键）
    Integer insertOfferGoodsWithExit(OfferGoodsDO offerGoodsDO);

    //更新，若主键无，则更新失败
    Integer updateOfferGoods(OfferGoodsDO offerGoodsDO);

    /*=================================特定更新===========================================================*/

    Boolean updateByPart(Integer oldPartId, Integer newPartId);
    /*=================================业务===========================================================*/

    //根据OE码获得报价单表所有数据
    OfferGoodsDO selectByOE(String OE);

    //根据分类状态获得所有供应商数据：0-新建 1-仅分类匹配成功 2-分类匹配失败，未导出 3-导入pool池 4-分类匹配失败，已导出
    List<OfferGoodsDO> findAllGoodsByCateStatus(Integer cateStatus);

    //根据分类状态获得供应商数据的数量
    Integer findGoodsSumByCateStatus(Integer cateStatus);

    //根据标准零件id获得数据
    List<OfferGoodsDO> findGoodsByPartId(Integer partId);


}
