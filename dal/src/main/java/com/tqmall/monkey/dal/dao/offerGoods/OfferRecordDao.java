package com.tqmall.monkey.dal.dao.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.offerGoods.OfferRecordDOMapper;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by huangzhangting on 15/6/2.
 */
@MyBatisRepository
public class OfferRecordDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.offerGoods.OfferRecordDOMapper";

    @Autowired
    private OfferRecordDOMapper offerRecordDOMapper;

    public OfferRecordDOMapper getOfferRecordDOMapper() {
        return offerRecordDOMapper;
    }

    /**
     * 根据下列属性找到唯一的商品记录
     * @param goodsId 商品表的主键Id
     * @param recordName 记录表的名称
     * @param providerName 供应商名称
     * @param status 该报价单的状态
     * @return
     */
    public OfferRecordDO selectByGoodsIdRecordNameProviderNameStatus(Integer goodsId, String recordName, String providerName, Integer status){
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsId",goodsId);
        map.put("recordName",recordName);
        map.put("providerName",providerName);
        map.put("status",status);

        return sqlTemplate.selectOne(NAMESPACE + ".selectByGoodsIdRecordNameProviderNameStatus", map);
    }

    /**updated by zhongxigeng on 15/7/16
     * 获得供应商报价单列表
     * @param goodsId 商品ID
     * @param status 报价单状态
     * @return
     */
    public List<OfferRecordDO> findRecordListByGoodsIdAndStatus(Integer goodsId,Integer status,String provider_name){
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsId",goodsId);
        map.put("status",status);
        map.put("providerName",provider_name);

        return sqlTemplate.selectList(NAMESPACE + ".findRecordListByGoodsIdAndStatus", map);
    }

    //updated by zhongxigeng on 15/7/16
    public List<String> findProviderNameByStatus(Integer status){
        return sqlTemplate.selectList(NAMESPACE + ".findProviderNameByStatus", status);
    }

    //updated by zhongxigeng on 15/7/16
    public Integer getRecordSum(Integer status){

        return sqlTemplate.selectOne(NAMESPACE + ".getRecordSum",status);
    }
}
