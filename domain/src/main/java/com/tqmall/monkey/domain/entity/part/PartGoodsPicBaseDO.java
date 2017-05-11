package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

/**
 * 关系表筛选出基础信息类
 * Created by zxg on 15/7/28.
 */
@Data
public class PartGoodsPicBaseDO {

    private Integer indexs;

    private String pictureNum = "";

    private String pictureIndex = "";

    private String oeNumber = "";

    private String partName = "";

    private String partCode = "";

    private String remarks = "";

    private Integer applicationAmount;

    //冗余关系表中的两个id
    private String goodsId;
    private String picId;

    //三级id和名称
    private String thirdCateCode;

    private String thirdCateName;

    private String secondCateCode;

    private String secondCateName;

    private String firstCateCode;

    private String firstCateName;

    //part的vehicleCode
    private String vehicleCode;

}
