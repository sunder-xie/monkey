package com.tqmall.monkey.domain.entity.online;

import lombok.Data;

import java.util.Date;

@Data
public class OnlineGoodsCarDO {

    private Integer goodsId;

    private Integer carId;

    private String carName;

    private Integer level;

    private Integer carBrandId = 0;

    private String carBrand = "";

    private Integer carSeriesId = 0;

    private String carSeries = "";

    private Integer carModelId = 0;

    private String carModel = "";

    private Integer carPowerId = 0;

    private String carPower = "";

    private Integer carYearId = 0;

    private String carYear = "";

    private Integer status = 1;

}