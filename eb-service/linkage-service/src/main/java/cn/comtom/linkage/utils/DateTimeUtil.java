package cn.comtom.linkage.utils;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式处理工具类<br>
 */
public final class DateTimeUtil
{
    private DateTimeUtil ()
    {

    }

    /**
     * 按照默认的格式转换时间，格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String dateToString (Date date)
    {
        return DateFormatUtils.format (date, DateStyle.YYYY_MM_DD_HH_MM_SS.getValue ());
    }

    /**
     * 按照指定格式转换时间 时间格式请参考DateStyle里面的定义
     * 
     * @param date
     * @param fromat
     * @return
     */
    public static String dateToString (Date date, String fromat)
    {
        return DateFormatUtils.format (date, fromat);
    }

    /**
     * 按照DateStyle里面定制的时间格式转换时间
     * 
     * @param date
     * @param dateStyle
     * @return
     */
    public static String dateToString (Date date, DateStyle dateStyle)
    {
        return DateFormatUtils.format (date, dateStyle.getValue ());
    }

    /**
     * 将时间日期字符串转换时间对象<br>
     * 自动从DateStyle里面寻找可以匹配的时间样式,如果没有匹配的则返回null<br>
     * 
     * @param dateString
     * @return
     */
    public static Date stringToDate (String dateString)
    {
        try
        {
            return DateUtils.parseDateStrictly (dateString, DateStyle.getAllStyle ());
        }
        catch (Exception e)
        {
            logger.error ("转换日期错误:" + dateString + SystemUtils.LINE_SEPARATOR + e.getMessage ());
            return null;
        }
    }

    /**
     * 将时间字符串按照指定的格式转换 ,如果格式不匹配返回null
     * 
     * @param dateString
     * @param format
     * @return
     */
    public static Date stringToDate (String dateString, String format)
    {
        try
        {
            return DateUtils.parseDateStrictly (dateString, new String[]
            { format });
        }
        catch (Exception e)
        {
            logger.error ("转换日期错误:" + dateString + SystemUtils.LINE_SEPARATOR + e.getMessage ());
            return null;
        }
    }

    /**
     * 获取日期的年份
     * 
     * @param date
     * @return
     */
    public static int getYear (Date date)
    {
        return getInterger (date, Calendar.YEAR);
    }

    /**
     * 获取日期的月份，在原有的月份加1<br>
     * 例如2014-5-22返回5
     * 
     * @param date
     * @return
     */
    public static int getMonth (Date date)
    {
        return getInterger (date, Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的年天数
     * 
     * @param date
     * @return
     */
    public static int getDayofYear (Date date)
    {
        return getInterger (date, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取日期的月份天数
     * 
     * @param date
     * @return
     */
    public static int getDayOfMoth (Date date)
    {
        return getInterger (date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期的星期天数,在原有的星期天数减1<br>
     * 例如星期一则返回1,星期天返回7
     * 
     * @param date
     * @return
     */
    public static int getDayOfWeek (Date date)
    {
        int value = getInterger (date, Calendar.DAY_OF_WEEK);
        if (value == 1)
        {
            return 7;
        }
        return value - 1;
    }

    /**
     * 获取日期的所在月份的第几周
     * 
     * @param date
     * @return
     */
    public static int getDayOfWeekInMonth (Date date)
    {
        return getInterger (date, Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获取日期的小时数，24小时制。
     * 
     * @param date
     * @return
     */
    public static int getHour (Date date)
    {
        return getInterger (date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期的分钟数。
     * 
     * @param date
     * @return
     */
    public static int getMinute (Date date)
    {
        return getInterger (date, Calendar.MINUTE);
    }

    /**
     * 获取日期的秒数。
     * 
     * @param date
     * @return
     */
    public static int getSecond (Date date)
    {
        return getInterger (date, Calendar.SECOND);
    }

    /**
     * 获取日期的毫秒数。
     * 
     * @param date
     * @return
     */
    public static int getMillisecond (Date date)
    {
        return getInterger (date, Calendar.MILLISECOND);
    }

    /**
     * 根据时间获取时间的某个部分，dateType为Calendar里面的常量
     * 
     * @param date
     * @param dateType
     * @return
     */
    private static int getInterger (Date date, int dateType)
    {
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date);
        return calendar.get (dateType);
    }

    /**
     * 判断年份是不是闰年
     * 
     * @param year
     * @return
     */
    public static boolean isLeapYear (int year)
    {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断日期是不是闰年
     * 
     * @param year
     * @return
     */
    public static boolean isLeapYear (Date date)
    {
        int year = getYear (date);
        return isLeapYear (year);
    }

    /**
     * @param date 日期
     * @param otherDate 另一个日期
     * @return 相差天数 为正数 毫秒时间忽略，不足一天为0
     */
    public static long getIntervalDays (Date date, Date otherDate)
    {
        long time = Math.abs (DateUtils.setMilliseconds (date, 0).getTime ()
                              - DateUtils.setMilliseconds (otherDate, 0).getTime ());
        return (long) time / (24 * 60 * 60 * 1000);
    }

  /*  public static void main (String[] args)
    {
        // Date date =new Date ();
        // String dateString=dateToString(date);
        // System.out.println (dateToString(date));
        // String[] formatArray=DateStyle.getAllStyle ();
        // for (String string : formatArray)
        // {
        // dateString=dateToString(new Date (),string);
        // System.out.println (dateString);
        // date=stringToDate (dateString);
        // System.out.println (dateToString(date));
        // date=stringToDate (dateString, string);
        // System.out.println (date);
        // }
    }*/

    /** 日志记录器. */
    private static Logger logger = LoggerFactory.getLogger (DateTimeUtil.class);

}
