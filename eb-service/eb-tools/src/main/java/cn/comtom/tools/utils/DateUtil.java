package cn.comtom.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日期操作辅助类
 * 
 * @author zhucanhui
 */
public final class DateUtil {
	private DateUtil() {
	}

	/** 日期格式 **/
	public interface DATE_PATTERN {
		String HHMMSS = "HHmmss";
		String HH_MM_SS = "HH:mm:ss";
		String YYYYMMDD = "yyyyMMdd";
		String YYYY_MM_DD = "yyyy-MM-dd";
		String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
		String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
		String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
		String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return String
	 */
	public static final String format(Object date) {
		return format(date, DATE_PATTERN.YYYY_MM_DD);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static final String format(Object date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null) {
			return format(date);
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 获取日期
	 * 
	 * @return String
	 */
	public static final String getDate() {
		return format(new Date());
	}

	/**
	 * 获取日期时间
	 * 
	 * @return String
	 */
	public static final String getDateTime() {
		return format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取日期
	 * 
	 * @param pattern
	 * @return String
	 */
	public static final String getDateTime(String pattern) {
		return format(new Date(), pattern);
	}

	/**
	 * 日期计算
	 * 
	 * @param date
	 * @param field
	 * @param amount
	 * @return Date
	 */
	public static final Date addDate(Date date, int field, int amount) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 字符串转换为日期:不支持yyM[M]d[d]格式
	 * 
	 * @param date
	 * @return Date
	 */
	public static final Date stringToDate(String date) {
		if (date == null) {
			return null;
		}
		String separator = String.valueOf(date.charAt(4));
		String pattern = "yyyyMMdd";
		if (!separator.matches("\\d*")) {
			pattern = "yyyy" + separator + "MM" + separator + "dd";
			if (date.length() < 10) {
				pattern = "yyyy" + separator + "M" + separator + "d";
			}
		} else if (date.length() < 8) {
			pattern = "yyyyMd";
		}
		pattern += " HH:mm:ss.SSS";
		pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 间隔天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Integer
	 */
	public static final Integer getDayBetween(Date startDate, Date endDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MILLISECOND, 0);

		long n = end.getTimeInMillis() - start.getTimeInMillis();
		return (int) (n / (60 * 60 * 24 * 1000l));
	}

	/**
	 * 间隔月
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Integer
	 */
	public static final Integer getMonthBetween(Date startDate, Date endDate) {
		if (startDate == null || endDate == null || !startDate.before(endDate)) {
			return null;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int year1 = start.get(Calendar.YEAR);
		int year2 = end.get(Calendar.YEAR);
		int month1 = start.get(Calendar.MONTH);
		int month2 = end.get(Calendar.MONTH);
		int n = (year2 - year1) * 12;
		n = n + month2 - month1;
		return n;
	}

	/**
	 * 间隔月，多一天就多算一个月
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Integer
	 */
	public static final Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
		if (startDate == null || endDate == null || !startDate.before(endDate)) {
			return null;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		int year1 = start.get(Calendar.YEAR);
		int year2 = end.get(Calendar.YEAR);
		int month1 = start.get(Calendar.MONTH);
		int month2 = end.get(Calendar.MONTH);
		int n = (year2 - year1) * 12;
		n = n + month2 - month1;
		int day1 = start.get(Calendar.DAY_OF_MONTH);
		int day2 = end.get(Calendar.DAY_OF_MONTH);
		if (day1 <= day2) {
			n++;
		}
		return n;
	}


	public static Boolean checkConflict(Date startTime , Date endTime , List<TimeMatcher> list){
		if((startTime == null && endTime == null) || list == null || list.isEmpty()){
			return false;
		}
		List<TimeMatcher> timeMatcherList = Optional.of(list).orElse(Collections.emptyList()).stream()
				.filter(Objects::nonNull)
				.filter(timeMatcher -> !(timeMatcher.getEndTime() == null && timeMatcher.getStartTime() == null))
				.filter(timeMatcher -> {
					if(startTime == null){
						if(endTime.after(timeMatcher.getStartTime())){
							return true;
						}else{
							return false;
						}
					}
					if(endTime == null){
						if(startTime.before(timeMatcher.getEndTime()) ){
							return true;
						}else{
							return false;
						}
					}
					if((endTime.after(timeMatcher.getEndTime()) && startTime.before(timeMatcher.getEndTime()))
							||(endTime.after(timeMatcher.getStartTime()) && endTime.before(timeMatcher.getEndTime())) ){
						return true;
					}
					return  false;
				}).collect(Collectors.toList());
		return !timeMatcherList.isEmpty();
	}
	
	/**
	  获取精确到秒的时间戳 
	  @param date 
	  @return 
	 */  
	public static int getSecondTimestamp(Date date){  
	    if (null == date) {  
	        return 0;  
	    }  
	    String timestamp = String.valueOf(date.getTime()/1000);  
	    return Integer.valueOf(timestamp);  
	}  
}
