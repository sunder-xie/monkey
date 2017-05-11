package com.tqmall.monkey.domain.bizBO.lop;

import lombok.Data;

/**
 * Created by huangzhangting on 16/7/30.
 */
@Data
public class WishListGoodsBO {

    private String goodsName;

    private String goodsOe;

    private Integer goodsNumber;

    private String goodsMeasureUnit;

    private String goodsMemo;

    /**
     * 页面传递参数，多个品质以逗号分割，插入时候会根据这个生成多个对象
     */
    private String qualityTypeStr;
}
