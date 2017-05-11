package com.tqmall.monkey.client.pool;


import com.tqmall.monkey.domain.entity.car.PoolGoodsCarDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/5/29.
 */
public interface PoolGoodsCarService {
    public void insertPoolGoodsCar(PoolGoodsCarDO obj);

    public List<PoolGoodsCarDO> findPoolGoodsCarList(PoolGoodsCarDO obj);

    public void insertBashPoolCar(List<PoolGoodsCarDO> carDOList);
}
