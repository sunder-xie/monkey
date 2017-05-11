package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

import java.util.Date;

@Data
public class BasePartGoodDO {
    private Integer id;

    private String uuId;

    private String oeNumber = "";

    //原始oe
    private String oldOeNumber = "";

    private String partName = "";

    private String partCode = "";

    private Integer firstCateId;

    private String firstCateCode = "";

    private Integer secondCateId;

    private String secondCateCode = "";

    private Integer thirdCateId;

    private String thirdCateCode = "";

    //替换历史（老的oe）：by 中西 2016-01-07
    private String replaceHistory;

    //原厂零件名称：by 中西 2016-05-23
    private String factoryName;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    public void setNone(){
        this.oeNumber = null;
        this.oldOeNumber = null;
        this.partName = null;
        this.partCode = null;
        this.firstCateCode = null;
        this.secondCateCode = null;
        this.thirdCateCode = null;
    }

}