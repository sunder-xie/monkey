package com.tqmall.monkey.dal.mapper.staffPer;

import com.tqmall.monkey.dal.MyBatisRepository;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHandleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface StaffPerHandleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StaffPerHandleDO record);

    int insertSelective(StaffPerHandleDO record);

    StaffPerHandleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaffPerHandleDO record);

    int updateByPrimaryKey(StaffPerHandleDO record);

    /**
     * 根据 month 和 month 的前一个月, 获取 两个月份的数据
     *
     * @param month
     * @param prevMonth
     * @return
     */
    List<StaffPerHandleDO> select2MonthDataByMonth(@Param("month") String month, @Param("prevMonth") String prevMonth);

    /**
     * 根据 monthStrArray 月份数组 或 staffId 员工ID, 批量获取 这几个月的数据
     *
     * @param monthStrArray
     * @return
     */
    List<StaffPerHandleDO> selectMonthDataByMonthStaff(@Param("monthStrArray") String[] monthStrArray, @Param("staffId") Integer staffId);

    /**
     * 批量增加 handles
     *
     * @param handles
     */
    void insertBatch(List<StaffPerHandleDO> handles);

    List<StaffPerHandleDO> select(StaffPerHandleDO handle);
}