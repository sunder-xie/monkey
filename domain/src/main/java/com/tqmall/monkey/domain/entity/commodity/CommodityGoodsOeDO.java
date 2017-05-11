package com.tqmall.monkey.domain.entity.commodity;

import lombok.Data;

import java.util.Date;

@Data

public class CommodityGoodsOeDO {
    private Integer id;

    private String goodsUuId  = "";

    private String oeNumber  = "";

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private Integer isdelete;

}