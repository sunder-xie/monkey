package com.tqmall.monkey.client.staffPer;

import com.alibaba.fastjson.JSON;
import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.component.utils.DateUtil;
import com.tqmall.monkey.component.utils.ResultUtil;
import com.tqmall.monkey.dal.mapper.staffPer.StaffPerDailyDOMapper;
import com.tqmall.monkey.dal.mapper.staffPer.StaffPerHandleDOMapper;
import com.tqmall.monkey.dal.mapper.staffPer.StaffPerHolidayDOMapper;
import com.tqmall.monkey.dal.mapper.staffPer.StaffPerStaffDOMapper;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerCalendarEventBO;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerChartRecord;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerStaffXHandleBO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerDailyDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHandleDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHolidayDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerStaffDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by lyj on 16/2/23.
 */
@Transactional
@Service
public class StaffPerServiceImpl implements StaffPerService {
    @Autowired
    private StaffPerHandleDOMapper staffPerHandleDOMapper;

    @Autowired
    private StaffPerHolidayDOMapper staffPerHolidayDOMapper;

    @Autowired
    private StaffPerStaffDOMapper staffPerStaffDOMapper;

    @Autowired
    private StaffPerDailyDOMapper staffPerDailyDOMapper;

    private static final String MODIFY_DAILY_TYPE_MODIFY = "1";
    private static final String MODIFY_DAILY_TYPE_CREATE = "2";
    private static final String HANDLE_START_DAY = "0000-00-00";

    @Override
    public Result<StaffPerStaffDO> addStaff(StaffPerStaffDO staffPerStaffDO) {
        staffPerStaffDOMapper.insertSelective(staffPerStaffDO);
        return ResultUtil.wrapSuccessfulResult(staffPerStaffDO);
    }

    @Override
    public Result<List<StaffPerStaffDO>> getAllStaff() {
        return ResultUtil.wrapSuccessfulResult(staffPerStaffDOMapper.selectAll());
    }

    @Override
    public Result<List<StaffPerStaffXHandleBO>> getStaffXHandleByYearMonth(String prevMonth, String prev2Month) {
        String[] monthStrArray = {prevMonth, prev2Month};
        List<StaffPerHandleDO> handleList = staffPerHandleDOMapper.selectMonthDataByMonthStaff(monthStrArray, null);

        List<StaffPerStaffXHandleBO> staffXHandleList = new ArrayList<>();
        List<StaffPerStaffDO> staffList = staffPerStaffDOMapper.selectAll();
        for (StaffPerStaffDO temp : staffList) {
            StaffPerStaffXHandleBO obj = new StaffPerStaffXHandleBO();

            obj.setStaffPerStaffDO(temp);

            StaffPerHandleDO prev2Handle = getHandleByMonthStaffId(handleList, prev2Month, temp.getId());
            //两个月前没有数据的话, 错误率算作0.1
            Double rate = (prev2Handle == null || prev2Handle.getTotalNum() == 0) ? 0.1 : Double.valueOf(prev2Handle.getErrorNum()) / prev2Handle.getTotalNum();
            obj.setPervMonthErrorRate(rate);

            StaffPerHandleDO prevHandle = getHandleByMonthStaffId(handleList, prevMonth, temp.getId());
            if (prevHandle == null) {
                obj.setScorePerv(0);
                obj.setScoreActual(0);
                obj.setStaffPerHandleDO(new StaffPerHandleDO());
                obj.setMonthErrorRate(0);
            } else {
                Float actualDays = prevHandle.getActualDays() == 0 ? 1 : prevHandle.getActualDays();
                obj.setScorePerv((1 - rate) * prevHandle.getTotalNum() / actualDays);

                Double rateActual = prevHandle.getTotalNum() == 0 ? 0 : Double.valueOf(prevHandle.getErrorNum()) / prevHandle.getTotalNum();
                obj.setScoreActual((1 - rateActual) * prevHandle.getTotalNum() / actualDays);

                obj.setStaffPerHandleDO(prevHandle);
                obj.setMonthErrorRate(rateActual);
            }
            staffXHandleList.add(obj);
        }

        return ResultUtil.wrapSuccessfulResult(staffXHandleList);
    }

