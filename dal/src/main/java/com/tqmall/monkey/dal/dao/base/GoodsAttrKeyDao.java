package com.tqmall.monkey.dal.dao.base;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.mapper.common.GoodsAttrKeyDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zxg on 15/8/21.
 */
@MyBatisRepository
public class GoodsAttrKeyDao {

    private final static String NAMESPACE = "com.tqmall.monkey.dal.mapper.common.GoodsAttrKeyDOMapper";

    @Autowired
    private GoodsAttrKeyDOMapper goodsAttrKeyDOMapper;

    public GoodsAttrKeyDOMapper getGoodsAttrKeyDOMapper() {
        return goodsAttrKeyDOMapper;
    }
}
