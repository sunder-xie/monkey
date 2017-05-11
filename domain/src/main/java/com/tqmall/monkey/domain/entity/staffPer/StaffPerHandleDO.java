package com.tqmall.monkey.domain.entity.staffPer;

import lombok.Data;

import java.util.Date;

@Data
public class StaffPerHandleDO {
    private Integer id;

    private Date gmtCreate;

    private Date gmtModified;

    private String isDeleted;

    private Integer staffId;

    private String timeYearMonth;

    private String startDay;

    private Float actualDays;

    private Integer totalNum;

    private Integer errorNum;

    private String remarks;

}