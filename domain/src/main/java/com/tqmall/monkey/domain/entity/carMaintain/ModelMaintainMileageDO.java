package com.tqmall.monkey.domain.entity.carMaintain;

import java.util.Date;

public class ModelMaintainMileageDO {
    private Integer id;

    private Integer maintainPlanId;

    private Integer mileage;

    private Boolean isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private Boolean itemFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaintainPlanId() {
        return maintainPlanId;
    }

    public void setMaintainPlanId(Integer maintainPlanId) {
        this.maintainPlanId = maintainPlanId;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
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

    public Boolean getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(Boolean itemFlag) {
        this.itemFlag = itemFlag;
    }
}