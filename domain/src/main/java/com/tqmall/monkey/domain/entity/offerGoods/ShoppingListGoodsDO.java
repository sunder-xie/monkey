package com.tqmall.monkey.domain.entity.offerGoods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingListGoodsDO {
    private Integer id;

    private Integer shoppingListId;

    private Integer offerGoodsId;

    private String goodsName;

    private String brandName;

    private BigDecimal primePriceTax;

    private BigDecimal primePriceNoTax;

    private BigDecimal primePriceS;

    private BigDecimal salePrice;

    private String oeNum;

    private Integer goodsNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Integer shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Integer getOfferGoodsId() {
        return offerGoodsId;
    }

    public void setOfferGoodsId(Integer offerGoodsId) {
        this.offerGoodsId = offerGoodsId;
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
        this.primePriceS = primePriceS;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}