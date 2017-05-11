package com.tqmall.monkey.dal.dao.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.mapper.carMaintain.CarMaintainPlanDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 15/8/14.
 */
@MyBatisRepository
public class CarMaintainPlanDao {
    @Autowired
    private CarMaintainPlanDOMapper carMaintainPlanMapper;

    public CarMaintainPlanDOMapper getCarMaintainPlanMapper() {
        return carMaintainPlanMapper;
    }
}
