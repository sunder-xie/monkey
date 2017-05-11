package com.tqmall.monkey.dal.dao.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.offerGoods.OfferRecordDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by ximeng on 2015/4/30.
 * 报价单记录的dao
 */
@MyBatisRepository
public class OfferGoodsRecordDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.offerGoods.OfferRecordDOMapper";
    @Autowired
    private OfferRecordDOMapper offerRecordDOMapper;

    public OfferRecordDOMapper getOfferRecordDOMapper() {
        return offerRecordDOMapper;
    }

    /**根据报价单基本商品主键ID获得供应商列表信息
     * goodsId：商品Id
     * index:开始页码
     * pageSize：页面大小
     */
    public Page<OfferRecordDO> queryPagesByOfferGoodsId(Map<String, Object> params,int index,int pageSize){
        String selectSql = NAMESPACE + ".queryPagesByOfferGoodsId";
        Page<OfferRecordDO> page = this.queryPage(selectSql,null, params, index, pageSize);
        return page;
    }

    //查询采购单的总数
    public int countPagesByOfferGoodsId(Map<String, Object> params){
        String countSql = NAMESPACE + ".countPagesByOfferGoodsId";
        return sqlTemplate.selectOne(countSql,params);
    }
}
