package com.tqmall.monkey.client.common;

import com.tqmall.monkey.domain.entity.common.GoodsAttrKeyDO;

/**
 * Created by zxg on 15/8/21.
 */
public interface GoodsAttrKeyService {

    //返回自增id，若存在，则返回实际id
    Integer insertGoodsAttr(GoodsAttrKeyDO goodsAttrKeyDO);

    GoodsAttrKeyDO getGoodsAttrKeyDOByAttrName(String attrName);
}
