package com.tqmall.monkey.client.offerGoods;

import com.tqmall.monkey.dal.dao.offerGoods.OfferRecordDao;
import com.tqmall.monkey.domain.entity.car.GoodsCarInfoDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huangzhangting on 15/6/2.
 */
@Service
public class OfferRecordServiceImpl implements OfferRecordService{
    @Autowired
    private OfferRecordDao offerRecordDao;

    @Override
    public Integer insertRecord(OfferRecordDO recordDO) {
        offerRecordDao.getOfferRecordDOMapper().insertSelective(recordDO);
        return recordDO.getId();
    }

    @Override
    public Integer updateRecord(OfferRecordDO recordDO) {
        offerRecordDao.getOfferRecordDOMapper().updateByPrimaryKeySelective(recordDO);
        return recordDO.getId();
    }

    @Override
    public List<GoodsCarInfoDO> findGoodsCarInfo(Integer status) {
        Map<String, Object> params = new HashMap<>();
        if(status!=null){
            params.put("status", status);
        }
        return offerRecordDao.getOfferRecordDOMapper().getGoodsCarInfo(params);
    }

    @Override
    public void modifyOfferRecord(OfferRecordDO record) {
        offerRecordDao.getOfferRecordDOMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public OfferRecordDO findByGoodsIdRecordNameProviderNameStatus(Integer goodsId, String recordName, String providerName, Integer status) {
        return offerRecordDao.selectByGoodsIdRecordNameProviderNameStatus(goodsId,recordName,providerName,status);
    }

    @Override
    public List<OfferRecordDO> findRecordListByGoodsIdAndStatus(Integer goodsId, Integer status,String provider_name) {
        return offerRecordDao.findRecordListByGoodsIdAndStatus(goodsId, status,provider_name);
    }

    @Override
    public List<String> findProviderNameByStatus(Integer status) {
        return offerRecordDao.findProviderNameByStatus(status);
    }

    @Override
    public Integer getRecordSum(Integer status) {
        return offerRecordDao.getRecordSum(status);
    }
}
