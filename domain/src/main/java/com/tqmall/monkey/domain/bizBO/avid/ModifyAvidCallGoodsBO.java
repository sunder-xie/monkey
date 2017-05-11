package com.tqmall.monkey.domain.bizBO.avid;

import lombok.Data;

/**
 * Created by huangzhangting on 16/11/1.
 */
@Data
public class ModifyAvidCallGoodsBO {
    private Integer id;

    private String goodsName;

    private String goodsOe;

    private Integer goodsNumber;

    private String goodsUnit;

    private Integer goodsQualityId; //首选品质

    private Integer backQualityId; //备选品质

    private String goodsRemark;
}
