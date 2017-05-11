package com.tqmall.monkey.domain.entity.commodity;

import lombok.Data;

import java.util.Date;

@Data

public class CommodityGoodsCarDO {
    public static final Integer NEW_STATUS = 0;
    public static final Integer SUCCESS_STATUS = 1;
    public static final Integer FAIL_STATUS = 2;


    private Integer id;

    private String goodsUuId  = "";

    private String liyangId  = "";

    private Integer status = 0;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private Integer isdelete = 0;


}