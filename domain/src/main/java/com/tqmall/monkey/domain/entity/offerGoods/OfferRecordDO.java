package com.tqmall.monkey.domain.entity.offerGoods;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data

public class OfferRecordDO {
    //是否上架
    public final static Integer status_not_dev = 0;
    public final static Integer status_dev = 1;
    public final static Integer status_fail = 2;


    private Integer id;

    private String recordName;

    private Integer offerGoodsId;

    private String providerName;

    private String linkmanName;

    private String linkmanPhone;

    private BigDecimal primePriceTax;

    private BigDecimal primePriceNoTax;

    private BigDecimal primePriceS;

    private BigDecimal adviceSalePrice;

    private String onhand;

    private Integer createId;

    private Integer updateId;

    private Integer status;

    private Date gmtCreate;

    private Date gmtModified;

    private String city;

    //自定义的销售价。无税的预采价x1.15
    private BigDecimal salePrice;
    //自定义时间
    private String gmtCreateString;
    private String gmtModifiedString;

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

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName == null ? null : providerName.trim();
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName == null ? null : linkmanName.trim();
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone == null ? null : linkmanPhone.trim();
    }

    public BigDecimal getPrimePriceTax() {
        return primePriceTax;
    }

    public void setPrimePriceTax(BigDecimal primePriceTax) {
        this.primePriceTax = primePriceTax;
    }

    public BigDecimal getPrimePriceNoTax() {
        return primePriceNoTax;
    }

    public void setPrimePriceNoTax(BigDecimal primePriceNoTax) {
        this.primePriceNoTax = primePriceNoTax;
    }

    public BigDecimal getPrimePriceS() {
        return primePriceS;
    }

    public void setPrimePriceS(BigDecimal primePriceS) {
        this.primePriceS = primePriceS ;
    }

    public String getOnhand() {
        return onhand;
    }

    public void setOnhand(String onhand) {
        this.onhand = onhand == null ? null : onhand.trim();
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

}