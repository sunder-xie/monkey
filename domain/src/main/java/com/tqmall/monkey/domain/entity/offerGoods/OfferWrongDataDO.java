package com.tqmall.monkey.domain.entity.offerGoods;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OfferWrongDataDO {
    //未导出和已导出
    public final  static Integer status_new = 0;
    public final  static Integer status_已导出 = 1;

    private Integer id;

    private String indexs;

    private String recordName;

    private String providerName;

    private String goodsName;

    private String goodsAttribute;

    private String oeNumber;

    private String primaryPrice;

    private String advicePrice;

    private String partName;

    private String partCode;

    private String brand;

    private String company;

    private String series;

    private String model;

    private String displacement;

    private String year;

    private String measureUnit;

    private String packageFormat;

    private String remark;

    private String goodsFormat;

    private String quickWearLabel;

    private Integer status;

    private String failReason;

    private String createAccount;

    private Date gmtCreate;

    private Date gmtModified;

    private String city;

    //界面使用
    private String[] failReasonArray;


}