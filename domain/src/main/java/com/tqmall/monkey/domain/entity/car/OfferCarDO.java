package com.tqmall.monkey.domain.entity.car;

import java.util.Date;

public class OfferCarDO {
    public static final Integer 新建_STATUS = 0;
    public static final Integer 成功_STATUS = 1;
    public static final Integer 失败_未导出EXCLE_STATUS = 2;
    public static final Integer 导入pool_STATUS = 3;
    public static final Integer 失败_已导出EXCLE_STATUS = 3;


    private Integer id;

    private String brandName;

    private String offerCarName;

    private String company;

    private Integer createId;

    private Integer updateId;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer onlineCarId;

    private Integer onlineBrandId;

    private String startYear;

    private String endYear;

    private String displacement;

    private Integer status;

    private Integer wrong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getOfferCarName() {
        return offerCarName;
    }

    public void setOfferCarName(String offerCarName) {
        this.offerCarName = offerCarName == null ? null : offerCarName.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
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

    public Integer getOnlineBrandId() {
        return onlineBrandId;
    }

    public void setOnlineBrandId(Integer onlineBrandId) {
        this.onlineBrandId = onlineBrandId;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear == null ? null : startYear.trim();
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear == null ? null : endYear.trim();
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

    public Integer getWrong() {
        return wrong;
    }

    public void setWrong(Integer wrong) {
        this.wrong = wrong;
    }
}