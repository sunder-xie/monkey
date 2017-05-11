package com.tqmall.monkey.domain.entity.category;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryDO {
    public final static String DefaultSecondCode = "XX";
    public final static String DefaultThirdCode = "XXX";
    public final static String IS_DELETED_TRUE = "Y";
    public final static String IS_DELETED_FALSE = "N";
    public final static Integer IS_LEAF_TRUE = 1;
    public final static Integer IS_LEAF_FALSE = 0;
    public final static Integer FIRST_LEVEL = 1;
    public final static Integer SECOND_LEVEL = 2;
    public final static Integer THIRD_LEVEL = 3;
    public final static String VEHICLE_CODE_NAN = "NaN";
    public final static String VEHICLE_CODE_CH= "CH";
    public final static String VEHICLE_CODE_C= "C";
    public final static String VEHICLE_CODE_H= "N";

    private Date gmtCreate;

    private Date gmtModified;

    private Integer creator;

    private Integer modifier;

    private Integer catId;

    private String catName;

    private Integer parentId;

    private Integer sortOrder;

    private String isDeleted;

    private Integer isLeaf;

    private Integer catLevel;

    private String catCode;

    private String vehicleCode;

    public void setDefaultProperties() {
        this.sortOrder = 50;
        this.isDeleted = IS_DELETED_FALSE;
        this.vehicleCode = "C";
    }

}