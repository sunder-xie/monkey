package com.tqmall.monkey.domain.bizBO.avid;

import lombok.Data;

import java.util.List;

/**
 * Created by huangzhangting on 16/11/1.
 */
@Data
public class ModifyAvidCallBO {
    private String operator; //操作人

    private Integer id; //急呼数据主键id

    private Integer carBrandId;

    private Integer carSeriesId;

    private Integer carModelId;

    private Integer carPowerId;

    private Integer carYearId;

    private Integer carId;

    private String carVin;

    private Integer isModifyCar;

    private Integer invoiceType;

    private String orderRemark;

    private String shopTel;

    private String fillInPerson;

    private String fillInPersonTel;

    private List<ModifyAvidCallGoodsBO> goodsParamList;
}
