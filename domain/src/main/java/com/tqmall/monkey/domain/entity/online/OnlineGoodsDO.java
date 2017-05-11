package com.tqmall.monkey.domain.entity.online;

import java.math.BigDecimal;
import java.util.Date;

public class OnlineGoodsDO {
    private Integer goodsId;

    private Short catId;

    private String goodsSn;

    private String newGoodsSn;

    private String goodsName;

    private String goodsNameStyle;

    private Short brandId;

    private String providerName;

    private Integer goodsNumber;

    private BigDecimal goodsWeight;

    private String keywords;

    private String goodsBrief;

    private String goodsThumb;

    private String goodsImg;

    private Byte isReal;

    private Integer addTime;

    private Boolean isDelete;

    private Boolean isBest;

    private Boolean isNew;

    private Boolean isHot;

    private Boolean isPromote;

    private Short goodsType;

    private String sellerNote;

    private String goodsFormat;

    private String barcode;

    private String packageFormat;

    private String factoryCode;

    private String minPackage;

    private String productCompany;

    private String measureUnit;

    private Integer conversionValue;

    private String minMeasureUnit;

    private String oeNum;

    private String repaireNum;

    private String productRegion;

    private String brandPartcode;

    private Integer initScore;

    private Integer sellerId;

    private String sellerNick;

    private Boolean sellerType;

    private Integer salesVolume;

    private Date gmtModified;

    private Byte goodsCarType;

    private Integer packingValue;

    private Byte carPartsType;

    private String packageMeasureUnit;

    private Integer goodsQualityType;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Short getCatId() {
        return catId;
    }

    public void setCatId(Short catId) {
        this.catId = catId;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn == null ? null : goodsSn.trim();
    }

    public String getNewGoodsSn() {
        return newGoodsSn;
    }

    public void setNewGoodsSn(String newGoodsSn) {
        this.newGoodsSn = newGoodsSn == null ? null : newGoodsSn.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsNameStyle() {
        return goodsNameStyle;
    }

    public void setGoodsNameStyle(String goodsNameStyle) {
        this.goodsNameStyle = goodsNameStyle == null ? null : goodsNameStyle.trim();
    }

    public Short getBrandId() {
        return brandId;
    }

    public void setBrandId(Short brandId) {
        this.brandId = brandId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName == null ? null : providerName.trim();
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public BigDecimal getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(BigDecimal goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief == null ? null : goodsBrief.trim();
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb == null ? null : goodsThumb.trim();
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg == null ? null : goodsImg.trim();
    }

    public Byte getIsReal() {
        return isReal;
    }

    public void setIsReal(Byte isReal) {
        this.isReal = isReal;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Boolean getIsBest() {
        return isBest;
    }

    public void setIsBest(Boolean isBest) {
        this.isBest = isBest;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(Boolean isPromote) {
        this.isPromote = isPromote;
    }

    public Short getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
    }

    public String getSellerNote() {
        return sellerNote;
    }

    public void setSellerNote(String sellerNote) {
        this.sellerNote = sellerNote == null ? null : sellerNote.trim();
    }

    public String getGoodsFormat() {
        return goodsFormat;
    }

    public void setGoodsFormat(String goodsFormat) {
        this.goodsFormat = goodsFormat == null ? null : goodsFormat.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getPackageFormat() {
        return packageFormat;
    }

    public void setPackageFormat(String packageFormat) {
        this.packageFormat = packageFormat == null ? null : packageFormat.trim();
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode == null ? null : factoryCode.trim();
    }

    public String getMinPackage() {
        return minPackage;
    }

    public void setMinPackage(String minPackage) {
        this.minPackage = minPackage == null ? null : minPackage.trim();
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany == null ? null : productCompany.trim();
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

    public String getOeNum() {
        return oeNum;
    }

    public void setOeNum(String oeNum) {
        this.oeNum = oeNum == null ? null : oeNum.trim();
    }

    public String getRepaireNum() {
        return repaireNum;
    }

    public void setRepaireNum(String repaireNum) {
        this.repaireNum = repaireNum == null ? null : repaireNum.trim();
    }

    public String getProductRegion() {
        return productRegion;
    }

    public void setProductRegion(String productRegion) {
        this.productRegion = productRegion == null ? null : productRegion.trim();
    }

    public String getBrandPartcode() {
        return brandPartcode;
    }

    public void setBrandPartcode(String brandPartcode) {
        this.brandPartcode = brandPartcode == null ? null : brandPartcode.trim();
    }

    public Integer getInitScore() {
        return initScore;
    }

    public void setInitScore(Integer initScore) {
        this.initScore = initScore;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick == null ? null : sellerNick.trim();
    }

    public Boolean getSellerType() {
        return sellerType;
    }

    public void setSellerType(Boolean sellerType) {
        this.sellerType = sellerType;
    }

    public Integer getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Integer salesVolume) {
        this.salesVolume = salesVolume;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Byte getGoodsCarType() {
        return goodsCarType;
    }

    public void setGoodsCarType(Byte goodsCarType) {
        this.goodsCarType = goodsCarType;
    }

    public Integer getPackingValue() {
        return packingValue;
    }

    public void setPackingValue(Integer packingValue) {
        this.packingValue = packingValue;
    }

    public Byte getCarPartsType() {
        return carPartsType;
    }

    public void setCarPartsType(Byte carPartsType) {
        this.carPartsType = carPartsType;
    }

    public String getPackageMeasureUnit() {
        return packageMeasureUnit;
    }

    public void setPackageMeasureUnit(String packageMeasureUnit) {
        this.packageMeasureUnit = packageMeasureUnit == null ? null : packageMeasureUnit.trim();
    }

    public Integer getGoodsQualityType() {
        return goodsQualityType;
    }

    public void setGoodsQualityType(Integer goodsQualityType) {
        this.goodsQualityType = goodsQualityType;
    }
}