    @Override
    public Result<StaffPerHandleDO> findHandleByPk(Integer id) {
        return ResultUtil.wrapSuccessfulResult(staffPerHandleDOMapper.selectByPrimaryKey(id));
    }

    @Override
    public Result<StaffPerHandleDO> modifyHandle(String jsonStr) {
        StaffPerHandleDO handle = JSON.parseObject(jsonStr, StaffPerHandleDO.class);

        StaffPerHandleDO temp = staffPerHandleDOMapper.selectByPrimaryKey(handle.getId());
        if (temp != null) {
            staffPerHandleDOMapper.updateByPrimaryKeySelective(handle);

            //跟新
        }
        return ResultUtil.wrapSuccessfulResult(temp);
    }

    @Override
    public Result<List<StaffPerHandleDO>> addBatchHandles(List<StaffPerHandleDO> handles) {
        staffPerHandleDOMapper.insertBatch(handles);
        return ResultUtil.wrapSuccessfulResult(handles);
    }

    @Override
    public Result<List<StaffPerHandleDO>> addBatchHandlesByAllStaff(List<StaffPerStaffDO> staffs) {
        //生成这个月的数据
        int month = 0;
        //获取上个月的年月份
        String timeYearMonth = DateUtil.getMonthBasedOnNow(month, DateUtil.DateFormat_yyyy_MM);

        List<StaffPerHandleDO> list = new ArrayList<>();
        for (StaffPerStaffDO staff : staffs) {
            Date date = new Date();
            StaffPerHandleDO handle = new StaffPerHandleDO();

            handle.setActualDays(0f);
            handle.setErrorNum(0);
            handle.setGmtCreate(date);
            handle.setGmtModified(date);
            handle.setIsDeleted("N");
            handle.setStartDay(HANDLE_START_DAY);
            handle.setTimeYearMonth(timeYearMonth);
            handle.setTotalNum(0);
            handle.setStaffId(staff.getId());
            handle.setRemarks("");

            list.add(handle);
        }

        return addBatchHandles(list);
    }

    @Override
    public Result<StaffPerHolidayDO> findHolidayByTimeYearMonth(String timeYearMonth) {
        return ResultUtil.wrapSuccessfulResult(staffPerHolidayDOMapper.selectByTimeYearMonth(timeYearMonth));
    }

    //--------start jsChart data --------//
    @Override
    public List<StaffPerChartRecord> getCDSomeMonthPerByStaff(Integer staffId, Integer count) {
        String[] monthStrArray = new String[count];
        for (int i = 0; i < count; i++) {
            monthStrArray[i] = DateUtil.getMonthBasedOnNow(-(i + 1), DateUtil.DateFormat_yyyy_MM);
        }

        List<StaffPerChartRecord> chartRecordList = new ArrayList<>();
        List<StaffPerHandleDO> list = staffPerHandleDOMapper.selectMonthDataByMonthStaff(monthStrArray, staffId);
        if (!list.isEmpty()) {
            for (StaffPerHandleDO temp : list) {
                Float actualDays = temp.getActualDays() == 0 ? 1 : temp.getActualDays();
                Double rateActual = temp.getTotalNum() == 0 ? 0 : Double.valueOf(temp.getErrorNum()) / temp.getTotalNum();

                StaffPerChartRecord chartRecord = new StaffPerChartRecord();
                chartRecord.setKey(temp.getTimeYearMonth());
                chartRecord.setValue((1 - rateActual) * temp.getTotalNum() / actualDays);

                chartRecordList.add(chartRecord);
            }
        }
        return chartRecordList;
    }

    @Override
    public List<StaffPerChartRecord> getCDSomeMonthErrorRateByStaff(Integer staffId, Integer count) {
        String[] monthStrArray = new String[count];
        for (int i = 0; i < count; i++) {
            monthStrArray[i] = DateUtil.getMonthBasedOnNow(-(i + 1), DateUtil.DateFormat_yyyy_MM);
        }

        List<StaffPerChartRecord> chartRecordList = new ArrayList<>();
        List<StaffPerHandleDO> list = staffPerHandleDOMapper.selectMonthDataByMonthStaff(monthStrArray, staffId);
        if (!list.isEmpty()) {
            for (StaffPerHandleDO temp : list) {
                Double rateActual = temp.getTotalNum() == 0 ? 0 : Double.valueOf(temp.getErrorNum()) / temp.getTotalNum();

                StaffPerChartRecord chartRecord = new StaffPerChartRecord();
                chartRecord.setKey(temp.getTimeYearMonth());
                chartRecord.setValue(rateActual);

                chartRecordList.add(chartRecord);
            }
        }
        return chartRecordList;
    }

