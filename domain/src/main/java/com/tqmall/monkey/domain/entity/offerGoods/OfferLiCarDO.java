package com.tqmall.monkey.domain.entity.offerGoods;

import lombok.Data;

import java.util.Date;

@Data
public class OfferLiCarDO {
    //关系的状态
    public final static Integer status_新建 = 0;
    public final static Integer status_匹配成功 = 1;
    public final static Integer status_关系导入pool = 2;
    public final static Integer status_匹配失败未导出 = 3;
    public final static Integer status_匹配失败已导出 = 4;

    private Integer id;

    private Integer offerGoodsId;

    private String liId;

    private Integer onlineCarId;

    private Integer onlinePid;

    private String onlineBrand;

    private String onlineCompany;

    private String onlineSeries;

    private String onlinePower;

    private Integer createId;

    private Integer updateId;

    private Integer status;

    private String wrongReason;

    private Date gmtCreate;

    private Date gmtModified;


}