package com.tqmall.monkey.dal.mapper.offerGoods;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.offerGoods.OfferWrongDataDO;

@MyBatisRepository
public interface OfferWrongDataDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferWrongDataDO record);

    int insertSelective(OfferWrongDataDO record);

    OfferWrongDataDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferWrongDataDO record);

    int updateByPrimaryKey(OfferWrongDataDO record);

    OfferWrongDataDO selectByOfferWrongDataDO(OfferWrongDataDO offerWrongDataDO);
}