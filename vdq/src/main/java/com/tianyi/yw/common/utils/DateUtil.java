package com.tianyi.yw.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * 主要功能：常用日期处理类，包含日期格式化、日期比较和日期加减三类操作�?
 * <br>本类处理的日期对象均为java.util.Date实例对象，如果是其他Date类型对象，请转换后再使用�?
 * <br>本类�?��方法均为静�?方法，�?过类名直接调用�?
 * <br>常量介绍�?
 *         <br>DateUtil.MONDAY    星期�?
 *         <br>DateUtil.TUESDAY   星期�?
 *         <br>DateUtil.WEDNESDAY 星期�?
 *         <br>DateUtil.THURSDAY  星期�?
 *         <br>DateUtil.FRIDAY    星期�?
 *         <br>DateUtil.SATURDAY  星期�?
 *         <br>DateUtil.SUNDAY    星期�?
 * 
 * @author wzy 2013/06/19
 * 
 */
public final class DateUtil {
	
	private static Log logger = LogFactory.getLog(DateUtil.class);
	
	/** 星期�?*/
	public static final int MONDAY = 1;
	/** 星期�?*/
	public static final int TUESDAY = 2;
	/** 星期�?*/
	public static final int WEDNESDAY = 3;
	/** 星期�?*/
	public static final int THURSDAY =4;
	/** 星期�?*/
	public static final int FRIDAY = 5;
	/** 星期�?*/
	public static final int SATURDAY = 6;
	/** 星期�?*/
	public static final int SUNDAY = 7;
	
	/**
	 * 私有构�?。不允许创建本类对象�?
	 */
	private DateUtil(){
		
	}
	
	/**
	 * 获取传入日期的年�?
	 * @param date 日期
	 * @return 年份
	 */
	public static int getYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获取传入日期的月�?
	 * @param date 日期
	 * @return 月份
	 */
	public static int getMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取传入日期的天�?
	 * @param date 日期
	 * @return 天数
	 */
	public static int getDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 将传入的日期对象格式化输出�?
	 * @param date 日期对象
	 * @param pattern 希望生成的日期格�?
	 * @return 日期格式字符�?
	 */
	public static String format(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 将传入的日期对象格式化输出�?
	 * 默认输出格式为yyyy-MM-dd
	 * @param date 日期对象
	 * @return 日期格式字符�?
	 */
	public static String format(Date date){
		return format(date, "yyyy-MM-dd");
	}
	
	/**
	 * 将传入的日期字符串格式化输出�?
	 * @param date 能转换为日期的字符串
	 * @param patternSrc 原日期字符串格式
	 * @param patternOut 希望生成的日期格�?
	 * @return 日期格式字符�?
	 * @throws ParseException 如果传入的date字符串不能转换成java.util.Date对象，则抛出该异�?
	 */
	public static String format(String date, String patternSrc, String patternOut) throws ParseException{
		return format(parse(date, patternSrc), patternOut);
	}
	
	/**
	 * 将字符串转换成日期对�?
	 * @param str 日期字符�?
	 * @param pattern 该字符串的格�?
	 * @return 日期格式字符�?
	 * @throws ParseException 如果传入的str字符串不能转换成java.util.Date对象，则抛出该异�?
	 */
	public static Date parse(String str, String pattern) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(str);
	}
	