    @Override
    public List<StaffPerChartRecord> getCDSomeMonthAveragePer(Integer count) {
        String[] monthStrArray = new String[count];
        for (int i = 0; i < count; i++) {
            monthStrArray[i] = DateUtil.getMonthBasedOnNow(-(i + 1), DateUtil.DateFormat_yyyy_MM);
        }

        List<StaffPerChartRecord> chartRecordList = new ArrayList<>();
        List<StaffPerHandleDO> list = staffPerHandleDOMapper.selectMonthDataByMonthStaff(monthStrArray, null);

        int len = monthStrArray.length;
        for (int i = len - 1; i >= 0; i--) {
            List<StaffPerHandleDO> handleList = getHandleByMonth(list, monthStrArray[i]);
            Double sum = 0.0;
            int staffNum = handleList == null ? 0 : handleList.size();

            if (staffNum > 0) {
                int lenJ = staffNum;

                for (int j = 0; j < lenJ; j++) {
                    if (handleList.get(j).getTotalNum() == 0) {
                        staffNum--;
                    } else {
                        Float actualDays = handleList.get(j).getActualDays() == 0 ? 1 : handleList.get(j).getActualDays();
                        Double rateActual = handleList.get(j).getTotalNum() == 0 ? 0 : Double.valueOf(handleList.get(j).getErrorNum()) / handleList.get(j).getTotalNum();
                        sum += (1 - rateActual) * handleList.get(j).getTotalNum() / actualDays;
                    }
                }

                StaffPerChartRecord chartRecord = new StaffPerChartRecord();
                chartRecord.setKey(monthStrArray[i]);
                chartRecord.setValue(sum == 0 ? 0 : sum / staffNum);

                chartRecordList.add(chartRecord);
            }
        }

        return chartRecordList;
    }

    @Override
    public List<StaffPerChartRecord> getCDSomeMonthAverageErrorRate(Integer count) {
        String[] monthStrArray = new String[count];
        for (int i = 0; i < count; i++) {
            monthStrArray[i] = DateUtil.getMonthBasedOnNow(-(i + 1), DateUtil.DateFormat_yyyy_MM);
        }

        List<StaffPerChartRecord> chartRecordList = new ArrayList<>();
        List<StaffPerHandleDO> list = staffPerHandleDOMapper.selectMonthDataByMonthStaff(monthStrArray, null);

        int len = monthStrArray.length;
        for (int i = len - 1; i >= 0; i--) {
            List<StaffPerHandleDO> handleList = getHandleByMonth(list, monthStrArray[i]);
            Double sum = 0.0;
            int staffNum = handleList == null ? 0 : handleList.size();

            if (staffNum > 0) {
                int lenJ = staffNum;

                for (int j = 0; j < lenJ; j++) {
                    if (handleList.get(j).getTotalNum() == 0) {
                        staffNum--;
                    } else {
                        Double rateActual = Double.valueOf(handleList.get(j).getErrorNum()) / handleList.get(j).getTotalNum();
                        sum += rateActual;
                    }
                }

                StaffPerChartRecord chartRecord = new StaffPerChartRecord();
                chartRecord.setKey(monthStrArray[i]);
                chartRecord.setValue(sum == 0 ? 0 : sum / staffNum);

                chartRecordList.add(chartRecord);
            }
        }

        return chartRecordList;
    }

