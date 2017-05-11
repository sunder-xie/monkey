package com.tqmall.monkey.domain.entity.commodity;

import lombok.Data;

import java.util.Date;

@Data

public class CommodityGoodsDO {
    //销售状态
    public static final Integer NEW_SALE_STATUS = 0;
    public static final Integer ONLINE_SALE_STATUS = 1;
    public static final Integer STOP_SALE_STATUS = 2;

    private Integer id;

    private String uuId  = "";

    private String goodsCode  = "";

    private Integer goodsQualityType;

    private Integer brandId;

    private String brandName  = "";

    private String goodsName  = "";

    private String goodsFormat  = "";

    private Integer cateKind;

    private String guaranteeTime  = "";

    private String saleUnit  = "";

    private Integer saleStatus;

    private Integer partId;

    private String partName;

    private String partSumCode  = "";

    private String remark = "";

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private Integer isdelete = 0;

    public void setNone(){
        this.uuId = null;
        this.goodsCode = null;
        this.brandName = null;
        this.goodsName = null;
        this.goodsFormat = null;
        this.guaranteeTime = null;
        this.saleUnit = null;
        this.partSumCode = null;
        this.remark = null;
        this.isdelete = null;
    }
}