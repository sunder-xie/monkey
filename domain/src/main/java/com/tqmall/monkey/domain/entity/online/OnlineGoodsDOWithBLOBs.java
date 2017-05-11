package com.tqmall.monkey.domain.entity.online;

public class OnlineGoodsDOWithBLOBs extends OnlineGoodsDO {
    private String goodsDesc;

    private String carModel;

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel == null ? null : carModel.trim();
    }
}