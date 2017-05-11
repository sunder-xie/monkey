package com.tqmall.monkey.domain.entity.car;

import lombok.Data;

/**
 * Created by huangzhangting on 15/6/2.
 */
@Data
public class GoodsCarInfoDO {
    private Integer id;

    private String recordName;

    private String providerName;

    private String oeNum;

    private Integer carId;

    private Integer goodsQualityType;
}
