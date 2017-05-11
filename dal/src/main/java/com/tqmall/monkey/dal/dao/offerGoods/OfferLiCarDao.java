package com.tqmall.monkey.dal.dao.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.offerGoods.OfferLiCarDOMapper;
import com.tqmall.monkey.domain.entity.offerGoods.OfferLiCarDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * 供应商数据和力洋库的id
 * Created by zxg on 15/7/15.
 */
@MyBatisRepository
public class OfferLiCarDao extends BaseDao {

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.offerGoods.OfferLiCarDOMapper";

    @Autowired
    private OfferLiCarDOMapper offerLiCarDOMapper;

    public OfferLiCarDOMapper getOfferLiCarDOMapper() {
        return offerLiCarDOMapper;
    }

    /**
     * 根据goods_id、li_id/status查询记录，可谓空
     * @param goodsId 供应商商品ID
     * @param liyangId  力洋库的主键Id
     * @param status 状态
     * @return
     */
    public List<OfferLiCarDO> findOfferLiByGoodsIdLiIdStatus(Integer goodsId,String liyangId,Integer status){
        HashMap<String,Object> params = new HashMap<>();
        params.put("goodsId",goodsId);
        params.put("liyangId",liyangId);
        params.put("status",status);

        return sqlTemplate.selectList(NAMESPACE+".findOfferLiByGoodsIdLiIdStatus",params);
    }
}
