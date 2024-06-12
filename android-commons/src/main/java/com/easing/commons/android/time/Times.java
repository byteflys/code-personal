package com.easing.commons.android.time;

import android.annotation.TargetApi;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.value.time.YMD;
import com.easing.commons.android.value.time.TimeZone;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

@TargetApi(26)
@SuppressWarnings("all")
public class Times {

    public static final String FORMAT_01 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_02 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_03 = "yyyy年MM月dd日HH时mm分ss秒";
    public static final String FORMAT_04 = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String FORMAT_05 = "yyyy年MM月dd日 HH时mm分";
    public static final String FORMAT_06 = "yyyy-MM-dd";
    public static final String FORMAT_07 = "yyyy/MM/dd";
    public static final String FORMAT_08 = "yyyy年MM月dd日";
    public static final String FORMAT_09 = "yyyy年MM月";
    public static final String FORMAT_10 = "HH时mm分ss秒";
    public static final String FORMAT_11 = "HH:mm:ss";
    public static final String FORMAT_12 = "yyyyMMddHHmmss";
    public static final String FORMAT_13 = "yyyyMMdd_HHmmss";
    public static final String FORMAT_14 = "yyyy年MM月dd日HH时mm分";
    public static final String FORMAT_15 = "yyyy-MM";
    public static final String FORMAT_16 = "yyyy/MM";
    public static final String FORMAT_17 = "HH:mm";
    public static final String FORMAT_18 = "yyyy";

    public static final List<String> FORMATS = new ArrayList();

    static {
        FORMATS.add(FORMAT_01);
        FORMATS.add(FORMAT_02);
        FORMATS.add(FORMAT_03);
        FORMATS.add(FORMAT_04);
        FORMATS.add(FORMAT_05);
        FORMATS.add(FORMAT_06);
        FORMATS.add(FORMAT_07);
        FORMATS.add(FORMAT_08);
        FORMATS.add(FORMAT_09);
        FORMATS.add(FORMAT_10);
        FORMATS.add(FORMAT_11);
        FORMATS.add(FORMAT_12);
        FORMATS.add(FORMAT_13);
        FORMATS.add(FORMAT_14);
        FORMATS.add(FORMAT_15);
        FORMATS.add(FORMAT_16);
        FORMATS.add(FORMAT_17);
    }

    // ==================== Text ====================

    public static String now() {
        return Times.formatDate(new Date(), null);
    }

    public static String now(String format) {
        if (format == null)
            format = FORMAT_01;
        return Times.formatDate(new Date(), format);
    }

    //生成一个很早以前的日期
    public static String longLongAgo() {
        return "1900-01-01 00:00:00";
    }

    //今天的最后一秒
    public static String endOfToday() {
        return Times.now("yyyy-MM-dd 23:59:59");
    }

    //今天的最后一秒
    public static String startOfToday() {
        return Times.now("yyyy-MM-dd 00:00:00");
    }

    //sec -> HH:mm:ss
    public static String toHMSTime(Number sec, boolean showHour) {
        int h = sec.intValue() / 60 / 60;
        int m = (sec.intValue() - h * 60 * 60) / 60;
        int s = sec.intValue() - h * 60 * 60 - m * 60;
        if (h == 0 && !showHour)
            return Maths.keepInt(m, 2) + ":" + Maths.keepInt(s, 2);
        return Maths.keepInt(h, 2) + ":" + Maths.keepInt(m, 2) + ":" + Maths.keepInt(s, 2);
    }

    //min -> HH:mm
    public static String toHMTime(int min) {
        String h = Maths.keepInt(min / 60, 2);
        String m = Maths.keepInt(min % 60, 2);
        return h + ":" + m;
    }

    //HH:mm
    public static String toHMTime(String time) {
        time = time.trim().replaceAll("：", ":");
        String h = time.split(":")[0];
        String m = time.split(":")[1];
        h = Maths.keepInt(Integer.valueOf(h), 2);
        m = Maths.keepInt(Integer.valueOf(m), 2);
        return h + ":" + m;
    }

    //不规范的mm:ss => 规范的mm:ss
    public static String toMSTime(String time) {
        time = time.trim().replaceAll("：", ":");
        String m = time.split(":")[0];
        String s = time.split(":")[1];
        m = Maths.keepInt(Integer.valueOf(m), 2);
        s = Maths.keepInt(Integer.valueOf(s), 2);
        return m + ":" + s;
    }

