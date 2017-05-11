package com.tqmall.monkey.dal.dao.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsOeDOMapper;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsOeDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/8/22.
 */

@MyBatisRepository
public class CommodityGoodsOeDao extends BaseDao {

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsOeDOMapper";


    @Autowired
    private CommodityGoodsOeDOMapper commodityGoodsOeDOMapper;

    public CommodityGoodsOeDOMapper getCommodityGoodsOeDOMapper() {
        return commodityGoodsOeDOMapper;
    }

    public void deleteBatchGoodsUuId(List<String> goodsUuIdList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",goodsUuIdList);
        map.put("updateUserId",updateUserId);

        sqlTemplate.update(NAMESPACE + ".deleteBatchGoodsUuId", map);
    }

    public List<CommodityGoodsOeDO> getListByUuId(String goodsUuId) {
        String selectSql = NAMESPACE+".selectOeByUuId";
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsUuId", goodsUuId);

        return sqlTemplate.selectList(selectSql,map);
    }

    //批量更新isdelete状态--id
    public void updateBatchIds(Integer isdelete,List<Integer> idList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",idList);
        map.put("updateUserId",updateUserId);
        map.put("isdelete",isdelete);

        sqlTemplate.update(NAMESPACE + ".updateBatchIds", map);
    }
}
