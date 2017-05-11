package com.tqmall.monkey.dal.dao.pool;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.mapper.pool.PoolGoodsCarDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 15/5/29.
 */

@MyBatisRepository
public class PoolGoodsCarDao {
    @Autowired
    private PoolGoodsCarDOMapper mapper;

    public PoolGoodsCarDOMapper getMapper() {
        return mapper;
    }
}
