package com.tqmall.monkey.dal.mapper.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainMileageDO;

import java.util.List;

@MyBatisRepository
public interface ModelMaintainMileageDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModelMaintainMileageDO record);

    int insertSelective(ModelMaintainMileageDO record);

    ModelMaintainMileageDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModelMaintainMileageDO record);

    int updateByPrimaryKey(ModelMaintainMileageDO record);

    List<ModelMaintainMileageDO> getMaintainMileageByPlanId(Integer maintainPlanId);

    int insertMaintainMileageBatch(List<ModelMaintainMileageDO> dataList);

    int checkMileageExsit(ModelMaintainMileageDO record);

    List<ModelMaintainMileageDO> selectByPlanId(Integer maintainPlanId);

    List<Integer> selectMileageByPlanId(Integer maintainPlanId);
}