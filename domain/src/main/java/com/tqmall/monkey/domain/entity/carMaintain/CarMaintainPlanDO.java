package com.tqmall.monkey.domain.entity.carMaintain;

import java.util.Date;

/*
*  车款保养方案实体类（力洋id和保养方案id对应关系表）
* */
public class CarMaintainPlanDO {
    private Integer id;

    private String l_id;

    private Integer maintainPlanId;

    private Boolean isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getL_id() {
        return l_id;
    }

    public void setL_id(String l_id) {
        this.l_id = l_id;
    }

    public Integer getMaintainPlanId() {
        return maintainPlanId;
    }

    public void setMaintainPlanId(Integer maintainPlanId) {
        this.maintainPlanId = maintainPlanId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}