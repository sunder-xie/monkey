package com.tqmall.monkey.dal.dao.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.offerGoods.OfferGoodsDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.offerGoods.OfferGoodsDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ximeng on 2015/4/26.
 * 报价单基本商品数据DAO类
 */
@MyBatisRepository
public class OfferGoodsDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.offerGoods.OfferGoodsDOMapper";
    @Autowired
    private OfferGoodsDOMapper offerGoodsDOMapper;

    public OfferGoodsDOMapper getOfferGoodsDOMapper() {
        return offerGoodsDOMapper;
    }

    //根据OE码获得报价单池中的基本商品数据
    public OfferGoodsDO selectByOeNumber(String OENumber){
        return sqlTemplate.selectOne(NAMESPACE + ".selectByOeNumber", OENumber);
    }

    /**
     * 模糊检索出一定范围的配件数据,按时间降序（desc）
     *  offer_car_name 车型名字
     *  brand_name 商品品牌名字
     *  goods_name 商品名字
     *  brand_partcode 商品品牌零件号
     *  oe_num 商品oe码
     * @param pageSize 取时间最近的条目数量
     * @return
     */
    public Page<OfferGoodsDO> queryOfferGoodsPage( Map<String, Object> params,int index,int pageSize){
        String selectSql = NAMESPACE + ".queryOfferGoodsPage";
        Page<OfferGoodsDO> page = this.queryPage(selectSql,null, params, index, pageSize);
        return page;
    }

    //根据oe码、品牌名称和属性获得绝对的一个商品
    public OfferGoodsDO selectByOeBrandQuality(String OENumber,String brandName,Integer qualityType){
        HashMap<String,Object> map = new HashMap<>();
        map.put("oe",OENumber);
        map.put("brandName",brandName);
        map.put("quality",qualityType);
        return sqlTemplate.selectOne(NAMESPACE + ".selectByOeBrandQuality", map);
    }

    //根据cate状态分页获得商品数据
    public Page<OfferGoodsDO> getGoodsPage(Integer cateStatus, int pageIndex, int pageSize){
        String selectSql = NAMESPACE + ".getGoodsPage";

        HashMap<String,Object> map = new HashMap<>();
        map.put("cateStatus",cateStatus);
        Page<OfferGoodsDO> page = this.queryPage(selectSql, null, map, pageIndex, pageSize);
        return page;
    }
}
