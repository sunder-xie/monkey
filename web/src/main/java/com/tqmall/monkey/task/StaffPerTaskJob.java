package com.tqmall.monkey.task;

import com.tqmall.core.common.entity.Result;
import com.tqmall.monkey.client.staffPer.StaffPerService;
import com.tqmall.monkey.domain.entity.staffPer.StaffPerStaffDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每月开始的时候(1号0:00:00)，生成所有用户的 handle
 * Created by lyj on 16/4/7.
 */
@Slf4j
@Component("staffPerTaskJob")
public class StaffPerTaskJob {
    @Autowired
    private StaffPerService staffPerService;

    @Scheduled(cron = "0 0 0 1 * ? ")
    public void execute() throws Exception {
        try {
            Result<List<StaffPerStaffDO>> result = staffPerService.getAllStaff();
            if (result.isSuccess()) {
                staffPerService.addBatchHandlesByAllStaff(result.getData());
            } else {
                log.error("StaffPerQuartzJob: 员工数据获取失败!");
            }
        } catch (Exception ex) {
            log.error("StaffPerQuartzJob: " + "error!");
        }
    }
}
