package com.tqmall.monkey.dal.mapper.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.carMaintain.CarMaintainPlanDO;

import java.util.List;

@MyBatisRepository
public interface CarMaintainPlanDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CarMaintainPlanDO record);

    int insertSelective(CarMaintainPlanDO record);

    CarMaintainPlanDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CarMaintainPlanDO record);

    int updateByPrimaryKey(CarMaintainPlanDO record);
    /*
    *  批量插入数据
    * */
    int insertMaintainPlanBatch(List<CarMaintainPlanDO> dataList);
    /*
    *  逻辑删除
    * */
    int deleteCarMaintainPlanLogic(CarMaintainPlanDO record);

    int deleteCarMaintainPlan(CarMaintainPlanDO record);

    int checkCarPlanExsit(CarMaintainPlanDO record);
}