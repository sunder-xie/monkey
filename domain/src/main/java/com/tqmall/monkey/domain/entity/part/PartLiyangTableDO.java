package com.tqmall.monkey.domain.entity.part;

import lombok.Data;

import java.util.Date;

@Data
public class PartLiyangTableDO {
    private Integer id;

    private Integer creator;

    private Integer modifier;

    private Date gmtModified;

    private Date gmtCreate;

    private String carBrandName;

    private String liyangTable;
}