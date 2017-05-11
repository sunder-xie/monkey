package com.tqmall.monkey.domain.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhongxi.
 * db_model_goods_relation 全车件
 */

@Data
public class MaintainGoodsDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer creator;

    private Integer modifier;

    private Integer modelId;

    private Integer goodId;

    private Integer modelParentId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
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

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public Integer getModelParentId() {
        return modelParentId;
    }

    public void setModelParentId(Integer modelParentId) {
        this.modelParentId = modelParentId;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

}
