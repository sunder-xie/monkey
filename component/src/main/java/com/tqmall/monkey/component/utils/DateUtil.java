package com.tqmall.monkey.component.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * success!
 * Created by bairui on 14/12/15.
 */
public class DateUtil {
    public final static String DateFormat_yyyy_MM_dd = "yyyy-MM-dd";
    public final static String DateFormat_yyyyMMdd = "yyyyMMdd";
    public final static String DateFormat_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public final static String DateFormat_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public final static String DateFormat_yyyy_MM = "yyyy-MM";
    public final static String DateFormat_dd = "dd";

    //获取当前时间
    public static Timestamp getCurrentTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }

    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

    /**
     * 相对于当前时间, 获取其他月份(num 为负数获取前几个月, num 为正数获取后几个月, num 为0获取当前月)
     * Created by lyj on 16/2/23
     *
     * @param num
     * @param dateFormatStr
     * @return
     */
    public static String getMonthBasedOnNow(int num, String dateFormatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, num);
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);

        return format.format(calendar.getTime());
    }

    /**
     * 获得 某个月的 第一天
     *
     * @param num
     * @param dateFormatStr
     * @return
     */
    public static String getFirstDayByMonth(int num, String dateFormatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, num);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
        return format.format(calendar.getTime());
    }

    /**
     * 获得 某个月的 最后一天
     *
     * @param num
     * @param dateFormatStr
     * @return
     */
    public static String getLastDayByMonth(int num, String dateFormatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, num + 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
        return format.format(calendar.getTime());
    }

    /**
     * 获得 每个月的 总天数
     *
     * @param num
     * @return
     */
    public static Integer getNumOfMonthByMonth(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, num);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 根据字符串 获取 date
     * @param timeStr
     * @param dateFormatStr
     * @return
     */
    public static Date getDateByTimeStr(String timeStr, String dateFormatStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
            Date date = sdf.parse(timeStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String token() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        int random = (int)(Math.random() * 9999);
        return formatter.format(new Date()) + String.format("%04d", random);
    }


    public static void main(String[] args) {
        System.out.println(dateToString(new Date(), DateUtil.DateFormat_yyyyMMddHHmmss));
        System.out.println(getFirstDayByMonth(-1, DateFormat_yyyy_MM_dd));
        System.out.println(getLastDayByMonth(2, DateFormat_dd));
        System.out.println(getNumOfMonthByMonth(-1));
    }
}

