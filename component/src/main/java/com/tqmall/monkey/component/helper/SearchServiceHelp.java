package com.tqmall.monkey.component.helper;

import com.alibaba.fastjson.JSONArray;


/**
 * Created by ximeng on 2015/5/7.
 * 走rich的搜索结果获得数据
 */
public interface SearchServiceHelp {
    /**
     * 根据oe码获得电商库商品Id
     * @param OE
     * @return
     */
    public Integer getGoodsIdByOe(String OE);


    /**
     * 获得电商Id
     * @param format 规格型号
     * @param brandName 品牌名称
     * @return
     */
    public Integer getGoodsIdByFormatBrand(String format,String brandName);



}
