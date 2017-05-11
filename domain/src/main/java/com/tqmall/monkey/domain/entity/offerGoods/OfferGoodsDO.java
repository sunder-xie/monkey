package com.tqmall.monkey.domain.entity.offerGoods;

import lombok.Data;

import java.util.Date;

@Data
public class OfferGoodsDO {
    //报价单商品状态：0-新建 1-正在上架 2-上架成功 3-博士审核失败 4-epc有对应数据，进入初级人工审核
    // 5-crawl有对应数据进入初级人工审核 6-扒取库中无对应正确数据 7-专业审核失败
    public final static Integer status_新建 = 0;
    public final static Integer status_正在上架 = 1;
    public final static Integer status_上架成功 = 2;
    public final static Integer status_博士审核失败 = 3;
    public final static Integer status_epc有数据进入初级人工审核 = 4;
    public final static Integer status_crawl扒取库有数据进入初级人工审核= 5;
    public final static Integer status_crawl扒取库中无对应正确数据 = 6;
    public final static Integer status_专业审核失败 = 7;

    //商品分类状态:0-新建 1-仅分类匹配成功 2-分类匹配失败，未导出 3-导入pool池 4-分类匹配失败，已导出
    public final static Integer cate_status_new = 0;
    public final static Integer cate_status_success = 1;
    public final static Integer cate_status_fail_not_excle = 2;
    public final static Integer cate_status_toPool = 3;
    public final static Integer cate_status_fail_excle = 4;

    //品牌状态
    public final static Integer brand_status_success = 1;
    public final static Integer brand_status_fail = 2;
    public final static Integer brand_status_toPool = 3;

    //oe 没有出错
    public final static Integer oe_right = 0;
    //没有被删除
    public final static Integer not_delete = 0;

    //插入表的自增主键
    private Integer id;

    private String oeNum;

    private String goodsName;

    private Integer brandId;

    private String brandName;

    private String goodsFormat;

    private String brandPartcode;

    private String measureUnit;

    private Integer conversionValue;

    private String minMeasureUnit;

    private String packageFormat;

    private Byte carPartsType;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer partId;

    private String partName;

    private String partSumCode;

    private Integer cateStatus;

    private Integer brandStatus;

    private Integer updateId;

    private Integer createId;

    private String remark;

    private Integer goodsQualityType;

    private Integer thirdCateId;

    private String thirdCateName;

    private Integer secondCateId;

    private String secondCateName;

    private Integer firstCateId;

    private String firstCateName;

    private Integer isdelete;

    private Integer oeIswrong;

    private  Integer cateKind;
    //冗余
    private String providerName;

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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getGoodsFormat() {
        return goodsFormat;
    }

    public void setGoodsFormat(String goodsFormat) {
        this.goodsFormat = goodsFormat == null ? null : goodsFormat.trim();
    }

    public String getBrandPartcode() {
        return brandPartcode;
    }

    public void setBrandPartcode(String brandPartcode) {
        this.brandPartcode = brandPartcode == null ? null : brandPartcode.trim();
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit == null ? null : measureUnit.trim();
    }

    public Integer getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(Integer conversionValue) {
        this.conversionValue = conversionValue;
    }

    public String getMinMeasureUnit() {
        return minMeasureUnit;
    }

    public void setMinMeasureUnit(String minMeasureUnit) {
        this.minMeasureUnit = minMeasureUnit == null ? null : minMeasureUnit.trim();
    }

    public String getPackageFormat() {
        return packageFormat;
    }

    public void setPackageFormat(String packageFormat) {
        this.packageFormat = packageFormat == null ? null : packageFormat.trim();
    }

    public Byte getCarPartsType() {
        return carPartsType;
    }

    public void setCarPartsType(Byte carPartsType) {
        this.carPartsType = carPartsType;
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

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

}