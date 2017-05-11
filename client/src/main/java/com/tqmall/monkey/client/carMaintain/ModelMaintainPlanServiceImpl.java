package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.dal.dao.carMaintain.ModelMaintainMileageDao;
import com.tqmall.monkey.dal.dao.carMaintain.ModelMaintainPlanDao;
import com.tqmall.monkey.domain.entity.carMaintain.MaintainPlanDetail;
import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainMileageDO;
import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainPlanDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/8/12.
 */

@Service
public class ModelMaintainPlanServiceImpl implements ModelMaintainPlanService{
    @Autowired
    private ModelMaintainPlanDao modelMaintainPlanDao;
    @Autowired
    private ModelMaintainMileageDao maintainMileageDao;


    @Override
    public List<ModelMaintainPlanDO> findMaintainPlanByModel(String carBrand, String company, String carSeries, String carModel) {
        Map<String, Object> params = new HashMap<>();
        params.put("carBrand", carBrand);
        params.put("company", company);
        params.put("carSeries", carSeries);
        params.put("carModel", carModel);

        return modelMaintainPlanDao.getMaintainPlanMapper().getMaintainPlanByModel(params);
    }

    @Override
    public boolean findMaintainDetail(Integer modelMileageId, Integer maintainId) {
        Map<String, Object> params = new HashMap<>();
        params.put("modelMileageId", modelMileageId);
        params.put("maintainId", maintainId);

        int count = modelMaintainPlanDao.getMaintainPlanMapper().getMaintainDetail(params);

        return (count>0);
    }

    @Override
    public int saveModelMaintainPlan(ModelMaintainPlanDO record) {
        return modelMaintainPlanDao.getMaintainPlanMapper().insertSelective(record);
    }

    @Override
    public int modifyModelMaintainPlan(ModelMaintainPlanDO record) {
        return modelMaintainPlanDao.getMaintainPlanMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    @Transactional
    public void modifyMaintainPlanDetail(List<MaintainPlanDetail> addList, List<MaintainPlanDetail> deleteList) {
        if(addList!=null){
            this.modelMaintainPlanDao.getMaintainPlanMapper().insertMaintainDetailBatch(addList);
        }
        if(deleteList!=null){
            for(MaintainPlanDetail del : deleteList){
                this.modelMaintainPlanDao.getMaintainPlanMapper().deleteMaintainDetail(del);
            }
        }
    }

    @Override
    @Transactional
    public void copyModelMaintainPlan(ModelMaintainPlanDO plan) {
        if(plan==null){
            return;
        }
        Integer copyPlanId = plan.getId();
        if(copyPlanId==null){
            return;
        }
        List<ModelMaintainMileageDO> mileageList = maintainMileageDao.getMaintainMileageMapper().selectByPlanId(copyPlanId);
        if(mileageList.isEmpty()){
            return;
        }

        plan.setId(null);
        plan.setMaintainPlan("拷贝方案"+copyPlanId);
        modelMaintainPlanDao.getMaintainPlanMapper().insertSelective(plan);

        List<Integer> mileIds = new ArrayList<>();
        for(ModelMaintainMileageDO mile : mileageList){
            mileIds.add(mile.getId());

            mile.setId(null);
            mile.setMaintainPlanId(plan.getId());
        }
        maintainMileageDao.getMaintainMileageMapper().insertMaintainMileageBatch(mileageList);

        List<MaintainPlanDetail> detailList = modelMaintainPlanDao.getMaintainPlanMapper().selectDetailByMileIds(mileIds);
        if(detailList.isEmpty()){
            return;
        }
        mileageList = maintainMileageDao.getMaintainMileageMapper().selectByPlanId(plan.getId());
        for(MaintainPlanDetail detail : detailList){
            for (ModelMaintainMileageDO mileage : mileageList){
                if(mileage.getMileage().equals(detail.getModelMileage())){
                    detail.setModelMileageId(mileage.getId());
                    break;
                }
            }
        }

        modelMaintainPlanDao.getMaintainPlanMapper().insertMaintainDetailBatch(detailList);
    }

}
