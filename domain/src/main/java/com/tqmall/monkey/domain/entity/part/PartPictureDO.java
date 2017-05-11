package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

import java.util.Date;

@Data
public class PartPictureDO {
    private Integer id;

    private String uuId;

    private String pictureNum = "";

    private String pictureIndex = "";

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

}