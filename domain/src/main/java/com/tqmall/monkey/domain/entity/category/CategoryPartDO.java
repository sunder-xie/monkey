package com.tqmall.monkey.domain.entity.category;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryPartDO {
    private Integer id;

    private Date gmtCreate;

    private Date gmtModified;

    private String isDeleted;

    private String partName;

    private String partCode;

    private Integer firstCatId;

    private String firstCatName;

    private Integer secondCatId;

    private String secondCatName;

    private Integer thirdCatId;

    private String thirdCatName;

    private String sumCode;

    private String alissNameText;

    private String labelNameText;

    private Integer catKind;

    private String vehicleCode;

}