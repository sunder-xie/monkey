package com.tqmall.monkey.domain.entity.pool;

import lombok.Data;

import java.util.Date;

@Data
public class PoolGoodsDO {
    private Integer id;

    private String oeNum;

    private String goodsName;

    private Integer brandId;

    private String brandName;

    private Byte carPartsType;

    private Integer partId;

    private Date gmtCreate;

    private Date gmtModified;

    private String partName;

    private String partSumCode;

    private String afterOe;

    private Integer source;

    private Integer thirdCateId;

    private String thirdCateName;

    private Integer secondCateId;

    private String secondCateName;

    private Integer firstCateId;

    private String firstCateName;

    private Integer isdelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOeNum() {
        return oeNum;
    }

    public void setOeNum(String oeNum) {
        this.oeNum = oeNum == null ? null : oeNum.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public Byte getCarPartsType() {
        return carPartsType;
    }

    public void setCarPartsType(Byte carPartsType) {
        this.carPartsType = carPartsType;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
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

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName == null ? null : partName.trim();
    }

    public String getPartSumCode() {
        return partSumCode;
    }

    public void setPartSumCode(String partSumCode) {
        this.partSumCode = partSumCode == null ? null : partSumCode.trim();
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

    public Integer getThirdCateId() {
        return thirdCateId;
    }

    public void setThirdCateId(Integer thirdCateId) {
        this.thirdCateId = thirdCateId;
    }

    public String getThirdCateName() {
        return thirdCateName;
    }

    public void setThirdCateName(String thirdCateName) {
        this.thirdCateName = thirdCateName == null ? null : thirdCateName.trim();
    }

    public Integer getSecondCateId() {
        return secondCateId;
    }

    public void setSecondCateId(Integer secondCateId) {
        this.secondCateId = secondCateId;
    }

    public String getSecondCateName() {
        return secondCateName;
    }

    public void setSecondCateName(String secondCateName) {
        this.secondCateName = secondCateName == null ? null : secondCateName.trim();
    }

    public Integer getFirstCateId() {
        return firstCateId;
    }

    public void setFirstCateId(Integer firstCateId) {
        this.firstCateId = firstCateId;
    }

    public String getFirstCateName() {
        return firstCateName;
    }

    public void setFirstCateName(String firstCateName) {
        this.firstCateName = firstCateName == null ? null : firstCateName.trim();
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }
}