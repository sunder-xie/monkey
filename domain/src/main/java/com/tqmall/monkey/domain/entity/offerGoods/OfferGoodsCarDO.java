package com.tqmall.monkey.domain.entity.offerGoods;

import lombok.Data;

import java.util.Date;

@Data
public class OfferGoodsCarDO {
    private Integer id;

    private Integer offerGoodsId;

    private String offerCarName;

    private Integer createId;

    private Integer updateId;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer onlineCarId;

    private Integer onlineCarParentId;

    private Date startYear;

    private Date endYear;

    private String displacement;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOfferGoodsId() {
        return offerGoodsId;
    }

    public void setOfferGoodsId(Integer offerGoodsId) {
        this.offerGoodsId = offerGoodsId;
    }

    public String getOfferCarName() {
        return offerCarName;
    }

    public void setOfferCarName(String offerCarName) {
        this.offerCarName = offerCarName == null ? null : offerCarName.trim();
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

    public Integer getOnlineCarId() {
        return onlineCarId;
    }

    public void setOnlineCarId(Integer onlineCarId) {
        this.onlineCarId = onlineCarId;
    }

    public Integer getOnlineCarParentId() {
        return onlineCarParentId;
    }

    public void setOnlineCarParentId(Integer onlineCarParentId) {
        this.onlineCarParentId = onlineCarParentId;
    }

    public Date getStartYear() {
        return startYear;
    }

    public void setStartYear(Date startYear) {
        this.startYear = startYear;
    }

    public Date getEndYear() {
        return endYear;
    }

    public void setEndYear(Date endYear) {
        this.endYear = endYear;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement == null ? null : displacement.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}