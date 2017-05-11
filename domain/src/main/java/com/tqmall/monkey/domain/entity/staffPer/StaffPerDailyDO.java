package com.tqmall.monkey.domain.entity.staffPer;

import lombok.Data;

import java.util.Date;

@Data
public class StaffPerDailyDO {
    private Integer id;

    private Date gmtCreate;

    private Date gmtModified;

    private String isDeleted;

    private Integer staffId;

    private String timeYearMonth;

    private String timeDay;

    private Float percentOfDay;

    private Integer totalNum;

    private Integer errorNum;

    private String remarks;

    private String carType;

    private String carTypeName;
}