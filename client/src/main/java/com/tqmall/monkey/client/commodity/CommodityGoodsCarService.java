package com.tqmall.monkey.client.commodity;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsCarDO;

import java.util.List;

/**
 * Created by zxg on 15/8/22.
 */
public interface CommodityGoodsCarService {

    boolean insertGoodsCar(CommodityGoodsCarDO commodityGoodsCarDO);

    boolean insertBatchCar(List<CommodityGoodsCarDO> list);

    void updateGoodsCar(CommodityGoodsCarDO commodityGoodsCarDO);
    //大数据批量
    boolean insertBatchCarByLoadData(List<CommodityGoodsCarDO> list);

    boolean deleteBatchAttr(List<String> goodsUuIdList,Integer userId);

    boolean updataStatusBatch(Integer status,List<String> goodsUuIdList,Integer updateUserId);

    boolean updataStatusBatchByLiyang(Integer status,List<String> liyangList,Integer updateUserId);

    boolean updataBatchById(Integer isdetele,List<Integer> idsList,Integer updateUserId);

    //===================详细的查找
    Page<CommodityGoodsCarDO> getPageByGoodsUuId(String uuId,String searchLiyang,Integer index,Integer pageSize);

    List<CommodityGoodsCarDO> getListByGoodsUuId(String uuId);

    //无论是否删除均遍历出来
    List<CommodityGoodsCarDO> getListByGoodsUuIdWithOutDelete(String uuId);

    List<CommodityGoodsCarDO> getListByGoodsUuIdStatus(String uuId,Integer status);

    List<String> getLiYangListGroupBy(List<String> uuIdList,Integer status);

    List<CommodityGoodsCarDO> getListByGBrandGPart(Integer brandId,Integer partId);

    CommodityGoodsCarDO selectByUuIdLiyangWithoutDelete(String uuId,String liyangId);

}
