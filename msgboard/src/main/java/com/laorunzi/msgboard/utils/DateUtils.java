package com.laorunzi.msgboard.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 15:53
 * 4 时间日期工具类
 */
public class DateUtils {

    /**
     * 获取一个时间的后几个月的时间
     * @param date 起始时间
     * @param months 相隔几个月, 整数为之后, 负数为之前
     * @return
     */
    public static Date getDayAfterMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();// 得到日历
        calendar.setTime(date); // 把当前时间赋给日历
        calendar.add(Calendar.MONTH, months);  // 设置为后一个月
        Date targetDate = calendar.getTime();
        return targetDate;
    }

    /**
     * 获取两个时间之间相差的秒数 date2 - date1
     * @param date1
     * @param date2
     * @param abs // 是否取绝对值
     * @return
     */
    public static long getSecendsBetweenTwoDate(Date date1, Date date2, boolean abs) {
        Long t1 = date1.getTime();
        Long t2 = date2.getTime();
        long result = t2 - t1;
        return abs ? Math.abs(result) : result;
    }
}
