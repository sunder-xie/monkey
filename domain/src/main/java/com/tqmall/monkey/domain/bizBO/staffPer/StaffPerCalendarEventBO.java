package com.tqmall.monkey.domain.bizBO.staffPer;

import lombok.Data;

/**
 * Calendar 使用的 事件对象
 * Created by lyj on 16/3/1.
 */
@Data
public class StaffPerCalendarEventBO {
    private Integer id;
    private String title;
    private String start;
    private Boolean allDay;
    private String color;

}
