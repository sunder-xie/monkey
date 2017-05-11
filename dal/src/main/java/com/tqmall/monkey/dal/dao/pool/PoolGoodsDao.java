package com.tqmall.monkey.dal.dao.pool;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.dao.base.BaseDao;
import com.tqmall.monkey.dal.mapper.pool.PoolGoodsDOMapper;
import com.tqmall.monkey.domain.entity.pool.PoolGoodsDO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Pool商品池
 * Created by zxg on 15/7/7.
 */
@MyBatisRepository
public class PoolGoodsDao extends BaseDao {
    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.pool.PoolGoodsDOMapper";

    @Autowired
    private PoolGoodsDOMapper poolGoodsDOMapper;

    public PoolGoodsDOMapper getPoolGoodsDOMapper() {
        return poolGoodsDOMapper;
    }

    public PoolGoodsDO selectByOeNumber(String OENumber){
        return sqlTemplate.selectOne(NAMESPACE + ".selectByOeNumber", OENumber);
    }
}