    //sec -> mm:ss
    public static String secToMSTime(int sec) {
        String m = Maths.keepInt(sec / 60, 2);
        String s = Maths.keepInt(sec % 60, 2);
        return m + ":" + s;
    }

    //ms -> mm:ss
    public static String msToMSTime(int ms) {
        return Times.secToMSTime(ms / 1000);
    }

    //毫秒数转化为模糊时间
    public static String msToVagueTime1(long ms) {
        if (ms <= 60 * 1000L)
            return "刚刚";
        if (ms <= 60 * 60 * 1000L)
            return ms / (60 * 1000L) + "分钟前";
        if (ms <= 24 * 60 * 60 * 1000L)
            return ms / (60 * 60 * 1000L) + "小时前";
        if (ms <= 30 * 24 * 60 * 60 * 1000L)
            return ms / (24 * 60 * 60 * 1000L) + "天前";
        if (ms <= 12 * 30 * 24 * 60 * 60 * 1000L)
            return ms / (30 * 24 * 60 * 60 * 1000L) + "个月前";
        return ms / (12 * 30 * 24 * 60 * 60 * 1000L) + "年前";
    }

    //毫秒数转化为模糊时间
    public static String msToVagueTime2(long ms) {
        if (ms <= 60 * 1000L)
            return ms / 1000L + "秒";
        if (ms <= 60 * 60 * 1000L)
            return ms / (60 * 1000L) + "分钟";
        if (ms <= 24 * 60 * 60 * 1000L)
            return ms / (60 * 60 * 1000L) + "小时";
        if (ms <= 30 * 24 * 60 * 60 * 1000L)
            return ms / (24 * 60 * 60 * 1000L) + "天";
        if (ms <= 12 * 30 * 24 * 60 * 60 * 1000L)
            return ms / (30 * 24 * 60 * 60 * 1000L) + "个月";
        return ms / (12 * 30 * 24 * 60 * 60 * 1000L) + "年";
    }

    //为HH:mm格式的时间增加一小时
    public static String addHMHour(String hmTime, int hour) {
        hmTime = toHMTime(hmTime);
        int h = Integer.valueOf(hmTime.substring(0, 2));
        return toHMTime(h + hour + hmTime.substring(2));
    }

    //HH:mm -> min
    public static int hmTimeToMin(String hmTime) {
        String[] hm = hmTime.split(":");
        return Integer.valueOf(hm[0]) * 60 + Integer.valueOf(hm[1]);
    }

    // ==================== Long ====================

    public static long timestamp() {
        return System.currentTimeMillis();
    }

    public static long millisOfNow() {
        return System.currentTimeMillis();
    }

    //当天过了多少秒
    public static long millisOfDay() {
        return (System.currentTimeMillis() + 8 * 60 * 60 * 1000) % (24 * 60 * 60 * 1000);
    }

    public static String formatDate(Long ms) {
        if (ms == null || ms == 0)
            return null;
        Date date = new Date(ms);
        return Times.formatDate(date, null);
    }

    public static String formatDate(Number ms, String format) {
        Date date = new Date(ms.longValue());
        return Times.formatDate(date, format);
    }

    public static String msToText(String msText) {
        long ms = Long.valueOf(msText);
        Date date = new Date(ms);
        return Times.formatDate(date, null);
    }

    public static String msToText(String msText, String format) {
        long ms = Long.valueOf(msText);
        Date date = new Date(ms);
        return Times.formatDate(date, format);
    }

    // ==================== Date ====================

    public static Date date() {
        return new Date();
    }

    public static Date getDate(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, d);
        return calendar.getTime();
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    public static Date addMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    public static Date subMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    public static Date subDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    @SneakyThrows
    public static Date parseDate(String date, String format) {
        if (format == null)
            format = "YYYY-MM-DD HH:mm:ss";
        return new SimpleDateFormat(format).parse(date);
    }

    @SneakyThrows
    public static String formatDate(Date date, String format) {
        if (format == null)
            format = "yyyy-MM-dd HH:mm:ss";
        return new SimpleDateFormat(format).format(date);
    }

    //将日期从一个格式转换为另一个格式
    @SneakyThrows
    public static String switchDateFormat(String date, String oldFormat, String newFormat) {
        Date d = Times.parseDate(date, oldFormat);
        return Times.formatDate(d, newFormat);
    }

