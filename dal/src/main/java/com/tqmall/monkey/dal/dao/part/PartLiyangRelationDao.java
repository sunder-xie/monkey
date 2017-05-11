package com.tqmall.monkey.dal.dao.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.part.PartLiyangRelationDOMapper;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.part.PartLiyangRelationDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 存力洋id和goods和pic的关系
 * Created by zxg on 15/7/27.
 */

@MyBatisRepository
public class PartLiyangRelationDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.part.PartLiyangRelationDOMapper";

    @Autowired
    private PartLiyangRelationDOMapper partLiyangRelationDOMapper;

    public PartLiyangRelationDOMapper getPartLiyangRelationDOMapper() {
        return partLiyangRelationDOMapper;
    }

    //判断是否存在
    public boolean exitPartRelation(PartLiyangRelationDO partLiyangRelationDO) {
        return sqlTemplate.selectOne(NAMESPACE+".exitPartRelation",partLiyangRelationDO);
    }



    /**
     * 根据配件id和图片id获得力洋id（分页）
     * @param index 查询的页面
     * @param pageSize 当页显示的数据数目
     * @param goodsId 配件id
     * @param picId
     * @return
     */
    public Page<String> selectLiyangIdPageByGoodsIdPicId(Integer index,Integer pageSize,String goodsId,String picId,Integer partLId,String table){
        HashMap<String,Object> params = new HashMap<>();
        params.put("partLId",partLId);
        params.put("goodsId",goodsId);
        params.put("picId",picId);
        params.put("liyangTable",table);

        String selectSql = NAMESPACE + ".selectLiyangIdListByGoodsIdPicId";
        String countSql = NAMESPACE + ".selectLiyangIdListByGoodsIdPicIdCount";


        return this.queryPage(selectSql, countSql, params, index, pageSize);
    }



}
