package com.tqmall.monkey.client.pool;

import com.tqmall.monkey.domain.entity.pool.PoolGoodsDO;

/**
 * Created by zxg on 15/7/7.
 */
public interface PoolGoodsService {
    PoolGoodsDO findByOE(String oe);

    //选择性插入pool，返回自增主键
    Integer insertPoolGoodsDO(PoolGoodsDO poolGoodsDO);
}
