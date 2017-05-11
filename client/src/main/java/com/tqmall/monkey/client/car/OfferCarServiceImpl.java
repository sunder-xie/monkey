package com.tqmall.monkey.client.car;

import com.tqmall.monkey.dal.dao.car.OfferCarDao;
import com.tqmall.monkey.domain.entity.car.OfferCarDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/5/21.
 */
@Service
public class OfferCarServiceImpl implements OfferCarService{
    @Autowired
    private OfferCarDao offerCarDao;

    @Override
    public Integer insertWithOutExit(OfferCarDO offerCarDO) {
        OfferCarDO exit = offerCarDao.getOfferCarDOMapper().selectByOfferCarDO(offerCarDO);
        if(exit == null){
            offerCarDao.getOfferCarDOMapper().insertSelective(offerCarDO);
            return offerCarDO.getId();
        }
        return exit.getId();
    }

    @Override
    public List<OfferCarDO> findAllCarsSumByStatus(Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(status!=null) {
            params.put("status", status);
        }
        return offerCarDao.getOfferCarDOMapper().getAllCarsByStatus(params);
    }

    @Override
    public Integer getAllCarsSumByStatus(Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(status!=null) {
            params.put("status", status);
        }
        return offerCarDao.getOfferCarDOMapper().getAllCarsSumByStatus(params);
    }

    @Override
    public List<OfferCarDO> findCarsByBrandName(String brandName, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("brandName", brandName);
        if(status!=null) {
            params.put("status", status);
        }

        return offerCarDao.getOfferCarDOMapper().getCarsByBrandName(params);
    }

    @Override
    public void modifyCar(OfferCarDO offerCarDO) {
        offerCarDao.getOfferCarDOMapper().updateByPrimaryKeySelective(offerCarDO);
    }

    @Override
    public OfferCarDO findCarById(Integer carId) {
        return offerCarDao.getOfferCarDOMapper().selectByPrimaryKey(carId);
    }

    @Override
    public List<String> findAllCarBrand(Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(status!=null) {
            params.put("status", status);
        }
        return offerCarDao.getOfferCarDOMapper().getAllCarBrand(params);
    }

    @Override
    public List<String> findCarYearsById(Integer carId) {
        return offerCarDao.getOfferCarDOMapper().getCarYearsById(carId);
    }

    //====================车型和商品的关系==============
    @Override
    public void insertCarAndGoodsWithOutExit(Integer car_id, Integer goods_id) {
        Boolean is_exit = offerCarDao.ExitCarAndGoodsRelation(car_id,goods_id);
        if(!is_exit) {
            offerCarDao.insertCarAndGoods(car_id, goods_id);
        }
    }
}
