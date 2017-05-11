package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;

import java.util.List;

/**
 * Created by zxg on 15/8/21.
 * 10:15
 */
public interface CommodityGoodsService {
    CommodityGoodsDO getCommodityGoodsByPrimaryKey(Integer id);

    boolean insertGoods(CommodityGoodsDO goodsDO);

    //自定义更新，id必填，所有参数选填
    boolean updateStatus(CommodityGoodsDO goodsDO);

    //批量插入数据
    boolean insertBatchGoods(List<CommodityGoodsDO> list);

    //批量插入大数据
    boolean insertBatchGoodsByLoadData(List<CommodityGoodsDO> list);


    boolean deleteBatchGoods(List<String> goodsUuIdList, Integer userId);
    //==============================具体更新===========

    /**
     * 根据 uuId 批量更新
     * @param saleStatus 销售状态
     * @param goodsUuIdList uuId
     * @param updateUserId 更新者
     * @param isdelete 0：正常 1：删除
     */
    boolean updateStatusBatch(Integer saleStatus, List<String> goodsUuIdList, Integer updateUserId, Integer isdelete);

    //新老分类替换
    boolean updateByPart(Integer oldPartId, Integer newPartId);
    //==============================具体查询业务========================


    CommodityGoodsDO getCommodityGoodsDOByCode(String goodsCode);

    boolean existCommodityGoods(String goodsCode);

    CommodityGoodsDO getCommodityGoods(String goodsCode,Integer goodsQualityType,Integer brandId );

    //获得已有的唯一的品牌和标准名称
    List<CommodityGoodsDO> getBrandPartGroupByThis();

    /**
     * 根据条件获得page
     * @param brandId 可选，品牌id
     * @param partId 可选 标准零件id
     * @param index 必选 页数
     * @param pageSize  必选 页大小
     */
    Page<CommodityGoodsDO> getGoodsPage(Integer brandId,Integer partId,String format,Integer index,Integer pageSize);

    //获得goods_car对应的status状态的所有uuId商品
    List<CommodityGoodsDO> getGoodsAndHasNewCar(Integer goodsCarStatus);

    /**
     * 根据品牌id和标准零件id获得对应所用商品
     * @param brandId 可为null，品牌id
     * @param partId    必填，标准零件名称id
     */
    List<CommodityGoodsDO> getGoodsByBrandPart(Integer brandId,Integer partId);
    /**
     * 根据品牌id获得对应所用商品
     * @param brandId 可为null，品牌id
     */
    List<CommodityGoodsDO> getGoodsByBrand(Integer brandId);
}

