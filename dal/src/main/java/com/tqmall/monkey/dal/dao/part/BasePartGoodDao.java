package com.tqmall.monkey.dal.dao.part;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.part.BasePartGoodDOMapper;
import com.tqmall.monkey.domain.entity.part.BasePartGoodDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * 配件库商品基本数据
 * Created by zxg on 15/7/25.
 */
@MyBatisRepository
public class BasePartGoodDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.part.BasePartGoodDOMapper";

    @Autowired
    private BasePartGoodDOMapper basePartGoodDOMapper;

    public BasePartGoodDOMapper getBasePartGoodDOMapper() {
        return basePartGoodDOMapper;
    }




    /**
     * 根据uuId列表，批量删除该数据
     * @param uuIdList
     * @return
     */
    public Integer deleteBatchGoodsByUuId(List<String> uuIdList){
        String selectSql = NAMESPACE + ".deleteBatchGoodsByUuId";
        HashMap<String,Object> map = new HashMap<>();
        map.put("list",uuIdList);

        return sqlTemplate.selectOne(selectSql, map);
    }
}
