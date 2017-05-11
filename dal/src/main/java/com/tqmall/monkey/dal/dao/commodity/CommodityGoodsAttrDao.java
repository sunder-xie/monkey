package com.tqmall.monkey.dal.dao.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsAttrDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsAttrDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/8/22.
 */
@MyBatisRepository

public class CommodityGoodsAttrDao extends BaseDao{

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsAttrDOMapper";

    @Autowired
    private CommodityGoodsAttrDOMapper commodityGoodsAttrDOMapper;

    public CommodityGoodsAttrDOMapper getCommodityGoodsAttrDOMapper() {
        return commodityGoodsAttrDOMapper;
    }

    public void deleteBatchGoodsUuId(List<String> goodsUuIdList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",goodsUuIdList);
        map.put("updateUserId",updateUserId);

        sqlTemplate.update(NAMESPACE + ".deleteBatchGoodsUuId", map);
    }

    public Page<CommodityGoodsAttrDO> getPageByGoodsCode(String goodsUuId, Integer index, Integer pageSize) {
        String selectSql = NAMESPACE+".selectAttrByU";
        String countSql = NAMESPACE+".selectAttrByUuIdCount";
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsUuId", goodsUuId);

        return this.queryPage(selectSql,countSql,map,index,pageSize);
    }



    public List<CommodityGoodsAttrDO> getListByGoodsUuId(String goodsUuId) {
        String selectSql = NAMESPACE+".selectAttrByUuId";
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsUuId", goodsUuId);

        return sqlTemplate.selectList(selectSql,map);
    }
}
