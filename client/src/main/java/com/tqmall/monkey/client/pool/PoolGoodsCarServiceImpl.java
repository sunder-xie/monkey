package com.tqmall.monkey.client.pool;

import com.tqmall.monkey.dal.dao.pool.PoolGoodsCarDao;
import com.tqmall.monkey.domain.entity.car.PoolGoodsCarDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangzhangting on 15/5/29.
 */
@Service
public class PoolGoodsCarServiceImpl implements PoolGoodsCarService{
    @Autowired
    private PoolGoodsCarDao poolGoodsCarDao;

    @Override
    public void insertPoolGoodsCar(PoolGoodsCarDO obj) {
        poolGoodsCarDao.getMapper().insertSelective(obj);
    }

    @Override
    public List<PoolGoodsCarDO> findPoolGoodsCarList(PoolGoodsCarDO obj) {
        return poolGoodsCarDao.getMapper().getPoolGoodsCarList(obj);
    }

    @Override
    public void insertBashPoolCar(List<PoolGoodsCarDO> carDOList) {
         poolGoodsCarDao.getMapper().insertBashPoolCar(carDOList);
    }
}