	/**
	 * 获取两个日期之间间隔的天数�?
	 * 该间隔天数为自然天�?
	 * 例：
	 *     2013-06-01 23:59:59�?013-06-02 00:00:00的间隔天数为1天�?（实际间隔时间是1秒）
	 * @param min 较小的日期对�?
	 * @param max 较大的日期对�?
	 * @return 整数天数（如果为负数，则说明参数1大于参数2�?
	 */
	public static int getBetweenDays(Date min, Date max){
		double temp = 0;
		//计算相差天数，忽略时分秒
		try {
			Date mx  = parse(format(max), "yyyy-MM-dd");
			Date mi = parse(format(min), "yyyy-MM-dd");
			temp = mx.getTime() - mi.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int)Math.ceil(temp / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * 获取两个日期之间间隔的月数�?
	 * 该间隔月数为自然月�?
	 * 例：
	 *     2013-06-30�?013-07-01的间隔月数为1月�?（实际间隔时间是1天）
	 * @param min 较小的日期对�?
	 * @param max 较大的日期对�?
	 * @return 整数月数（如果为负数，则说明参数1大于参数2�?
	 */
	public static int getBetweenMonths(Date min, Date max){
		Calendar calMin = Calendar.getInstance();
		calMin.setTime(min);
		Calendar calMax = Calendar.getInstance();
		calMax.setTime(max);
		int month1 = calMin.get(Calendar.YEAR) * 12 + calMin.get(Calendar.MONTH) + 1;
		int month2 = calMax.get(Calendar.YEAR) * 12 + calMax.get(Calendar.MONTH) + 1;
		
		return month2 - month1;
	}
	
	/**
	 * 获取两个日期之间间隔的年数�?
	 * 该间隔年数为自然年�?
	 * 例：
	 *     2012-12-31�?013-01-01的间隔年数为1年�?（实际间隔时间是1天）
	 * @param min 较小的日期对�?
	 * @param max 较大的日期对�?
	 * @return 整数年数（如果为负数，则说明参数1大于参数2�?
	 */
	public static int getBetweenYears(Date min, Date max){
		return getYear(max) - getYear(min);
	}
	
	/**
	 * 获取两个日期之间间隔的周数�?
	 * 该间隔周数为自然周，并且默认是以周一为周的起始日�?
	 * 例：
	 *     上周周日到本周周�?��隔为1周�?（实际间隔时间是1天）
	 * @param min 较小的日期对�?
	 * @param max 较大的日期对�?
	 * @return 整数周数（如果为负数，则说明参数1大于参数2�?
	 */
	public static int getBetweenWeeks(Date min, Date max){
		return getBetweenWeeks(min, max, MONDAY);
	}
	
	/**
	 * 获取两个日期之间间隔的周数�?
	 * 该间隔周数为自然�?
	 * 例：
	 *     上周周日到本周周�?��隔为1周�?（实际间隔时间是1天）
	 * @param min 较小的日期对�?
	 * @param max 较大的日期对�?
	 * @param value 表示星期几作为一周的�?��
	 * @return 整数周数（如果为负数，则说明参数1大于参数2�?
	 */
	public static int getBetweenWeeks(Date min, Date max, int value){
		Calendar calMin = Calendar.getInstance();
		Calendar calMax = Calendar.getInstance();
		boolean flag = false;
		if(min.getTime() > max.getTime()){
			calMax.setTime(min);
			calMin.setTime(max);
			flag = true;
		}else{
			calMax.setTime(max);
			calMin.setTime(min);
		}
		int minWeekDay = getDayOfWeek(calMin.getTime(), value);
		
		Date firstWeekEnd = addDays(calMin.getTime(), 7 - minWeekDay);
		
		if(getBetweenDays(firstWeekEnd, calMax.getTime()) > 0){
			Date tempDate = addDays(firstWeekEnd, 1);
			int count = 0;
			while(getBetweenDays(tempDate, calMax.getTime()) >= 0){
				count++;
				tempDate = addDays(tempDate, 7);
			}
			if(flag){
				count = count * -1;
			}
			return count;
		}else{
			return 0;
		}
	}
	
	/**
	 * 获取传入的Date日期在一周中处于第几�?
	 * @param date 日期对象
	 * @param value 表示星期几作为一周的�?��
	 * @return 第几�?
	 */
	public static int getDayOfWeek(Date date,int value){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int temp = cal.get(Calendar.DAY_OF_WEEK) - value;
		if(temp < 1){
			temp += 7;
		}
		return temp;
	}
	
	/**
	 * 在当前日期上修改天数，并将修改后的日期对象返回�?
	 * @param date 待修改的日期对象
	 * @param days 如果值为负，则减少相应天数�?
	 * @return 修改后的日期对象
	 */
	public static Date addDays(Date date, int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	/**
	 * 在当前日期上修改月数，并将修改后的日期对象返回�?
	 * @param date 待修改的日期对象
	 * @param months 如果值为负，则减少相应月数�?
	 * @return 修改后的日期对象
	 */
	public static Date addMonths(Date date, int months){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}
	
	/**
	 * 在当前日期上修改小时数，并将修改后的日期对象返回�?
	 * @param date 待修改的日期对象
	 * @param hours 如果值为负，则减少相应小时数�?
	 * @return 修改后的日期对象
	 */
	public static Date addHours(Date date, int hours){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hours);
		return cal.getTime();
	}
	
	/**
	 * 在当前日期上修改分钟数，并将修改后的日期对象返回�?
	 * @param date 待修改的日期对象
	 * @param minutes 如果值为负，则减少相应分钟数�?
	 * @return 修改后的日期对象
	 */
	public static Date addMinutes(Date date, int minutes){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	
	
	/**
	 * 
	 * @param formatType 日期字符格式
	 * @return 返回相应的字符格式串，默认格式为yyyy-MM-dd HH:mm:ss
	 */
	private static String getFormatStr(int formatType) {
		String formatStr = null;
		switch (formatType) {
		case 1:
			formatStr = "yyyy-MM-dd";
			break;
		case 2:
			formatStr = "yyyy-MM-dd HH:mm:ss";
			break;
		case 3:
			formatStr = "yyyy-MM-dd HH:mm:ss SSS";
			break;
		case 4:
			formatStr = "yyyyMMdd";
			break;
		case 5:
			formatStr = "yyyyMMddHHmmss";
			break;
		case 6:
			formatStr = "yyyyMMddHHmmssSSS";
			break;
		case 7:
			formatStr = "yyyyMM";
			break;
		case 8:
			formatStr = "yyyy";
			break;
		case 9:
			formatStr = "MM";
			break;
		case 10:
			formatStr = "dd";
			break;
		case 11:
			formatStr = "HHmmssSSS";
			break;
		default:
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		return formatStr;
	}

	public static long getLongFromStr(String timeStr, int formatType) {
		long time = 0L;
		String formatStr = getFormatStr(formatType);
		if ((timeStr != null) && (timeStr.length() > 0)) {
			DateFormat df = new SimpleDateFormat(formatStr);
			try {
				time = df.parse(timeStr).getTime();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return time;
	}

	public static long getLongFromLong(long timeLong, int formatType) {
		long time = 0L;
		if (timeLong > 0L) {
			String formatStr = getFormatStr(formatType);
			DateFormat df = new SimpleDateFormat(formatStr);
			time = Long.parseLong(df.format(Long.valueOf(timeLong)));
		}
		return time;
	}

	/**
	 * 将指定的时间转换为相应的时间字符�?
	 * 
	 * @param timeLong
	 * @param formatType
	 * @return
	 */
	public static String getStringTime(long timeLong, int formatType) {
		String time = "";
		if (timeLong > 0L) {
			String formatStr = getFormatStr(formatType);
			DateFormat df = new SimpleDateFormat(formatStr);
			time = df.format(Long.valueOf(timeLong));
		}
		return time;
	}

	public static long convertTime4Long(String stime, int formatType)
			throws Exception {
		Date date = null;
		DateFormat df = new SimpleDateFormat(getFormatStr(formatType));
		try {
			date = df.parse(stime);
		} catch (Exception e) {
			throw new Exception();
		}
		return date.getTime();
	}

	public static String convertTimeString(long time) {
		String s = String.valueOf(time);
		return s.substring(0, 4) + "-" + s.substring(4, 6) + "-"
				+ s.substring(6, 8) + " " + s.substring(8, 10) + ":"
				+ s.substring(10, 12) + ":" + s.substring(12, 14);
	}

	public static String getMonth(int month) {
		Calendar strDate = Calendar.getInstance();
		strDate.add(2, month);
		return getStringTime(strDate.getTime().getTime(), 7);
	}

	public static String getYearOrMonthOrDay(int formatType) {
		Calendar ca = Calendar.getInstance();
		String time = "";
		switch (formatType) {
		case 1:
			time = String.valueOf(ca.get(Calendar.YEAR));
			break;
		case 2:
			time = String.valueOf(ca.get(Calendar.MONTH));
			break;
		case 3:
			time = String.valueOf(ca.get(Calendar.DATE));
			break;
		default:
			break;
		}
		return time;
	}

	public static String getCurrentTimeToString(int styleType) {
		// 建议写成String形式写入
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(getFormatStr(styleType));
		String dateStr = format.format(date);

		return dateStr;
	}

	/**
	 * 获取当前时间几月前或几月后的时间
	 * 
	 * @param month
	 * @param formatStyle
	 * @return
	 */
	public static String getNextTime(int month, int formatStyle) {
		Calendar strDate = Calendar.getInstance();
		strDate.add(2, month);
		return getStringTime(strDate.getTime().getTime(), formatStyle);
	}

	public static String getNextMinute(int minute, int formatStyle) {
		Calendar strDate = Calendar.getInstance();
		strDate.add(Calendar.MINUTE, minute);
		return getStringTime(strDate.getTime().getTime(), formatStyle);
	}

	public static Date getNextMinute(int minute) {
		Calendar strDate = Calendar.getInstance();
		strDate.add(Calendar.MINUTE, minute);
		return strDate.getTime();
	}

	public static void main(String[] args) {
		int month = Calendar.getInstance().get(2) + 1;
		System.out.println(month);
		month = 4;
		logger.info("\n***********" + getNextTime(-month, 4));
	}
}
