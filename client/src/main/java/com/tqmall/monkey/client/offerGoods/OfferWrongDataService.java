package com.tqmall.monkey.client.offerGoods;

import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.offerGoods.OfferWrongDataDO;

import java.util.List;

/**
 * 出错的Service
 * Created by zxg on 15/7/8.
 */
public interface OfferWrongDataService {
    void insertOfferWrongDataWithoutExit(OfferWrongDataDO offerWrongDataDO);

    void updateOfferWrongDatStatus(Integer id,Integer status);

    Page<OfferWrongDataDO> selectWrongPageBystatus(Integer status,String account, Integer pageIndex, Integer pageSize);

    List<OfferWrongDataDO> selectWrongListBystatus(Integer status,String account);

    Integer selectWrongDataSum(Integer status,String account);

}
