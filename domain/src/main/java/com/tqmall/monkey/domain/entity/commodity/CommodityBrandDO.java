package com.tqmall.monkey.domain.entity.commodity;

import lombok.Data;

import java.util.Date;

@Data
public class CommodityBrandDO {
    private Integer id;

    private String firstLetter;

    private String nameCh;

    private String nameEn;

    private String onlineWebsite;

    private String code;

    private String profile;

    private Integer country;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private Integer isdelete;


}