package cn.comtom.core.main.sichuan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * DateUtil 时间处理帮助类
 * 
 * @author gooton-胡长明 2012-06-21
 */
public class DateUtil
{
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY_MM_DD = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	/*
	 * public static SimpleDateFormat getDF_YYYY_MM_DD(){ return
	 * DF_YYYY_MM_DD.get(); };
	 */

	public static final ThreadLocal<SimpleDateFormat> DF_YYYYMMDD = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyyMMdd");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY_MM_DD_CHN = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy年MM月dd日");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY_MM_DD_HH_MM_SS = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DFYYYYMMDDHHMMSS = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY_MM_DD_HH_MM = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY_MM = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_MM = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("MM");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYYMM = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyyMM");
		}
	};
	public static final ThreadLocal<SimpleDateFormat> DF_YYYY_MM_CHN = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy年MM月");
		}
	};

	public static final ThreadLocal<SimpleDateFormat> DF_MM_DD = new ThreadLocal<SimpleDateFormat>()
	{
		protected synchronized SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("MM-dd");
		}
	};

	private static DateUtil instance;

	public static DateUtil getInstance()
	{
		if (instance == null)
		{
			instance = new DateUtil();
		}
		return instance;
	}

	public String formateDate(Date date)
	{
		return this.formateDate(date, DF_YYYY_MM_DD.get());
	}

	/**
	 * 按指定的格式进行日期格式化
	 * 
	 * @param value
	 * @param formate
	 * @return String
	 */
	public String formateDate(Date date, SimpleDateFormat formate)
	{
		if (date == null)
		{
			return "";
		} else
		{
			return formate.format(date);
		}
	}

	/**
	 * 格式化成NUMBER
	 * 
	 * @param date
	 * @return
	 */
	public Long formateDateToNum(Date date)
	{
		if (date == null)
		{
			return null;
		} else
		{
			return Long.parseLong(DF_YYYYMMDD.get().format(date));
		}
	}

	/**
	 * 格式化成NUMBER
	 * 
	 * @param date
	 * @return
	 */
	public Long formateDateToNum(Date date, SimpleDateFormat formate)
	{
		if (date == null)
		{
			return null;
		} else
		{
			return Long.parseLong(formate.format(date));
		}
	}

	/*
	 * public Date truncateDate(Date date){ return DateUtils.truncate(date,
	 * Calendar.); }
	 */

	/**
	 * 将STRING转换成DATE
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public Date parseDate(String date) throws ParseException
	{
		if (StringUtils.isNotBlank(date))
		{
			return DF_YYYY_MM_DD.get().parse(date);
		} else
		{
			return null;
		}
	}

	/**
	 * 将STRING转换成DATE
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public Date parseDate(String date, SimpleDateFormat formate)
	{
		if (StringUtils.isBlank(date))
		{
			return null;
		}
		try
		{
			return formate.parse(date);
		} catch (ParseException e)
		{
			return null;
		}
	}

	public Date addHours(Date date, int amount)
	{
		if (date == null)
		{
			return null;
		} else
		{
			return DateUtils.addHours(date, amount);
		}
	}

	public Date addMinute(Date date, int amount)
	{
		if (date == null)
		{
			return null;
		} else
		{
			return DateUtils.addMinutes(date, amount);
		}
	}

	/**
	 * 日期
	 * 
	 * @param value
	 * @param amount
	 * @return
	 */
	public Date addDays(Date date, int amount)
	{
		if (date == null)
		{
			return null;
		} else
		{
			return DateUtils.addDays(date, amount);
		}
	}

	/**
	 * 加月
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public Date addMonths(Date date, int amount)
	{
		if (date == null)
		{
			return null;
		} else
		{
			return DateUtils.addMonths(date, amount);
		}
	}

	/**
	 * 转换java.util.date-->java.sql.date
	 * 
	 * @param d
	 * @return
	 */
	public java.sql.Date convertUtilDate(Date d)
	{
		return new java.sql.Date(d.getTime());
	}

	/**
	 * 比较两个日期大小
	 * 
	 * @param da
	 * @param db
	 * @return -1: da < db 0 : da = db 1 : da > db -9: 同时为空
	 */
	public int compareDate(Date da, Date db)
	{
		if (da == null && db == null)
		{
			return -9;
		} else if (da != null && db == null)
		{
			return 1;
		} else if (da == null && db != null)
		{
			return -1;
		} else
		{
			Calendar ca = Calendar.getInstance();
			ca.setTime(da);
			Calendar cb = Calendar.getInstance();
			cb.setTime(db);
			return compareDate(ca, cb);
		}
	}

	/**
	 * 
	 * @param ca
	 * @param cb
	 * @return
	 */
	private int compareDate(Calendar ca, Calendar cb)
	{
		if (ca.before(cb))
		{
			return -1;
		} else if (ca.after(cb))
		{
			return 1;
		} else if (ca.equals(cb))
		{
			return 0;
		} else
		{
			// 不会发生，忽略
			return -9;
		}
	}

	/**
	 * 获取天
	 * 
	 * @param d
	 * @return
	 */
	public int getDay(Date d)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		return ca.get(Calendar.DATE);
	}

	public int getDay()
	{
		return getDay(new Date());
	}

	/**
	 * 获得当前时间
	 * 
	 * @param parrten
	 *            输出的时间格式
	 * @return 返回时间
	 */
	public static String getTime(String parrten)
	{
		String timestr;
		if (parrten == null || parrten.equals(""))
		{
			parrten = "yyyyMMddHHmmss";
		}
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(parrten);
		java.util.Date cday = new Date();
		timestr = sdf.format(cday);
		return timestr;
	}

	/**
	 * 获取月
	 * 
	 * @param d
	 * @return
	 */
	public int getMonth(Date d)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		return ca.get(Calendar.MONTH) + 1;
	}

	public int getMonth()
	{
		return getMonth(new Date());
	}

	/**
	 * 获取年
	 * 
	 * @param d
	 * @return
	 */
	public int getYear(Date d)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(d);
		return ca.get(Calendar.YEAR);
	}

	public int getYear()
	{
		return getYear(new Date());
	}

	/**
	 * 是否是同一个月
	 * 
	 * @param da
	 * @param db
	 * @return
	 */
	public boolean isSameMonth(Date da, Date db)
	{
		if (da != null && db != null)
		{
			Calendar ca = Calendar.getInstance();
			ca.setTime(da);
			Calendar cb = Calendar.getInstance();
			cb.setTime(db);
			return (ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR)) && (ca.get(Calendar.MONTH) == cb.get(Calendar.MONTH));
		} else
		{
			return false;
		}
	}

	/**
	 * 
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * 
	 * @param date2
	 * 
	 * @return
	 */

	public static int getdaysBetween(Date date1, Date date2)

	{

		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		long time1 = cal.getTimeInMillis();

		cal.setTime(date2);

		long time2 = cal.getTimeInMillis();

		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));

	}

	/**
	 * 
	 * 计算两个日期之间相差的分钟
	 * 
	 * @param date1
	 * 
	 * @param date2
	 * 
	 * @return
	 */

	public static int getminutesBetween(Date date1, Date date2)

	{

		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		long time1 = cal.getTimeInMillis();

		cal.setTime(date2);

		long time2 = cal.getTimeInMillis();

		long between_days = (time2 - time1) / (1000 * 60);

		return Integer.parseInt(String.valueOf(between_days));

	}

	/**
	 * 
	 * 计算两个日期之间相差的秒数
	 * 
	 * @param date1
	 * 
	 * @param date2
	 * 
	 * @return
	 */

	public static int getsecondsBetween(Date date1, Date date2)

	{

		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		long time1 = cal.getTimeInMillis();

		cal.setTime(date2);

		long time2 = cal.getTimeInMillis();

		long between_days = (time2 - time1) / 1000;

		return Integer.parseInt(String.valueOf(between_days));

	}
    /** 
     * 根据年月获取对应的月份 天数 
     * */  
    public static int getDaysByYearMonth(int year, int month) {  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    } 

    /**
     * 获取某月所有日期
     * @param date
     * @return
     */
    public static List<Date> getAllTheDateOftheMonth(Date date) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		int month = cal.get(Calendar.MONTH);
		while(cal.get(Calendar.MONTH) == month){
			list.add(cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}
    
	/**
	 * 获取最近12个月
	 * @return
	 */
    public static String[] getLast12Months(){  
        String[] last12Months = new String[12];  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        //cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1); //要先+1,才能把本月的算进去</span>  
        for(int i=0; i<12; i++){  
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月  
            last12Months[i] = cal.get(Calendar.YEAR)+ "-" + fillZero((cal.get(Calendar.MONTH)+1)+"", 2);  
        }  
          
        return last12Months;  
    } 
    /**
     * 右对齐左补零
     * @param str
     * @param len
     * @return
     */
    public static String fillZero(String str, int len) {
    	int tmp = str.length();         
    	int t;          
    	String str1 = str;         
    	if(tmp >= len)           
    		return str1;         
    	t = len - tmp;          
    	for(int i = 0; i < t; i++ )
    	str1 = "0" + str1;         
    	return str1;      
    }
    
    /**
     * 获得两个日期之间的所有月份
     * 
     * @param minDate
     * @param maxDate
     * @return
     * @throws ParseException
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
         result.add(sdf.format(curr.getTime()));
         curr.add(Calendar.MONTH, 1);
        }

        return result;
      }
    
    /**
     * 获取当前月第一天
     * @return
     */
    public static Date getCurrentMonthFirstDay() {  
    	Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return c.getTime();  
    } 
    
    /**
     * 获取日期之间天数
     * 
     * @param now
     * @param returnDate
     * @return
     */
    public static int daysBetween(Date beginDate, Date endDate) {
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginDate);
		setTimeToMidnight(begin);
		long beginMs = begin.getTimeInMillis();
		
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		setTimeToMidnight(end);
		long endMs = end.getTimeInMillis();
		
		long intervalMs = endMs - beginMs;
		return millisecondsToDays(intervalMs);
	}
    public static void setTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
	}
    public static int millisecondsToDays(long intervalMs) {
		return (int) (intervalMs / (1000 * 86400));
	}
    
    /**
     * 获取某月的最后一天
     * @return
     */
    public static Date getLastDayForMonth(Date date) {  
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	Date firstDayOfMonth = calendar.getTime();  
    	calendar.add(Calendar.MONTH, 1);
    	calendar.add(Calendar.DAY_OF_MONTH, -1);
    	Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth;  
    }
    
	/**
	 * 得到去年的年份
	 * @return
	 */
	public static String getLastYear() {
		Date thisYear = new Date();
		Calendar c = Calendar.getInstance();
        c.setTime(thisYear);
        c.add(Calendar.YEAR, -1);
        Date lastYear = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(lastYear);
	}
}