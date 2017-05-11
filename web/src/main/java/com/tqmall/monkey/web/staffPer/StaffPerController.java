package com.tqmall.monkey.web.staffPer;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.shiro.MonkeyJdbcRealm;
import com.tqmall.monkey.client.staffPer.StaffPerService;
import com.tqmall.monkey.component.utils.DateUtil;
import com.tqmall.monkey.component.utils.ResultUtil;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerCalendarEventBO;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerChartRecord;
import com.tqmall.monkey.domain.bizBO.staffPer.StaffPerStaffXHandleBO;
import com.tqmall.monkey.domain.entity.UserDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerDailyDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHandleDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerHolidayDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerStaffDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 员工处理数据效率值 控制器
 * Created by lyj on 16/2/23.
 */
@Slf4j
@Controller
@RequestMapping("/monkey/staffPer")
public class StaffPerController {
    @Autowired
    private MonkeyJdbcRealm monkeyJdbcRealm;//获得当前登录用户信息
    @Autowired
    private StaffPerService staffPerService;

    public static List<StaffPerChartRecord> chartDataPrev;//使用统计月的前一个月的粗无虑
    public static List<StaffPerChartRecord> chartData;//使用统计月的错误率

    public static String DATA_FORMAT_FLAG = "yyyy_MM_dd";//daily js中使用的 日期格式, 用来控制是当日才能输入, 还是当月

