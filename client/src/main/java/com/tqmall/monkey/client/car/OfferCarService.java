package com.tqmall.monkey.client.car;


import com.tqmall.monkey.domain.entity.car.OfferCarDO;

import java.util.List;

/**
 * Created by .
 */
public interface OfferCarService {
    //判断存在不存在，不存在则插入，存在取出id
    Integer insertWithOutExit(OfferCarDO offerCarDO);

    //根据车的匹配状态获得车数据
    public List<OfferCarDO> findAllCarsSumByStatus(Integer status);

    Integer getAllCarsSumByStatus(Integer status);


    public List<OfferCarDO> findCarsByBrandName(String brandName, Integer status);

    public void modifyCar(OfferCarDO offerCarDO);

    public OfferCarDO findCarById(Integer carId);

    List<String> findAllCarBrand(Integer status);

    List<String> findCarYearsById(Integer carId);



    //====================车型和商品的关系==============
    void insertCarAndGoodsWithOutExit(Integer car_id,Integer goods_id);
}
