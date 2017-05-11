package com.tqmall.monkey.domain.bizBO.staffPer;

import com.tqmall.monkey.domain.entity.staffPer.StaffPerHandleDO;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerStaffDO;
import lombok.Data;

/**
 * Created by lyj on 16/2/23.
 */
@Data
public class StaffPerStaffXHandleBO {
    //staff 信息
    private StaffPerStaffDO staffPerStaffDO;

    //handle 信息
    private StaffPerHandleDO staffPerHandleDO;

    //StaffPerHandleDO timeYearMonth 的前一个月的 错误率
    private double pervMonthErrorRate;

    //StaffPerHandleDO timeYearMonth 的 错误率
    private double monthErrorRate;

    //使用前一个月的错误率算出来的得分
    private double scorePerv;

    //使用本月的错误率算出来的得分
    private double scoreActual;
}