    @Override
    public List<StaffPerChartRecord> getCDSomeMonthTotalNum(Integer count) {
        String[] monthStrArray = new String[count];
        for (int i = 0; i < count; i++) {
            monthStrArray[i] = DateUtil.getMonthBasedOnNow(-(i + 1), DateUtil.DateFormat_yyyy_MM);
        }

        List<StaffPerChartRecord> chartRecordList = new ArrayList<>();
        List<StaffPerHandleDO> list = staffPerHandleDOMapper.selectMonthDataByMonthStaff(monthStrArray, null);

        int len = monthStrArray.length;
        for (int i = len - 1; i >= 0; i--) {
            List<StaffPerHandleDO> handleList = getHandleByMonth(list, monthStrArray[i]);
            Double sum = 0.0;
            int staffNum = handleList == null ? 0 : handleList.size();

            if (staffNum > 0) {
                int lenJ = staffNum;

                for (int j = 0; j < lenJ; j++) {
                    sum += handleList.get(j).getTotalNum();
                }

                StaffPerChartRecord chartRecord = new StaffPerChartRecord();
                chartRecord.setKey(monthStrArray[i]);
                chartRecord.setValue(sum);

                chartRecordList.add(chartRecord);
            }
        }

        return chartRecordList;
    }
    //--------end jsChart data --------//

    private StaffPerHandleDO getHandleByMonthStaffId(List<StaffPerHandleDO> handleList, String month, Integer id) {
        if (handleList == null || handleList.size() == 0 && id == null) {
            return null;
        }
        for (StaffPerHandleDO temp : handleList) {
            if (id.equals(temp.getStaffId()) && month.equals(temp.getTimeYearMonth())) {
                return temp;
            }
        }
        return null;
    }

    private List<StaffPerHandleDO> getHandleByMonth(List<StaffPerHandleDO> list, String month) {
        if (list == null || list.size() == 0) {
            return null;
        }

        List<StaffPerHandleDO> tempList = new ArrayList<>();
        for (StaffPerHandleDO temp : list) {
            if (month.equals(temp.getTimeYearMonth())) {
                tempList.add(temp);
            }
        }
        return tempList;
    }

    @Override
    public Result<StaffPerStaffDO> findStaffByName(String nickName) {
        StaffPerStaffDO staffPerStaffDO = new StaffPerStaffDO();
        staffPerStaffDO.setStaffName(nickName);
        List<StaffPerStaffDO> list = staffPerStaffDOMapper.select(staffPerStaffDO);
        if (list.isEmpty()) {
            return ResultUtil.wrapErrorResult("001", "没有找到名字为" + nickName + "的统计的员工");
        } else {
            return ResultUtil.wrapSuccessfulResult(list.get(0));
        }

    }

    @Override
    public Result<StaffPerDailyDO> findDailyByStaffAndTime(Integer staffId, String timeYearMonth, String timeDay) {
        StaffPerDailyDO staffPerDailyDO = new StaffPerDailyDO();
        staffPerDailyDO.setStaffId(staffId);
        staffPerDailyDO.setTimeYearMonth(timeYearMonth);
        staffPerDailyDO.setTimeDay(timeDay);

        List<StaffPerDailyDO> list = staffPerDailyDOMapper.select(staffPerDailyDO);
        if (list.isEmpty()) {
            return ResultUtil.wrapErrorResult("001", "没有" + timeYearMonth + timeDay + "的数据");
        } else {
            return ResultUtil.wrapSuccessfulResult(list.get(0));
        }
    }

    @Override
    public Result<StaffPerDailyDO> modifyDaily(String jsonStr, String type) {
        StaffPerDailyDO daily = JSON.parseObject(jsonStr, StaffPerDailyDO.class);

        StaffPerHandleDO handle = new StaffPerHandleDO();
        handle.setStaffId(daily.getStaffId());
        handle.setTimeYearMonth(daily.getTimeYearMonth());
        List<StaffPerHandleDO> handleList = staffPerHandleDOMapper.select(handle);

        Date date = new Date();
        if (MODIFY_DAILY_TYPE_CREATE.equals(type)) {
            if (handleList.isEmpty()) {
                return ResultUtil.wrapErrorResult("001", "新增 daily 失败, 没有该员工的 handle!");
            } else {
                daily.setId(null);
                daily.setErrorNum(-1);
                daily.setGmtCreate(date);
                daily.setGmtModified(date);
                daily.setIsDeleted("N");
                staffPerDailyDOMapper.insertSelective(daily);
                modifyDailyToModifyHandle(handleList.get(0), daily, date, MODIFY_DAILY_TYPE_CREATE, null);

                return ResultUtil.wrapSuccessfulResult(daily);
            }
        } else if (MODIFY_DAILY_TYPE_MODIFY.equals(type)) {
            if (handleList.isEmpty()) {
                return ResultUtil.wrapErrorResult("002", "修改 daily 失败, 没有该员工的 handle!");
            } else {
                StaffPerDailyDO oldDaily = staffPerDailyDOMapper.selectByPrimaryKey(daily.getId());

                daily.setGmtModified(date);
                daily.setIsDeleted("N");
                staffPerDailyDOMapper.updateByPrimaryKeySelective(daily);
                modifyDailyToModifyHandle(handleList.get(0), daily, date, MODIFY_DAILY_TYPE_MODIFY, oldDaily);

                return ResultUtil.wrapSuccessfulResult(daily);
            }
        } else {
            return ResultUtil.wrapErrorResult("003", "新增或修改 daily 失败, 不存在的 type!");
        }
    }

