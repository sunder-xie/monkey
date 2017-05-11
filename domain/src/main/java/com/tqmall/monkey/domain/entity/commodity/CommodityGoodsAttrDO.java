package com.tqmall.monkey.domain.entity.commodity;

import lombok.Data;

import java.util.Date;

@Data
public class CommodityGoodsAttrDO {
    private Integer id;

    private String goodsUuId = "";

    private Integer attrId;

    private String attrName = "";

    private String attrValue = "";

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private Integer isdelete = 0;

}