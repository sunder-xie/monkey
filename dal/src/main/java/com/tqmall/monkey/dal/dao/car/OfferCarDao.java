package com.tqmall.monkey.dal.dao.car;


import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.car.OfferCarDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.car.OfferCarDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@MyBatisRepository
public class OfferCarDao extends BaseDao {

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.car.OfferCarDOMapper";

    @Autowired
    private OfferCarDOMapper offerCarDOMapper;

    public OfferCarDOMapper getOfferCarDOMapper() {
        return offerCarDOMapper;
    }

    /**
     *
     * @return
     */
    public Page<OfferCarDO> getCarsPage(Map<String, Object> params, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getCarsPage";
        //String countSql = NAMESPACE + ".getTotalCount";
        Page<OfferCarDO> page = this.queryPage(selectSql, null, params, pageIndex, pageSize);
        return page;
    }

    //====================车型和商品的关系==============
    public boolean ExitCarAndGoodsRelation(Integer car_id, Integer goods_id){
        HashMap<String,Object> map = new HashMap<>();
        map.put("carId",car_id);
        map.put("goodsId", goods_id);
        return sqlTemplate.selectOne(NAMESPACE + ".ExitCarAndGoodsRelation",map);
    }

    public void insertCarAndGoods(Integer car_id, Integer goods_id){
        HashMap<String,Object> map = new HashMap<>();
        map.put("carId",car_id);
        map.put("goodsId",goods_id);
        sqlTemplate.insert(NAMESPACE+".insertCarAndGoods",map);
    }


}
