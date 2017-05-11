package com.tqmall.monkey.client.carMaintain;

import com.tqmall.monkey.dal.dao.carMaintain.CarMaintainPlanDao;
import com.tqmall.monkey.domain.entity.carMaintain.CarMaintainPlanDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by huangzhangting on 15/8/14.
 */

@Service
public class CarMaintainPlanServiceImpl implements CarMaintainPlanService{
    @Autowired
    private CarMaintainPlanDao carMaintainPlanDao;

    @Override
    @Transactional
    public void modifyCarMaintainPlan(List<CarMaintainPlanDO> addList, List<CarMaintainPlanDO> deleteList) {
        if(addList!=null){
            carMaintainPlanDao.getCarMaintainPlanMapper().insertMaintainPlanBatch(addList);
        }
        if(deleteList!=null){
            int count;
            for(CarMaintainPlanDO planDO : deleteList){
//                planDO.setIsDeleted(true);
//                count = carMaintainPlanDao.getCarMaintainPlanMapper().checkCarPlanExsit(planDO);
//                if(count==0){
//                    carMaintainPlanDao.getCarMaintainPlanMapper().deleteCarMaintainPlanLogic(planDO);
//                }else {
//                    carMaintainPlanDao.getCarMaintainPlanMapper().deleteCarMaintainPlan(planDO);
//                }

                carMaintainPlanDao.getCarMaintainPlanMapper().deleteCarMaintainPlan(planDO);
            }
        }
    }

}
