package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

import java.util.Date;

@Data
public class PartLiyangDO {
    private Integer id;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private String isDeleted;

    private String liyangBrand;

    private String liyangFactory;

    private String liyangSeries;

    private String liyangModel;
}