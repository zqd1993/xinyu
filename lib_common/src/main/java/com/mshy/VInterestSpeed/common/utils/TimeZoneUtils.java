package com.mshy.VInterestSpeed.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by zhangbin on 2019/1/21.
 */

public class TimeZoneUtils {

    public static long getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));    //指定时区
        String format = sdf.format(Calendar.getInstance().getTime());
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
        return timestamp;
    }

    //获取当前 - 年 - 月 - 日
    public static String getCurrentYearMonthDay(String fileType){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int shi = cal.get(Calendar.HOUR_OF_DAY);
        int fen = cal.get(Calendar.MINUTE);
        int miao = cal.get(Calendar.SECOND);
        int suiji = new Random().nextInt(10000);
        StringBuffer buffer = new StringBuffer();
        buffer.append("/"+year+"").append("_")
                .append(month+"").append("_")
                .append(day+"").append("_")
                .append(shi+"").append("_")
                .append(fen+"").append("_")
                .append(miao+"").append("_")
                .append(suiji+"").append(".")
                .append(fileType);
        return  buffer.toString();
    }


    //获取当前 - 年 - 月 - 日
    public static String getCurrentYearMonthDayJpg(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int shi = cal.get(Calendar.HOUR_OF_DAY);
        int fen = cal.get(Calendar.MINUTE);
        int miao = cal.get(Calendar.SECOND);
        int suiji = new Random().nextInt(10000);
        StringBuffer buffer = new StringBuffer();
        buffer.append("/"+year+"").append("_")
                .append(month+"").append("_")
                .append(day+"").append("_")
                .append(shi+"").append("_")
                .append(fen+"").append("_")
                .append(miao+"").append("_")
                .append(suiji+"").append(".jpg");
        return  buffer.toString();
    }
}
