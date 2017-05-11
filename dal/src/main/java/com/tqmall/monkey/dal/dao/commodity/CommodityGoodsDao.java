package com.tqmall.monkey.dal.dao.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/8/21.
 * 商品库基本表
 */

@MyBatisRepository
public class CommodityGoodsDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsDOMapper";

    @Autowired
    private CommodityGoodsDOMapper commodityGoodsDOMapper;

    public CommodityGoodsDOMapper getCommodityGoodsDOMapper() {
        return commodityGoodsDOMapper;
    }

    public void deleteBatchGoodsUuId(List<String> goodsUuIdList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",goodsUuIdList);
        map.put("updateUserId",updateUserId);
        map.put("isdelete",1);

        sqlTemplate.update(NAMESPACE + ".updateBatchUuId", map);

//        sqlTemplate.update(NAMESPACE + ".deleteBatchGoodsUuId", map);
    }



    public List<CommodityGoodsDO> getBrandPartGroupByThis(){
        return sqlTemplate.selectList(NAMESPACE+".getBrandPartGroupByThis",null);
    }


    public Page<CommodityGoodsDO> getGoodsPage(Integer brandId, Integer partId,String format, Integer index, Integer pageSize) {
        String selectSql = NAMESPACE+".selectGoodsByBrandPart";
        String countSql = NAMESPACE+".selectGoodsByBrandPartCount";
        HashMap<String,Object> map = new HashMap<>();
        map.put("brandId",brandId);
        map.put("partId",partId);
        map.put("goodsFormat",format);

        return this.queryPage(selectSql, countSql, map, index, pageSize);
    }

    public CommodityGoodsDO getCommodityGoods(String goodsCode, Integer goodsQualityType, Integer brandId){
        String selectSql = NAMESPACE+".getCommodityGoodsByCodeQuaBrand";

        HashMap<String,Object> map = new HashMap<>();
        map.put("brandId",brandId);
        map.put("goodsCode",goodsCode);
        map.put("goodsQualityType",goodsQualityType);


        return sqlTemplate.selectOne(selectSql, map);
    }


    //获得goods_car对应的status状态的所有uuId商品
    public List<CommodityGoodsDO> getGoodsAndHasNewCar(Integer goodsCarStatus){
        String selectSql = NAMESPACE+".getGoodsAndHasNewCar";
        if(goodsCarStatus == null ){
            goodsCarStatus = 0;
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsCarStatus",goodsCarStatus);

        return sqlTemplate.selectList(selectSql, map);
    }


    //批量更新saleStatus状态
    public void updateSaleStatusBatchUuId(Integer saleStatus,List<String> goodsUuIdList,Integer updateUserId,Integer isdelete){
        Map<String,Object> map = new HashMap<>();
        map.put("list",goodsUuIdList);
        map.put("updateUserId",updateUserId);
        map.put("saleStatus",saleStatus);
        map.put("isdelete",isdelete);

        sqlTemplate.update(NAMESPACE + ".updateBatchUuId", map);
    }


    /**
     * 根据品牌id和标准零件id获得对应所用商品
     * @param brandId 可为null，品牌id
     * @param partId    必填，标准零件名称id
     * @return
     */
    public List<CommodityGoodsDO> getGoodsByBrandPart(Integer brandId,Integer partId){
        String selectSql = NAMESPACE+".selectGoodsByBrandPart";
        HashMap<String,Object> map = new HashMap<>();
        map.put("brandId",brandId);
        map.put("partId",partId);

        return sqlTemplate.selectList(selectSql,map);
    }

}
