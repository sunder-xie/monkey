package com.tqmall.monkey.dal.mapper.staffPer;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerDailyDO;

import java.util.List;

@MyBatisRepository
public interface StaffPerDailyDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StaffPerDailyDO record);

    int insertSelective(StaffPerDailyDO record);

    StaffPerDailyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaffPerDailyDO record);

    int updateByPrimaryKey(StaffPerDailyDO record);

    List<StaffPerDailyDO> select(StaffPerDailyDO staffPerDailyDO);
}