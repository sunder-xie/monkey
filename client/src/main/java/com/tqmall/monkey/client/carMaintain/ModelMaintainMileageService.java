package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainMileageDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/8/12.
 */
public interface ModelMaintainMileageService {
    List<ModelMaintainMileageDO> findMaintainMileageByPlanId(Integer maintainPlanId);

    int saveModelMaintainMileage(ModelMaintainMileageDO record);

    /*
    *  批量插入保养里程
    *  mileage：里程数
    *  addStep：增量
    *  addNum：插入条数
    *  maintainPlanId：所属保养方案id
    * */
    int saveModelMaintainMileageBatch(int maintainPlanId, int mileage, int addStep, int addNum);

    int modifyModelMaintainMileage(ModelMaintainMileageDO record);

    /*
    *  删除保养里程
    * */
    int removeModelMaintainMileage(ModelMaintainMileageDO record);

    int removeModelMaintainMileage(List<Integer> idList);
}
