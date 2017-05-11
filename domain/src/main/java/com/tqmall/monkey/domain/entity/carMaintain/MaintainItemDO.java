package com.tqmall.monkey.domain.entity.carMaintain;

import lombok.Data;

import java.util.Date;

/*
*  保养项目实体类
* */
@Data
public class MaintainItemDO {
    private Integer id;

    private String name;

    private String unit;

    private String type;

    private Boolean isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer sort;

}