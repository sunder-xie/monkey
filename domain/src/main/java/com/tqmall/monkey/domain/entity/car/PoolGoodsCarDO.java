package com.tqmall.monkey.domain.entity.car;

import java.util.Date;

public class PoolGoodsCarDO {

    private Integer id;

    private Integer carId;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer carParentId = 0;

    private String goodsOeNum = "";

    private Integer carBrandId = 0;

    private String carBrand = "";

    private Integer carSeriesId = 0;

    private String carSeries = "";

    private Integer carPowerId = 0;

    private String carPower = "";

    private Integer carYearId = 0;

    private String carYear = "";

    private String afterOe = "";

    private Integer source = 0;//默认报价单

    private Integer isdelete = 0;

    private Integer localCarId;//临时数据，不存数据库

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
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

    public Integer getCarParentId() {
        return carParentId;
    }

    public void setCarParentId(Integer carParentId) {
        this.carParentId = carParentId;
    }

    public String getGoodsOeNum() {
        return goodsOeNum;
    }

    public void setGoodsOeNum(String goodsOeNum) {
        this.goodsOeNum = goodsOeNum == null ? null : goodsOeNum.trim();
    }

    public Integer getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(Integer carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand == null ? null : carBrand.trim();
    }

    public Integer getCarSeriesId() {
        return carSeriesId;
    }

    public void setCarSeriesId(Integer carSeriesId) {
        this.carSeriesId = carSeriesId;
    }

    public String getCarSeries() {
        return carSeries;
    }

    public void setCarSeries(String carSeries) {
        this.carSeries = carSeries == null ? null : carSeries.trim();
    }

    public Integer getCarPowerId() {
        return carPowerId;
    }

    public void setCarPowerId(Integer carPowerId) {
        this.carPowerId = carPowerId;
    }

    public String getCarPower() {
        return carPower;
    }

    public void setCarPower(String carPower) {
        this.carPower = carPower == null ? null : carPower.trim();
    }

    public Integer getCarYearId() {
        return carYearId;
    }

    public void setCarYearId(Integer carYearId) {
        this.carYearId = carYearId;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear == null ? null : carYear.trim();
    }

    public String getAfterOe() {
        return afterOe;
    }

    public void setAfterOe(String afterOe) {
        this.afterOe = afterOe == null ? null : afterOe.trim();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public Integer getLocalCarId() {
        return localCarId;
    }

    public void setLocalCarId(Integer localCarId) {
        this.localCarId = localCarId;
    }
}