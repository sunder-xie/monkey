package com.tqmall.monkey.dal.mapper.staffPer;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHolidayDO;

@MyBatisRepository
public interface StaffPerHolidayDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StaffPerHolidayDO record);

    int insertSelective(StaffPerHolidayDO record);

    StaffPerHolidayDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaffPerHolidayDO record);

    int updateByPrimaryKey(StaffPerHolidayDO record);

    /**
     * 根据 timeYearMonth 年月份 获取数据
     * @param timeYearMonth
     * @return
     */
    StaffPerHolidayDO selectByTimeYearMonth(String timeYearMonth);
}