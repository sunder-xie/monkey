package com.tqmall.monkey.dal.dao.commodity;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsCarDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.commodity.CommodityGoodsCarDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 15/8/22.
 */
@MyBatisRepository
public class CommodityGoodsCarDao extends BaseDao{

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.commodity.CommodityGoodsCarDOMapper";

    @Autowired
    private CommodityGoodsCarDOMapper commodityGoodsCarDOMapper;

    public CommodityGoodsCarDOMapper getCommodityGoodsCarDOMapper() {
        return commodityGoodsCarDOMapper;
    }

    //批量删除
    public void deleteBatchGoodsUuId(List<String> goodsUuIdList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",goodsUuIdList);
        map.put("updateUserId",updateUserId);
        map.put("isdelete",1);

        sqlTemplate.update(NAMESPACE + ".updateBatchGoodsUuId", map);
    }

    //批量更新status状态---goods uuId
    public void updateStatusBatchGoodsUuId(Integer status,List<String> goodsUuIdList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",goodsUuIdList);
        map.put("updateUserId",updateUserId);
        map.put("status",status);

        sqlTemplate.update(NAMESPACE + ".updateBatchGoodsUuId", map);
    }

    //批量更新status状态--liyang code
    public void updateStatusBatchLiyang(Integer status,List<String> liyangList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",liyangList);
        map.put("updateUserId",updateUserId);
        map.put("status",status);

        sqlTemplate.update(NAMESPACE + ".updateBatchLiyang", map);
    }
    //批量更新isdelete状态--id
    public void updateBatchIds(Integer isdelete,List<Integer> idList,Integer updateUserId){
        Map<String,Object> map = new HashMap<>();
        map.put("list",idList);
        map.put("updateUserId",updateUserId);
        map.put("isdelete",isdelete);

        sqlTemplate.update(NAMESPACE + ".updateBatchIds", map);
    }




    public Page<CommodityGoodsCarDO> getPageByGoodsUuId(String goodsUuId,String searchLiyang, Integer index, Integer pageSize) {
        String selectSql = NAMESPACE+".selectCarByUuId";
        String countSql = NAMESPACE+".selectCarByUuIdCount";
        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsUuId", goodsUuId);
        if(null != searchLiyang && !"".equals(searchLiyang.trim())){
            map.put("liyangId", "%"+searchLiyang.trim()+"%");

        }
        return this.queryPage(selectSql,countSql,map,index,pageSize);
    }

    public List<CommodityGoodsCarDO> getListByGoodsUuIdStatusWithDelete(String goodsUuId,Integer status,Integer isdelete){
        String selectSql = NAMESPACE+".getListByGoodsUuIdStatusWithDelete";

        HashMap<String,Object> map = new HashMap<>();
        map.put("goodsUuId", goodsUuId);
        map.put("isdelete", isdelete);
        map.put("status", status);
        return sqlTemplate.selectList(selectSql, map);
    }

    //获得已有的力洋Id--状态
    public List<String> getLiYangListGroupBy(List<String> uuIdList,Integer status) {
//        if(status == null){
//            status = CommodityGoodsCarDO.NEW_STATUS;
//        }

        HashMap<String,Object> map = new HashMap<>();
        map.put("uuIdList",uuIdList);
        map.put("status",status);

        return sqlTemplate.selectList(NAMESPACE+".selectAllLiyangByStatus",map);
    }

    /**
     * 根据商品库商品的品牌id+标准零件名称获得其对应的力洋编号
     * @param brandId
     * @param partId
     * @return
     */
    public List<CommodityGoodsCarDO> getListByGBrandGPart(Integer brandId, Integer partId) {
        String selectSql = NAMESPACE+".selectGoodsByGBrandGPart";
        HashMap<String,Object> map = new HashMap<>();
        map.put("brandId",brandId);
        map.put("partId",partId);

        return sqlTemplate.selectList(selectSql,map);
    }
}