    //解析任意格式的日期
    @SneakyThrows
    public static Date parseDate(String date) {
        try {
            return new Date(Long.parseLong(date));
        } catch (Exception e) {
        }

        for (String format : FORMATS)
            try {
                return new SimpleDateFormat(format).parse(date);
            } catch (Exception e) {
            }

        Console.info("unknown date format：" + date);
        return null;
    }

    //任意格式的日期，转换为指定格式
    @SneakyThrows
    public static String formatDate(String date, String format) {
        Date d = parseDate(date);
        if (format == null)
            format = FORMAT_01;
        if (d != null)
            return new SimpleDateFormat(format).format(d);
        return null;
    }

    //任意格式的日期，转换为指定格式
    public static String formatDate(String date) {
        return Times.formatDate(date, null);
    }

    // ==================== DateTime ====================

    public static LocalDateTime getDateTime(Date date) {
        return LocalDateTime.parse(Times.formatDate(date, "yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static long getTimeMillis(LocalDateTime t, TimeZone zone) {
        Date date = Times.getZoneDate(t, zone);
        return date.getTime();
    }

    public static Date getZoneDate(LocalDateTime t, TimeZone zone) {
        if (zone == null)
            zone = zone.DEFAULT;
        return Date.from(t.atZone(zone.getZoneId()).toInstant());
    }

    public static LocalDate copy(LocalDate d) {
        return LocalDate.of(d.getYear(), d.getMonth(), d.getDayOfMonth());
    }

    public static String formatDate(LocalDate d, String format) {
        if (format == null)
            format = FORMAT_01;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return d.format(formatter);
    }

    public static String formatDateTime(LocalDateTime t, String format) {
        if (format == null)
            format = FORMAT_01;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return t.format(formatter);
    }

    public static String formatZoneTime(long t, TimeZone zone, String format) {
        if (format == null)
            format = FORMAT_01;
        if (zone == null)
            zone = zone.DEFAULT;
        LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochSecond(t / 1000), zone.getZoneId());
        String timeText = Times.formatDateTime(dt, format);
        return timeText;
    }

    // ==================== Calendar ====================

    //获得当前时间对应的Calendar
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    //Calendar转Date
    public static Date toDate(Calendar calendar) {
        return calendar.getTime();
    }

    //从年月日获取Calendar
    public static Calendar getCalendar(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, d);
        return calendar;
    }

    //data 转 Calendar
    public static Calendar getCalendarData(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 日期计算
     *
     * @param text   计算前的时间
     * @param format 日期格式
     * @param field  修改的类型
     *               Calendar.YEAR 年
     *               <p>
     *               Calendar.DAY_OF_MONTH 月
     *               <p>
     *               Calendar.DATE 天
     *               <p>
     *               Calendar.WEEK_OF_YEAR 周
     *               <p>
     *               Calendar.HOUR_OF_DAY 小时
     *               <p>
     *               Calendar.MINUTE 分钟
     *               <p>
     *               Calendar.SECOND 秒
     *               <p>
     *               Calendar.MILLISECOND 毫秒
     * @param isAdd  加还是减
     * @param disk   需要计算的数 ，
     * @return 新的时间
     */
    public static String hanCalendarCalculations(String text, String format, int field, boolean isAdd, int disk) {
        Calendar calendar = parseCalendar(text, format);
        //时间更改
        if (isAdd)
            calendar.add(field, +disk);
        else
            calendar.add(field, -disk);

        return formatCalendar(calendar, format);

    }

    public static Calendar copy(Calendar calendar) {
        Calendar copyCalendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int M = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DATE);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int s = calendar.get(Calendar.SECOND);
        copyCalendar.set(y, M, d, h, m, s);
        return copyCalendar;
    }

