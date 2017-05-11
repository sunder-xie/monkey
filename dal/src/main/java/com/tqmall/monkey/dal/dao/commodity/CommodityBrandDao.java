package com.tqmall.monkey.dal.dao.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.commodity.CommodityBrandDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityBrandDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/8/14.
 * 商品库品牌维护管理
 */
@MyBatisRepository
public class CommodityBrandDao extends BaseDao{
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.commodity.CommodityBrandDOMapper";

    @Autowired
    private CommodityBrandDOMapper commodityBrandDOMapper;

    public CommodityBrandDOMapper getCommodityBrandDOMapper() {
        return commodityBrandDOMapper;
    }

    public Page<CommodityBrandDO> getPageByCountroyKey(Integer index,Integer pageSize,Integer country,String nameKey){
        String selectSql = NAMESPACE + ".getPageByCountroyKey";
        String countSql = NAMESPACE + ".getPageByCountroyKeyCount";

        Map<String ,Object> params = new HashMap<>();
        params.put("country",country);
        if(null != nameKey && ! "".equals(nameKey)) {
            params.put("nameCh", "%" + nameKey + "%");
        }else{
            params.put("nameCh", null);
        }

        Page<CommodityBrandDO> page = this.queryPage(selectSql, countSql, params, index, pageSize);
        if(page.getTotalNumber() == 0 && null != nameKey){
            params.put("nameCh", null);
            params.put("nameEn", "%" + nameKey + "%");
            page = this.queryPage(selectSql, countSql, params, index, pageSize);
        }

        return page;
    }

    //中文名称搜索
    public CommodityBrandDO getCommodityBrandDOByNameCh(String nameCh){
        String selectSql = NAMESPACE + ".getCommodityBrandDOByNameKey";
        Map<String ,Object> params = new HashMap<>();
        params.put("nameCh", nameCh );

        return sqlTemplate.selectOne(selectSql,params);
    }
    //英文名称搜索
    public CommodityBrandDO getCommodityBrandDOByNameEn(String nameEn){
        String selectSql = NAMESPACE + ".getCommodityBrandDOByNameKey";
        Map<String ,Object> params = new HashMap<>();
        params.put("nameEn", nameEn );

        return sqlTemplate.selectOne(selectSql,params);
    }

    //获得所有品牌列表
    public List<CommodityBrandDO> getAllCommodityBrand(){
        String selectSql = NAMESPACE + ".getAllCommodityBrand";
        return sqlTemplate.selectList(selectSql);
    }
}
