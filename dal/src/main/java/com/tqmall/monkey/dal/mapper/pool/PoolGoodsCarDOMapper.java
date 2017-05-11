package com.tqmall.monkey.dal.mapper.pool;


import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.car.PoolGoodsCarDO;

import java.util.List;

@MyBatisRepository
public interface PoolGoodsCarDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PoolGoodsCarDO record);

    int insertSelective(PoolGoodsCarDO record);

    PoolGoodsCarDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PoolGoodsCarDO record);

    int updateByPrimaryKey(PoolGoodsCarDO record);

    List<PoolGoodsCarDO> getPoolGoodsCarList(PoolGoodsCarDO record);

    int insertBashPoolCar(List<PoolGoodsCarDO> list);
}