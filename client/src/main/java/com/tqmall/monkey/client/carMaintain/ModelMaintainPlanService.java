package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.domain.entity.carMaintain.MaintainPlanDetail;
import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainPlanDO;

import java.util.List;

/**
 * Created by huangzhangting on 15/8/12.
 */
public interface ModelMaintainPlanService {
    List<ModelMaintainPlanDO> findMaintainPlanByModel(String carBrand, String company, String carSeries, String carModel);

    /*
    *  查询判断保养里程和保养项的关系
    *  true：需要保养
    *  false：不需要保养
    * */
    boolean findMaintainDetail(Integer modelMileageId, Integer maintainId);

    int saveModelMaintainPlan(ModelMaintainPlanDO record);

    /*
    *  修改车型保养方案，目前就是修改方案名称，以及逻辑删除
    * */
    int modifyModelMaintainPlan(ModelMaintainPlanDO record);

    /*
    *  修改保养方案明细，包括新增和删除，就是修改保养里程和保养项得对应关系
    * */
    void modifyMaintainPlanDetail(List<MaintainPlanDetail> addList, List<MaintainPlanDetail> deleteList);

    void copyModelMaintainPlan(ModelMaintainPlanDO plan);

}
