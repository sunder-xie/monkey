package com.tqmall.monkey.dal.dao.online;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.online.OnlineGoodsDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxg on 15/9/11.
 * 线上商品库的商品表数据
 */

@MyBatisRepository
public class OnlineGoodsDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.online.OnlineGoodsDOMapper";

    @Autowired
    private OnlineGoodsDOMapper onlineGoodsDOMapper;

    public OnlineGoodsDOMapper getOnlineGoodsDOMapper() {
        return onlineGoodsDOMapper;
    }

    /**
     * 线上商品id集合
     * @param format 规格型号
     * @param brandName 商品品牌名称
     * @return primaryId
     */
    public List<Integer> getGoodsIdListByFormatBrand(String format,String brandName){
        String sql = NAMESPACE+".getGoodsIdListByFormatBrand";
        HashMap<String,Object> map = new HashMap<>();

        map.put("goodsFormat",format);
        map.put("brandName",brandName);

        return sqlTemplate.selectList(sql,map);
    }

    /**
     * 特殊sql，返回所有onlineGId，goodsFormat，brandName
     * @return  dg.goods_id as 'goodsId',dg.goods_format as 'format',db.brand_name as 'brandName'
     */
    public List<HashMap<String,Object>> getGoodsListOfFormatBrand(){
        String sql = NAMESPACE+".getGoodsListOfFormatBrand";
        return sqlTemplate.selectList(sql);
    }



}
