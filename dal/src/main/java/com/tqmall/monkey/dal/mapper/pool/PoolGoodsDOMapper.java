package com.tqmall.monkey.dal.mapper.pool;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.pool.PoolGoodsDO;

@MyBatisRepository
public interface PoolGoodsDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PoolGoodsDO record);

    int insertSelective(PoolGoodsDO record);

    PoolGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PoolGoodsDO record);

    int updateByPrimaryKey(PoolGoodsDO record);
}