    //首页--权限控制
    @RequiresRoles(value = {"data_admin"}, logical = Logical.OR)
    @RequestMapping(value = "index")
    public ModelAndView index() throws Exception {
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("monkey/currentUser");
        if (user == null) {
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("monkey/staffPerPage/staffPer");
        String prevMonth = DateUtil.getMonthBasedOnNow(-1, DateUtil.DateFormat_yyyy_MM);
        String prev2Month = DateUtil.getMonthBasedOnNow(-2, DateUtil.DateFormat_yyyy_MM);

        Result<List<StaffPerStaffXHandleBO>> result = staffPerService.getStaffXHandleByYearMonth(prevMonth, prev2Month);
        if (result.isSuccess()) {
            chartDataPrev = getChartRecordPrevList(result.getData());
            chartData = getChartRecordList(result.getData());

            modelAndView.addObject("dataList", result.getData());
        }
        modelAndView.addObject("timeYearMonth", DateUtil.getMonthBasedOnNow(-1, DateUtil.DateFormat_yyyy_MM));
        modelAndView.addObject("dateFormatFlag", DATA_FORMAT_FLAG);

        return modelAndView;
    }

    @RequestMapping(value = "/addStaff", method = RequestMethod.POST)
    @ResponseBody
    public Result<StaffPerStaffDO> addStaff(@RequestBody StaffPerStaffDO staffPerStaffDO) throws Exception {
        return staffPerService.addStaff(staffPerStaffDO);
    }

    @RequestMapping(value = "/getHandle", method = RequestMethod.GET)
    @ResponseBody
    public Result<StaffPerHandleDO> getHandle(Integer id) throws Exception {
        return staffPerService.findHandleByPk(id);
    }

    @RequestMapping(value = "/modifyHandle", method = RequestMethod.POST)
    @ResponseBody
    public Result<StaffPerHandleDO> modifyHandle(String jsonStr) throws Exception {
        return staffPerService.modifyHandle(jsonStr);
    }

    @RequestMapping(value = "/addBatchHandlesByAllStaff", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<StaffPerHandleDO>> addBatchHandlesByAllStaff(String day) throws Exception {
        if (day != null && day.equals(DateUtil.dateToString(new Date(), DateUtil.DateFormat_yyyy_MM_dd))) {
            Result<List<StaffPerStaffDO>> result = staffPerService.getAllStaff();
            if (result.isSuccess()) {
                return staffPerService.addBatchHandlesByAllStaff(result.getData());
            }
        }
        return ResultUtil.wrapErrorResult("001", "员工数据获取失败");
    }

    @RequestMapping(value = "/getHandleAndHoliday", method = RequestMethod.GET)
    @ResponseBody
    public Result getHandleAndHoliday(Integer id) throws Exception {
        Result<StaffPerHandleDO> handleResult = staffPerService.findHandleByPk(id);

        Map<String, Object> dataMap = new HashMap<>();
        if (handleResult.isSuccess()) {
            Result<StaffPerHolidayDO> holidayResult = staffPerService.findHolidayByTimeYearMonth(handleResult.getData().getTimeYearMonth());

            if (holidayResult.isSuccess()) {
                dataMap.put("handleResult", handleResult.getData());
                dataMap.put("holidayResult", holidayResult.getData());

                return ResultUtil.wrapSuccessfulResult(dataMap);
            }

            return ResultUtil.wrapErrorResult("001", "员工前月工作数据 和 节假日数据 获取失败!");
        } else {
            return ResultUtil.wrapErrorResult("002", "员工前月工作数据 和 节假日数据 获取失败!");
        }
    }

    //--------start jsChart data --------//
    /*
    获取 所有员工 在统计月 的 伪效率值(通过统计月的前一个月的错误率来计算) 数据
     */
    @RequestMapping(value = "/getCDSmStaffPer", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSmStaffPer(Integer type) throws Exception {
        if (type == 0) {//初始进入页面
            return ResultUtil.wrapSuccessfulResult(chartDataPrev);
        } else {
            String prevMonth = DateUtil.getMonthBasedOnNow(-1, DateUtil.DateFormat_yyyy_MM);
            String prev2Month = DateUtil.getMonthBasedOnNow(-2, DateUtil.DateFormat_yyyy_MM);

            Result<List<StaffPerStaffXHandleBO>> result = staffPerService.getStaffXHandleByYearMonth(prevMonth, prev2Month);
            if (result.isSuccess()) {
                return ResultUtil.wrapSuccessfulResult(getChartRecordPrevList(result.getData()));
            }
            return ResultUtil.wrapErrorResult("001", "获取所有员工, 在统计月的伪效率值(通过统计月的前一个月的错误率来计算)数据失败!");
        }
    }

    /*
    获取 所有员工 在统计月 的 真效率值(通过统计月的错误率来计算) 数据
     */
    @RequestMapping(value = "/getCDSmStaffActualPer", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSmStaffActualPer(Integer type) throws Exception {
        if (type == 0) {//初始进入页面
            return ResultUtil.wrapSuccessfulResult(chartData);
        } else {
            String prevMonth = DateUtil.getMonthBasedOnNow(-1, DateUtil.DateFormat_yyyy_MM);
            String prev2Month = DateUtil.getMonthBasedOnNow(-2, DateUtil.DateFormat_yyyy_MM);

            Result<List<StaffPerStaffXHandleBO>> result = staffPerService.getStaffXHandleByYearMonth(prevMonth, prev2Month);
            if (result.isSuccess()) {
                return ResultUtil.wrapSuccessfulResult(getChartRecordList(result.getData()));
            }
            return ResultUtil.wrapErrorResult("001", "获取所有员工, 在统计月的真效率值(通过统计月的错误率来计算)数据失败!");
        }
    }

    /*
    获取 所有员工 历史几个月份 的 平均效率值 数据
     */
    @RequestMapping(value = "/getCDSomeMonthAveragePer", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSomeMonthAveragePer(Integer count) throws Exception {
        if (count < 1) {
            count = 1;
        }
        return ResultUtil.wrapSuccessfulResult(staffPerService.getCDSomeMonthAveragePer(count));
    }

    /*
    获取 单个员工 历史几个月份 的 效率值 数据
     */
    @RequestMapping(value = "/getCDSomeMonthPerByStaff", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSomeMonthPer(Integer staffId, Integer count) throws Exception {
        if (count < 1) {
            count = 1;
        }
        return ResultUtil.wrapSuccessfulResult(staffPerService.getCDSomeMonthPerByStaff(staffId, count));
    }

    /*
    获取 所有员工 历史几个月份 的 平均错误率 数据
     */
    @RequestMapping(value = "/getCDSomeMonthAverageErrorRate", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSomeMonthAverageErrorRate(Integer count) throws Exception {
        if (count < 1) {
            count = 1;
        }
        return ResultUtil.wrapSuccessfulResult(staffPerService.getCDSomeMonthAverageErrorRate(count));
    }

    /*
    获取 单个员工 历史几个月份 的 错误率 数据
     */
    @RequestMapping(value = "/getCDSomeMonthErrorRateByStaff", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSomeMonthErrorRateByStaff(Integer staffId, Integer count) throws Exception {
        if (count < 1) {
            count = 1;
        }
        return ResultUtil.wrapSuccessfulResult(staffPerService.getCDSomeMonthErrorRateByStaff(staffId, count));
    }

    /*
    获取 所有员工 历史几个月份 的 处理总数 数据
     */
    @RequestMapping(value = "/getCDSomeMonthTotalNum", method = RequestMethod.GET)
    @ResponseBody
    public Result getCDSomeMonthTotalNum(Integer count) throws Exception {
        if (count < 1) {
            count = 1;
        }
        return ResultUtil.wrapSuccessfulResult(staffPerService.getCDSomeMonthTotalNum(count));
    }

    //--------end jsChart data --------//

    private List<StaffPerChartRecord> getChartRecordPrevList(List<StaffPerStaffXHandleBO> data) {
        List<StaffPerChartRecord> list = new ArrayList<>();
        for (StaffPerStaffXHandleBO temp : data) {
            StaffPerChartRecord chartRecord = new StaffPerChartRecord();
            chartRecord.setKey(temp.getStaffPerStaffDO().getStaffName());
            chartRecord.setValue(temp.getScorePerv());
            list.add(chartRecord);
        }
        return list;
    }

    private List<StaffPerChartRecord> getChartRecordList(List<StaffPerStaffXHandleBO> data) {
        List<StaffPerChartRecord> list = new ArrayList<>();
        for (StaffPerStaffXHandleBO temp : data) {
            StaffPerChartRecord chartRecord = new StaffPerChartRecord();
            chartRecord.setKey(temp.getStaffPerStaffDO().getStaffName());
            chartRecord.setValue(temp.getScoreActual());
            list.add(chartRecord);
        }
        return list;
    }

    //--------start daily data --------//
    //首页--权限控制
    @RequiresRoles(value = {"data_operator", "data_admin"}, logical = Logical.OR)
    @RequestMapping(value = "dailyData")
    public ModelAndView dailyData() throws Exception {
        UserDO user = (UserDO) monkeyJdbcRealm.getSessionValue("monkey/currentUser");
        if (user == null) {
            return null;
        }

        ModelAndView modelAndView = new ModelAndView("monkey/staffPerPage/dailyData");

        Integer staffId = -1;
        log.info(user.getNickName());
        //Result<StaffPerStaffDO> result = staffPerService.findStaffByName("王栋");//测试环境
        Result<StaffPerStaffDO> result = staffPerService.findStaffByName(user.getNickName());//正式环境
        if (result.isSuccess()) {
            staffId = result.getData().getId();
            log.info(String.valueOf(staffId));
        }
        modelAndView.addObject("staffId", staffId);
        modelAndView.addObject("timeYearMonth", DateUtil.getMonthBasedOnNow(0, DateUtil.DateFormat_yyyy_MM));
        modelAndView.addObject("dateFormatFlag", DATA_FORMAT_FLAG);
        return modelAndView;
    }

    @RequestMapping(value = "/getDailyByStaffAndTime", method = RequestMethod.GET)
    @ResponseBody
    public Result<StaffPerDailyDO> getDailyByStaffAndTime(Integer staffId, String timeYearMonth, String timeDay)
            throws Exception {
        return staffPerService.findDailyByStaffAndTime(staffId, timeYearMonth, timeDay);
    }

    @RequestMapping(value = "/modifyDaily", method = RequestMethod.POST)
    @ResponseBody
    public Result<StaffPerDailyDO> modifyDaily(String jsonStr, String type) throws Exception {
        return staffPerService.modifyDaily(jsonStr, type);
    }

    @RequestMapping(value = "/getStaffEventsByStaffAndTime", method = RequestMethod.GET)
    @ResponseBody
    public List<StaffPerCalendarEventBO> getStaffEventsByStaffAndTime(Integer staffId, String timeYearMonth)
            throws Exception {
        Result<List<StaffPerCalendarEventBO>> result = staffPerService.getStaffEventsByStaffAndTime(staffId, timeYearMonth);
        if (result.isSuccess()) {
            System.out.println(result.getData().get(0));
            return result.getData();
        } else {
            return new ArrayList<>();
        }
    }
    //--------end daily data --------//

    @RequestMapping(value = "/getDateFormatFlag", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getDateFormatFlag() throws Exception {
        return Result.wrapSuccessfulResult(DATA_FORMAT_FLAG);
    }

    @RequestMapping(value = "/modifyDateFormatFlag", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> modifyDateFormatFlag(Integer type) throws Exception {
        if (type == 1) {
            DATA_FORMAT_FLAG = "yyyy_MM";
        } else {
            DATA_FORMAT_FLAG = "yyyy_MM_dd";
        }
        return Result.wrapSuccessfulResult(DATA_FORMAT_FLAG);
    }

    /*
    获取 指定员工 当月 到今天之前的所有处理数量
     */
    @RequestMapping(value = "/getCurrentDataOnNowMonthByStaff", method = RequestMethod.GET)
    @ResponseBody
    public Result getCurrentDataOnNowMonthByStaff(Integer staffId) throws Exception {
        return ResultUtil.wrapSuccessfulResult(staffPerService.getCurrentDataOnNowMonthByStaff(staffId));
    }

}
