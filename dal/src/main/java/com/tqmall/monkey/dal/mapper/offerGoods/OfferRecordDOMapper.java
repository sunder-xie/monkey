package com.tqmall.monkey.dal.mapper.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.car.GoodsCarInfoDO;
import com.tqmall.monkey.domain.entity.offerGoods.OfferRecordDO;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OfferRecordDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferRecordDO record);

    int insertSelective(OfferRecordDO record);

    OfferRecordDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferRecordDO record);

    int updateByPrimaryKey(OfferRecordDO record);

    List<GoodsCarInfoDO> getGoodsCarInfo(Map<String, Object> params);
}