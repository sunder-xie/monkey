package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

import java.util.Date;

@Data
public class PartSubjoinDO {
    private Integer id;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private String uuid;

    private String remarks;

    private Integer applicationAmount;

}