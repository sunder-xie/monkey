package com.tqmall.monkey.dal.mapper.staffPer;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerStaffDO;

import java.util.List;

@MyBatisRepository
public interface StaffPerStaffDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StaffPerStaffDO record);

    int insertSelective(StaffPerStaffDO record);

    StaffPerStaffDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaffPerStaffDO record);

    int updateByPrimaryKey(StaffPerStaffDO record);

    /**
     * 获取所有员工
     * @return
     */
    List<StaffPerStaffDO> selectAll();

    List<StaffPerStaffDO> select(StaffPerStaffDO staffPerStaffDO);
}