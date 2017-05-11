package com.tqmall.monkey.dal.dao.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.dal.mapper.carMaintain.ModelMaintainPlanDOMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 15/8/12.
 */
@MyBatisRepository
public class ModelMaintainPlanDao {
    @Autowired
    private ModelMaintainPlanDOMapper maintainPlanMapper;

    public ModelMaintainPlanDOMapper getMaintainPlanMapper() {
        return maintainPlanMapper;
    }
}
