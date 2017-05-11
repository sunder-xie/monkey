package com.tqmall.monkey.domain.bizBO.lop;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/7/30.
 */
@Data
public class WishListRequestBO{

    private String vin;

    private Integer isDeckVehicle;

    private Integer isModifiedVehicle;

    private Integer isReceiptPrice;

    private Integer brand;

    private Integer series;

    private Integer model;

    private Integer engine;

    private Integer year;

    private Integer tqCarModel;

    //private String companyName;

    private String telephone;

    private String wishListMemo;

    private String wishListMaker;

    private String wishListMakerTel;
    /**
     * 客户端token
     */
    private String token;

    private List<WishListGoodsBO> goodsList;

    //急呼数据主键id
    private Integer avidCallId;
    //操作人
    private String operator;

}
