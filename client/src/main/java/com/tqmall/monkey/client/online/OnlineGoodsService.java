package com.tqmall.monkey.client.online;

import com.google.common.collect.Multimap;


/**
 * Created by zxg on 15/9/11.
 */
public interface OnlineGoodsService {

    Multimap<String, Integer> getGoodsListOfFormatBrand();
}
