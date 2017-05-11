package com.tqmall.monkey.client.common;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.domain.bizBO.goods.GoodsQualityBO;

import java.util.List;
import java.util.Set;

/**
 * Created by huangzhangting on 16/10/28.
 */
public interface CommonService {
    /**
     * 获取商品品质
     * @return
     */
    Result<List<GoodsQualityBO>> getGoodsQuality();

    /**
     * 根据关键词搜索电商三级类目名称
     * @param keyword
     * @return
     */
    Result<Set<String>> searchCateName(String keyword);

}
