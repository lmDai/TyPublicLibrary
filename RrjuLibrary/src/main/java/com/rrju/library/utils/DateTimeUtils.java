package com.rrju.library.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间日期及格式化工具
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeUtils {

    private static final StringBuilder m_sbFormator = new StringBuilder();
    private static final Formatter m_formatter = new Formatter(m_sbFormator,
            Locale.getDefault());
    private static final Calendar m_calCurrent = Calendar.getInstance();
    private static final Calendar m_calVideo = Calendar.getInstance();
    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 毫秒数转换为时间格式化字符串
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(long timeMs) {
        return stringForTime(timeMs, false);
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public static String getCurrentDateYMD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 媒体时间格式
     *
     * @param time
     * @return
     */
    public static String mediaFormatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    /**
     * 获取小时和分钟时间格式
     *
     * @param time
     * @return
     */
    public static String getHourAndMinuteFormatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(time));
    }

    public static Date getHourAndMinuteDateTime(long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return new Date(time);
    }

    /**
     * 毫秒数转换为时间格式化字符串 支持是否显示小时
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(long timeMs, boolean existsHours) {
        boolean bNegative = timeMs < 0;// 是否为负数
        if (bNegative) {
            timeMs = -timeMs;
        }
        int totalSeconds = (int) (timeMs / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        m_sbFormator.setLength(0);
        try {
            if (hours > 0 || existsHours) {
                return m_formatter.format("%s%02d:%02d:%02d",
                        bNegative ? "-" : "", hours, minutes, seconds)
                        .toString();
            } else {
                return m_formatter.format("%s%02d:%02d", bNegative ? "- " : "",
                        minutes, seconds).toString();
            }
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 日期比较 （传入开始日期和结束日期相比，若小于开始日期，提交时间已到）
     *
     * @param startDate 开始日期字符串
     * @param endDate   结束日期字符串
     * @return true, 过期不可继续操作
     */
    public static boolean compareDate(String startDate, String endDate) {
        boolean isTimeOut = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTime(date1);
            endCalendar.setTime(date2);
            if (endCalendar.before(startCalendar)) {
                isTimeOut = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTimeOut;
    }

    /**
     * 时间比较 （传入开始时间和结束时间相比，若小于开始时间，提交时间已到）
     *
     * @param startTime 开始时间字符串
     * @param endTime   结束时间字符串
     * @return true, 过期不可继续操作
     */
    public static boolean compareTime(String startTime, String endTime) {
        boolean isTimeOut = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = sdf.parse(startTime);
            Date date2 = sdf.parse(endTime);
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTime(date1);
            endCalendar.setTime(date2);
            if (endCalendar.before(startCalendar)) {
                isTimeOut = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTimeOut;
    }

    /**
     * 获取相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysBetween(final Calendar startDate,
                                  final Calendar endDate) {
        final boolean forward = startDate.before(endDate);
        final int multiplier = forward ? 1 : -1;
        final Calendar date = (Calendar) startDate.clone();
        int daysBetween = 0;
        int fieldAccuracy = 4;
        int field;
        int dayBefore, dayAfter;

        while (forward && date.before(endDate) || !forward
                && endDate.before(date)) {
            switch (fieldAccuracy) {
                case 4:
                    field = Calendar.MILLISECOND;
                    break;
                case 3:
                    field = Calendar.SECOND;
                    break;
                case 2:
                    field = Calendar.MINUTE;
                    break;
                case 1:
                    field = Calendar.HOUR_OF_DAY;
                    break;
                default:
                case 0:
                    field = Calendar.DAY_OF_MONTH;
                    break;
            }
            dayBefore = date.get(Calendar.DAY_OF_MONTH);
            date.add(field, multiplier);
            dayAfter = date.get(Calendar.DAY_OF_MONTH);

            if (dayBefore == dayAfter && date.get(field) == endDate.get(field))
                fieldAccuracy--;
            if (dayBefore != dayAfter) {
                daysBetween += multiplier;
            }
        }
        return daysBetween;
    }

    private final static long HOURS_PER_DAY = 60 * 60 * 1000;

    /**
     * 获取相差小时或分钟
     *
     * @param startDate
     * @param endDate
     * @param hours
     * @return
     */
    public static int hoursOrMinutesBetween(final Calendar startDate,
                                            final Calendar endDate, boolean hours) {
        long beginMS = startDate.getTimeInMillis();
        long endMS = endDate.getTimeInMillis();
        long diff = (endMS - beginMS)
                / (hours ? HOURS_PER_DAY : (HOURS_PER_DAY / 60));
        return (int) diff;
    }

    public static String getPubTimeStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            String pubTime = null;
            String[] subStrings = str.split("-");
            if (subStrings.length >= 6) {
                m_calVideo.set(Integer.parseInt(subStrings[0]),
                        Integer.parseInt(subStrings[1]) - 1,
                        Integer.parseInt(subStrings[2]),
                        Integer.parseInt(subStrings[3]),
                        Integer.parseInt(subStrings[4]),
                        Integer.parseInt(subStrings[5]));
            } else if (subStrings.length >= 3) {
                m_calVideo.set(Integer.parseInt(subStrings[0]),
                        Integer.parseInt(subStrings[1]) - 1,
                        Integer.parseInt(subStrings[2]));
            } else {
                return "";
            }
            m_calCurrent.setTimeInMillis(System.currentTimeMillis());

            if (m_calCurrent.before(m_calVideo)) {// 未来的
                return "官方推荐";
            }
            m_calCurrent.add(Calendar.DAY_OF_YEAR, -7); // 一周内
            if (m_calCurrent.after(m_calVideo)) {
                pubTime = subStrings[0] + "年" + subStrings[1] + "月"
                        + subStrings[2] + "日";
            } else {
                m_calCurrent.add(Calendar.DAY_OF_YEAR, 7);
                int nDistanceDay = daysBetween(m_calVideo, m_calCurrent); // 与今天相距的天数
                if (nDistanceDay > 2) {
                    switch (m_calVideo.get(Calendar.DAY_OF_WEEK)) {
                        case 1:
                            pubTime = "星期日";
                            break;
                        case 2:
                            pubTime = "星期一";
                            break;
                        case 3:
                            pubTime = "星期二";
                            break;
                        case 4:
                            pubTime = "星期三";
                            break;
                        case 5:
                            pubTime = "星期四";
                            break;
                        case 6:
                            pubTime = "星期五";
                            break;
                        case 7:
                            pubTime = "星期六";
                            break;
                        default:
                            break;
                    }
                } else if (nDistanceDay == 2) {
                    pubTime = "前天";
                } else if (nDistanceDay == 1
                        || (nDistanceDay == 0 && m_calVideo
                        .get(Calendar.DAY_OF_YEAR) != m_calCurrent
                        .get(Calendar.DAY_OF_YEAR))) { // 相距不超过24小时，但不是同一天时，也认定为昨天
                    pubTime = "昨天";
                } else {
                    int nHoursOrMinutesDiff = hoursOrMinutesBetween(m_calVideo,
                            m_calCurrent, true);
                    if (nHoursOrMinutesDiff > 0) {
                        pubTime = nHoursOrMinutesDiff + "小时前";
                    } else {
                        nHoursOrMinutesDiff = hoursOrMinutesBetween(m_calVideo,
                                m_calCurrent, false);
                        if (nHoursOrMinutesDiff > 0) {
                            pubTime = nHoursOrMinutesDiff + "分钟前";
                        } else {
                            pubTime = "刚刚";
                        }
                    }
                }
            }
            return pubTime;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        String timeStr = timeStamp + "";
        if (timeStr.length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
//        if (calendar.before(inputTime)){
//            //当前时间在输入时间之前
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年"+"MM"+"月"+"dd"+"日");
//            return sdf.format(currenTimeZone);
//        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return timeFormatStr(inputTime, sdf.format(currenTimeZone));
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return "昨天" + " " + timeFormatStr(inputTime, sdf.format(currenTimeZone));
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "月" + "d" + "日");
                String temp1 = sdf.format(currenTimeZone);
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
                return temp1 + temp2;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "M" + "月" + "d" + "日");
                String temp1 = sdf.format(currenTimeZone);
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
                return temp1 + temp2;
            }

        }

    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Calendar calendar, String strDay) {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11) {
            tempStr = "下午" + " " + strDay;
        } else {
            tempStr = "上午" + " " + strDay;
        }
        return tempStr;
    }

    /**
     * 返回发布时间格式
     *
     * @param millis 时间
     * @return
     */
    public static String getDateStr(long millis) {

        long time = (System.currentTimeMillis() - millis);
        time /= 1000;
        long minute = time / 60;
        long hour = minute / 60;
        long day = hour / 24;

        String stime;
        if (day > 1) {
            stime = day + "天前";
        } else if (day <= 1) {
            stime = "昨天";
        } else if (hour > 0) {
            stime = hour + "小时前";
        } else if (minute > 0) {
            stime = minute + "分钟前";
        } else {
            stime = "刚刚";
        }
        return stime;

    }

    /**
     * 获取时间
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return sdf.format(time);
        } catch (Exception e) {
            return sdf.format(System.currentTimeMillis());
        }

    }

    /**
     * 获取只自定义格式的日期时间
     *
     * @param datetime
     * @return
     */
    public static String getDefinedMdate(String datetime, SimpleDateFormat formatter1) {
        try {
            ParsePosition pos = new ParsePosition(0);
            Date date = formatter.parse(datetime, pos);
            String str = formatter1.format(date);
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前年月日yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getYMDdate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(date);
            String str = sdf.format(date1);
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 日期格式 yyyy-MM-dd HH:mm
     *
     * @param datetime
     * @return
     */
    public static String getYMDHMdate(String datetime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            ParsePosition pos = new ParsePosition(0);
            Date date = sdf.parse(datetime, pos);
            String str = sdf.format(date);
            return str;
            // return sdf.format(new Date(Long.parseLong(birthday)));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 日期格式 yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getYMDHMdate(long time) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(new Date(time));
    }

    /**
     * 提交微博登陆中的生日
     *
     * @param birthday
     * @return
     */
    public static String getBirthday(String birthday) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(birthday);
            String str = sdf.format(date);
            return str;
            // return sdf.format(new Date(Long.parseLong(birthday)));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 年月日
     *
     * @param date
     * @return
     */
    public static String getYMDSeparatorByPoint(String date) {
        String _date = getBirthday(date);
        _date = _date.replace("-", ".");
        return _date;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static String StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    /**
     * 毫秒数转换为时间格式化字符串支持毫秒
     *
     * @param timeMs
     * @return
     */
    public static String stringForMillisecondTime(long timeMs,
                                                  boolean isShowMillisecond, boolean alignment) {
        return stringForTime(timeMs, false, isShowMillisecond, alignment);
    }

    /**
     * 毫秒数转换为时间格式化字符串 支持是否显示小时或毫秒
     *
     * @param timeMs
     * @param existsHours
     * @param existsMillisecond
     * @param alignment         是否需要统计显示宽度，如为true时，5:4.5就为05:04.5
     * @return
     */
    public static String stringForTime(long timeMs, boolean existsHours,
                                       boolean existsMillisecond, boolean alignment) {
        boolean bNegative = timeMs < 0;// 是否为负数
        if (bNegative) {
            timeMs = -timeMs;
        }
        int totalSeconds = (int) (timeMs / 1000);// 总计时间
        int millisecond = (int) (timeMs % 1000) / 100;// 毫秒
        int seconds = totalSeconds % 60;// 秒
        int minutes = (totalSeconds / 60) % 60;// 分
        int hours = totalSeconds / 3600;// 时

        m_sbFormator.setLength(0);
        try {
            // 判断是否支持小时
            if (hours > 0 || existsHours) {
                return m_formatter.format(
                        alignment ? "%s%02d:%02d:%02d" : "%s%d:%d:%d",
                        bNegative ? "-" : "", hours, minutes, seconds)
                        .toString();
            } else if (existsMillisecond) {
                if (minutes > 0 || alignment) {
                    return m_formatter.format(
                            alignment ? "%s%02d:%02d.%d" : "%s%d:%d.%d",
                            bNegative ? "- " : "", minutes, seconds,
                            millisecond).toString();
                } else {
                    return m_formatter.format(
                            alignment ? "%s%02d.%d" : "%s%d.%d",
                            bNegative ? "- " : "", seconds, millisecond)
                            .toString();
                }
            } else {
                return m_formatter.format(
                        alignment ? "%s%02d:%02d" : "%s%d:%d",
                        bNegative ? "- " : "", minutes, seconds).toString();
            }
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 获取两个日期之间的间隔时间
     *
     * @return
     */
    public static String getGapCount(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date startDate = formatter.parse(strDate, pos);

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        long time = (System.currentTimeMillis() - fromCalendar
                .getTimeInMillis());
        time /= 1000;
        long minute = time / 60;
        long hour = minute / 60;
        int day = (int) (hour / 24);
        String str = "";
        switch (day) {
            case 0:
                if (hour > 0) {
                    str = hour + "小时";
                } else if (minute > 0) {
                    str = minute + "分钟";
                } else {
                    str = "刚刚";
                }
                break;

            case 1:
                str = "昨天";
                break;
            default:
                str = day + "天前";
                break;
        }
        return str;
    }

    /**
     * 与当前时间的相差分钟数
     *
     * @param strDate
     * @return minute 相差分钟数
     */
    public static int getDifferMinute(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startDate = formatter.parse(strDate, pos);
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        long time = (fromCalendar.getTimeInMillis() - System
                .currentTimeMillis());
        time /= 1000;
        long minute = time / 60;
        return (int) minute;
    }

    /**
     * 两个相差小时数
     *
     * @param startTime
     * @param endTime
     * @return hour 相差小时数
     */
    public static int getDifferHour(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            endCalendar.setTime(endDate);
            int nHoursOrMinutesDiff = hoursOrMinutesBetween(startCalendar, endCalendar, true);
            return nHoursOrMinutesDiff;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 根据出生日期获取年龄
     *
     * @param dateOfBirth 出生日期
     * @return
     */
    public static String getAgeByBirth(String dateOfBirth) {
        Date birthDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthDate = sdf.parse(dateOfBirth);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (birthDate != null) {
            now.setTime(new Date());
            born.setTime(birthDate);
            if (born.after(now)) {
                return "";
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        if (age > 0 && age < 100) {
            return String.valueOf(age);
        } else {
            return "";
        }
    }

    /**
     * 获取往后几天日期
     *
     * @param day 往后几天
     * @return
     */
    public static String getAfterDate(String date, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);//本月份的天数
            calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH + day);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 获取往前几天日期
     *
     * @param date
     * @param day  往前几天
     * @return
     */
    public static String getBeforeDate(Date date, int day) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        date = calendar.getTime();
        return sf.format(date);
    }

    /***
     * 返回指定时间段的所有年份/月份
     * @param beginDate 起始时间
     * @param endDate 结束时间
     * @return
     */
    public static List<String> getDatesBetweenTwoDate(String beginDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        List<String> lDate = new ArrayList<String>();
        lDate.add(beginDate);//把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        try {
            cal.setTime(sdf.parse(beginDate));
            boolean bContinue = true;
            while (bContinue) {
                //根据日历的规则，为给定的日历字段添加或减去指定的时间量
                cal.add(Calendar.MONTH, 1);
                // 测试此日期是否在指定日期之后
                if (sdf.parse(endDate).after(cal.getTime())) {
                    lDate.add(sdf.format(cal.getTime()));
                } else {
                    break;
                }
            }
            lDate.add(endDate);//把结束时间加入集合
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lDate;
    }

    /**
     * 将指定字符串格式时间转换成 Calendar对象
     *
     * @param dateStr
     * @return
     */
    public static Calendar getCalendar(String dateStr) {
        // 将字符串转换后的Calender对象
        Calendar calendar = null;
        // 声明一个Date类型的对象
        Date date = null;
        // 声明格式化日期的对象
        SimpleDateFormat format = null;
        if (dateStr != null) {
            // 创建日期的格式化类型
            format = new SimpleDateFormat("yyyy年MM月");
            // 创建一个Calendar类型的对象
            calendar = Calendar.getInstance();
            // forma.parse()方法会抛出异常
            try {
                //解析日期字符串，生成Date对象
                date = format.parse(dateStr);
                // 使用Date对象设置此Calendar对象的时间
                calendar.setTime(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return calendar;
    }


}
