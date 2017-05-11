package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.domain.entity.carMaintain.CarMaintainPlanDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/8/14.
 */
public interface CarMaintainPlanService {
    /*
    *  修改车款保养方案配置，包括新增和删除
    * */
    void modifyCarMaintainPlan(List<CarMaintainPlanDO> addList, List<CarMaintainPlanDO> deleteList);

}
