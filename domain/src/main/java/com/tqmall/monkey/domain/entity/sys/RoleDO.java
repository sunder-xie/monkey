package com.tqmall.monkey.domain.entity.sys;

import lombok.Data;

import java.util.Date;

/**
 * 角色
 */
public class RoleDO {
    private Integer id;

    private String roleName;

    private String description;

    private Integer available;

    private Integer createId;

    private Integer updateId;

    private Date gmtCreate;

    private Date gmtModified;

    //用户授权页面使用
    private Boolean checkFlag;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
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

    public Boolean getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(Boolean checkFlag) {
        this.checkFlag = checkFlag;
    }
}