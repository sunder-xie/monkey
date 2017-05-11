package com.tqmall.monkey.dal.mapper.carMaintain;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.carMaintain.MaintainPlanDetail;
import com.tqmall.monkey.domain.entity.carMaintain.ModelMaintainPlanDO;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ModelMaintainPlanDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModelMaintainPlanDO record);

    int insertSelective(ModelMaintainPlanDO record);

    ModelMaintainPlanDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModelMaintainPlanDO record);

    int updateByPrimaryKey(ModelMaintainPlanDO record);

    List<ModelMaintainPlanDO> getMaintainPlanByModel(Map<String, Object> params);

    /*
    *  查询判断保养里程和保养项目的对应关系
    *  参数：保养里程id、保养项id
    *  >0：该里程有这个保养项
    *  =0：该里程没有这个保养项
    * */
    int getMaintainDetail(Map<String, Object> params);

    /*
    *  批量插入
    * */
    int insertMaintainDetailBatch(List<MaintainPlanDetail> dataList);

    /*
    *  删除保养方案明细，就是删除保养里程和保养项得对应关系
    * */
    int deleteMaintainDetail(MaintainPlanDetail record);

    List<MaintainPlanDetail> selectDetailByMileIds(List<Integer> list);
}