    //从字符串解析Calendar 日期转Calendar
    @SneakyThrows
    public static Calendar parseCalendar(String text, String format) {
        if (format == null)
            format = FORMAT_01;
        Date date = new SimpleDateFormat(format).parse(text);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    //格式化Calendar
    @SneakyThrows
    public static String formatCalendar(Calendar calendar, String format) {
        if (format == null)
            format = FORMAT_01;
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    //判断是否在某个区间内 parseCalendar
    public static boolean inTimeZone(String timeText, Calendar startTime, Calendar endTime, String format) {
        if (format == null)
            format = Times.FORMAT_01;
        String startTimeText = Times.formatCalendar(startTime, format);
        String endTimeText = Times.formatCalendar(endTime, format);
        return timeText.compareTo(startTimeText) >= 0 && timeText.compareTo(endTimeText) < 0;
    }


    //判断是否在某个区间内 parseCalendar
    public static boolean inTimeZone(String timeText, String startTimeText, String endTimeText) {

        return timeText.compareTo(startTimeText) >= 0 && timeText.compareTo(endTimeText) < 0;
    }


    //判断是否在某个区间内
    public static boolean inTimeZone(Calendar time, Calendar startTime, Calendar endTime, String format) {
        if (format == null)
            format = Times.FORMAT_01;
        String timeText = Times.formatCalendar(time, format);
        String startTimeText = Times.formatCalendar(startTime, format);
        String endTimeText = Times.formatCalendar(endTime, format);
        return timeText.compareTo(startTimeText) >= 0 && timeText.compareTo(endTimeText) < 0;
    }

    // ==================== YMD ====================

    public static YMD getYmd() {
        Calendar calendar = Calendar.getInstance();
        return YMD.create(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
    }

    // ==================== Tool Function ====================

    //判断是不是HH:mm格式的时间
    public static boolean isHMTime(String time) {
        if (!time.contains(":"))
            return false;
        try {
            String[] hm = time.split(":");
            int h = Integer.valueOf(hm[0]);
            int m = Integer.valueOf(hm[1]);
            if (h < 0 || h > 23)
                return false;
            if (m < 0 || m > 59)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //HH:mm格式的时间转换为，相当于当天00:00的毫秒数
    public static long HMTimeToMillis(String time) {
        String[] hm = time.split(":");
        int h = Integer.valueOf(hm[0]);
        int m = Integer.valueOf(hm[1]);
        return (h * 60 + m) * 1000;
    }

    //计算时间差
    public static long duration(String startTime, String endTime) {
        Date start = parseDate(startTime);
        Date end = parseDate(endTime);
        return end.getTime() - start.getTime();
    }

    /**
     * 获取指定日期所在月份开始的时间
     * 时间格式yyyyMMdd
     *
     * @param date 指定日期
     * @return
     */
    public static String getMonthBegin(Date date) {
        SimpleDateFormat aDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间
        return aDateFormat.format(c.getTime());
    }

    /**
     * 获取指定日期所在月份结束的时间戳
     *
     * @param date 指定日期
     * @return
     */
    public static Long getMonthEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 999);
        // 获取本月最后一天的时间戳
        return c.getTimeInMillis();
    }

    /**
     * 判断指定时间 和现在是否在同一天
     *
     * @param date 指定日期 时间 "2021-05-21 13:24:39"
     * @return 是否在同一天
     */
    public static boolean isTimeDay(String date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = parseCalendar(date, FORMAT_01);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 时间转时间戳
     */
    public static long getLastDayOfMonthInMillis(String time, String format) {
        final Calendar cal = parseCalendar(time, format);
        return cal.getTimeInMillis();
    }

    /**
     * 时间换算
     *
     * @param num 毫秒
     */
    public static String convertTime(long num) {

        StringBuffer stringBuffer = new StringBuffer();

        long day = num / (24 * 60 * 60 * 1000);
        long hour = (num / (60 * 60 * 1000) - day * 24);
        long min = ((num / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (num / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day != 0)
            stringBuffer.append(day + "天");
        if (hour != 0)
            stringBuffer.append(hour + "小时");
        if (min != 0)
            stringBuffer.append(min + "分");
        if (s != 0)
            stringBuffer.append(s + "秒");
        return stringBuffer.toString();
    }

    /**
     * 时间换算
     *
     * @param num 毫秒
     */
    public static String convertTime1(long num) {

        if (num == 0)
            return "0";

        StringBuffer stringBuffer = new StringBuffer();

        long day = num / (24 * 60 * 60 * 1000);
        long hour = (num / (60 * 60 * 1000) - day * 24);
        long min = ((num / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (num / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day != 0)
            stringBuffer.append(new DecimalFormat("00").format(day) + ":");
        if (hour != 0)
            stringBuffer.append(new DecimalFormat("00").format(hour) + ":");
        if (min != 0)
            stringBuffer.append(new DecimalFormat("00").format(min) + ":");
        if (s != 0)
            stringBuffer.append(new DecimalFormat("00").format(s));
        return stringBuffer.toString();
    }
}

