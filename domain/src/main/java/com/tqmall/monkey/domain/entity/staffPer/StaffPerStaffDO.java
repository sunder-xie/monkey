package com.tqmall.monkey.domain.entity.staffPer;

import lombok.Data;

import java.util.Date;

@Data
public class StaffPerStaffDO {
    private Integer id;

    private Date gmtCreate;

    private Date gmtModified;

    private String isDeleted;

    private String staffName;

    private String staffNo;
}