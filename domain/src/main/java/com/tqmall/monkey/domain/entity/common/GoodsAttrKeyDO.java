package com.tqmall.monkey.domain.entity.common;

import lombok.Data;

import java.util.Date;

@Data
public class GoodsAttrKeyDO {
    private Integer id;

    private String attrName;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private Integer isdelete;

}