    @Override
    public Result<List<StaffPerCalendarEventBO>> getStaffEventsByStaffAndTime(Integer staffId, String timeYearMonth) {
        StaffPerDailyDO staffPerDailyDO = new StaffPerDailyDO();
        staffPerDailyDO.setStaffId(staffId);
        staffPerDailyDO.setTimeYearMonth(timeYearMonth);

        List<StaffPerDailyDO> staffPerDailyDOList = staffPerDailyDOMapper.select(staffPerDailyDO);
        if (staffPerDailyDOList.isEmpty()) {
            return ResultUtil.wrapErrorResult("001", "X");
        } else {
            List<StaffPerCalendarEventBO> list = new ArrayList<>();
            for (StaffPerDailyDO daily : staffPerDailyDOList) {
                String color = "green";
                if (daily.getPercentOfDay() == 0) {
                    color = "orange";
                } else if (daily.getTotalNum() == 0) {
                    color = "red";
                }

                StaffPerCalendarEventBO ce = new StaffPerCalendarEventBO();
                ce.setAllDay(true);
                ce.setColor(color);
                ce.setId(daily.getId());
                ce.setStart(daily.getTimeYearMonth() + "-" + daily.getTimeDay());
                ce.setTitle(String.valueOf(daily.getTotalNum()));
                list.add(ce);
            }
            return ResultUtil.wrapSuccessfulResult(list);
        }
    }

    @Override
    public List<StaffPerChartRecord> getCurrentDataOnNowMonthByStaff(Integer staffId) {
        List<StaffPerChartRecord> chartRecordList = new ArrayList<>();
        StaffPerDailyDO staffPerDailyDO = new StaffPerDailyDO();
        staffPerDailyDO.setIsDeleted("N");
        staffPerDailyDO.setStaffId(staffId);
        List<StaffPerDailyDO> list = staffPerDailyDOMapper.select(staffPerDailyDO);
        if (!list.isEmpty()) {
            for (StaffPerDailyDO temp : list) {
                StaffPerChartRecord chartRecord = new StaffPerChartRecord();
                chartRecord.setKey(temp.getTimeDay());
                chartRecord.setValue(Double.valueOf(temp.getTotalNum()));

                chartRecordList.add(chartRecord);
            }
        }
        return chartRecordList;
    }

    private void modifyDailyToModifyHandle(StaffPerHandleDO old, StaffPerDailyDO daily, Date date, String type, StaffPerDailyDO oldDaily) {
        if (type.equals(MODIFY_DAILY_TYPE_CREATE)) {
            if (HANDLE_START_DAY.equals(old.getStartDay())) {
                old.setStartDay(daily.getTimeYearMonth() + "-" + daily.getTimeDay());
            }
            old.setActualDays(old.getActualDays() + 1 * daily.getPercentOfDay());
            old.setGmtModified(date);
            old.setTotalNum(old.getTotalNum() + daily.getTotalNum());
        } else if (type.equals(MODIFY_DAILY_TYPE_MODIFY)) {

            old.setActualDays(old.getActualDays() - 1 * oldDaily.getPercentOfDay() + 1 * daily.getPercentOfDay());
            old.setGmtModified(date);
            old.setTotalNum(old.getTotalNum() - oldDaily.getTotalNum() + daily.getTotalNum());
        }

        staffPerHandleDOMapper.updateByPrimaryKeySelective(old);
    }
}
