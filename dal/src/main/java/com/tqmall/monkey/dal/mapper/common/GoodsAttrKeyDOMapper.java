package com.tqmall.monkey.dal.mapper.common;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.common.GoodsAttrKeyDO;

//商品属性
@MyBatisRepository
public interface GoodsAttrKeyDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAttrKeyDO record);

    int insertSelective(GoodsAttrKeyDO record);

    GoodsAttrKeyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAttrKeyDO record);

    int updateByPrimaryKey(GoodsAttrKeyDO record);

    GoodsAttrKeyDO selectByAttrName(String attrName);
}