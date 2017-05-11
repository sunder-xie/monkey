package com.tqmall.monkey.domain.entity.carMaintain;

/**
 * Created by huangzhangting on 15/8/13.
 *
 *  保养方案明细实体类
 */
public class MaintainPlanDetail {
    private Integer modelMileageId;//车型保养里程id
    private Integer modelMileage;
    private Integer maintainId;//保养项id

    public Integer getModelMileageId() {
        return modelMileageId;
    }

    public void setModelMileageId(Integer modelMileageId) {
        this.modelMileageId = modelMileageId;
    }

    public Integer getModelMileage() {
        return modelMileage;
    }

    public void setModelMileage(Integer modelMileage) {
        this.modelMileage = modelMileage;
    }

    public Integer getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(Integer maintainId) {
        this.maintainId = maintainId;
    }
}
