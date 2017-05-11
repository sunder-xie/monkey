package com.tqmall.monkey.client.staffPer;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerCalendarEventBO;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerChartRecord;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerStaffXHandleBO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerDailyDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHandleDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHolidayDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerStaffDO;

import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 16/2/23.
 */
public interface StaffPerService {

    Result<StaffPerStaffDO> addStaff(StaffPerStaffDO staffPerStaffDO);

    Result<List<StaffPerStaffDO>> getAllStaff();

    Result<List<StaffPerStaffXHandleBO>> getStaffXHandleByYearMonth(String prevMonth, String prev2Month);

    Result<StaffPerHandleDO> findHandleByPk(Integer id);

    Result<StaffPerHandleDO> modifyHandle(String jsonStr);

    /**
     * 批量增加 handles
     * @param list
     */
    Result<List<StaffPerHandleDO>> addBatchHandles(List<StaffPerHandleDO> list);

    /**
     * 根据员工数据批量生成 handles
     * @param staffs
     * @return
     */
    Result<List<StaffPerHandleDO>> addBatchHandlesByAllStaff(List<StaffPerStaffDO> staffs);

    /**
     * 根据 timeYearMonth 年月份 获取 StaffPerHolidayDO
     * @param timeYearMonth
     * @return
     */
    Result<StaffPerHolidayDO> findHolidayByTimeYearMonth(String timeYearMonth);

    //--------start jsChart data --------//
    List<StaffPerChartRecord> getCDSomeMonthPerByStaff(Integer staffId, Integer count);

    List<StaffPerChartRecord> getCDSomeMonthErrorRateByStaff(Integer staffId, Integer count);

    List<StaffPerChartRecord> getCDSomeMonthAveragePer(Integer count);

    List<StaffPerChartRecord> getCDSomeMonthAverageErrorRate(Integer count);

    List<StaffPerChartRecord> getCDSomeMonthTotalNum(Integer count);

    //--------end jsChart data --------//

    Result<StaffPerStaffDO> findStaffByName(String nickName);

    Result<StaffPerDailyDO> findDailyByStaffAndTime(Integer staffId, String timeYearMonth, String timeDay);

    Result<StaffPerDailyDO> modifyDaily(String jsonStr, String type);

    Result<List<StaffPerCalendarEventBO>> getStaffEventsByStaffAndTime(Integer staffId, String timeYearMonth);

    List<StaffPerChartRecord> getCurrentDataOnNowMonthByStaff(Integer staffId);
}
