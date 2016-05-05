package com.tianyi.yw.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 * 
 * @author qinjun
 * @version 1.0
 * 
 */
public class StringUtil {
	public static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 判断给定字符串是否为�?
	 * 
	 * @param sourceString
	 *            待判断的字符�?
	 * @return true 空字符串，false 非空字符�?
	 */
	public static boolean isEmpty(String sourceString) {
		if (null == sourceString)
			return true;
		if (sourceString.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 去掉首尾空字�?
	 * 
	 * @param sourceString
	 *            待处理字符串
	 * @return 返回去掉首尾空字符后的字符串
	 */
	public static String trim(String sourceString) {
		if (null == sourceString) {
			return "";
		}
		return sourceString.trim();
	}

	/**
	 * 去掉左边空字�?
	 * 
	 * @param sourceString
	 *            待处理字符串
	 * @return 返回去掉左边空字符后的字符串
	 */
	public static String leftTrim(String sourceString) {
		if (null == sourceString) {
			return "";
		}
		String regEx = "^\\s{1,}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(sourceString);
		String result = mat.replaceAll("");
		return result;
	}

	/**
	 * 去掉右边空字�?
	 * 
	 * @param sourceString
	 *            待处理字符串
	 * @return 返回去掉右边空字符后的字符串
	 */
	public static String rightTrim(String sourceString) {
		if (null == sourceString) {
			return "";
		}
		String regEx = "\\s{1,}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(sourceString);
		String result = mat.replaceAll("");
		return result;
	}

	/**
	 * 判断邮件是否有效
	 * 
	 * @param emailString
	 *            待验证的email地址
	 * @return true 有效 ,false 无效
	 */
	public static boolean isEmail(String emailString) {
		if (isEmpty(emailString)) {
			return false;
		}
		String regEx = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(emailString);
		return mat.matches();
	}

	/**
	 * 将给定的字符串进行加�?
	 * 
	 * @param sourceString
	 *            待加密字符串
	 * @return MD5加密字符�?
	 */
	public static String toMD5(String sourceString) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		if (isEmpty(sourceString)) {
			return null;
		}
		byte[] md = md5.digest(sourceString.getBytes());
		char[] c = new char[md.length * 2];
		int k = 0;
		// 将每位都进行双字节加�?
		for (int i = 0; i < md.length; i++) {
			byte b = md[i];
			c[k++] = HEX_DIGITS[b >> 4 & 0xf];
			c[k++] = HEX_DIGITS[b & 0xf];
		}
		return new String(c);
	}

	/**
	 * 将日期装换为字符�?
	 * 
	 * @param date
	 *            待转换的日期�?pattern 日期格式
	 * @return 日期字符�?
	 */
	public static String dateToString(Date date, String pattern){
		if (null == date || isEmpty(pattern)) {
			return "1900-01-01";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将日期装换为字符�?
	 * 
	 * @param date
	 *            待转换的日期
	 * @return 日期字符�?
	 */
	public static String dateToString(Date date) {
		if (null == date) {
			return "1900-01-01";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 比较两个字符串�?是否相同
	 * 
	 * @param sourceString
	 *            源字�?
	 * @param targetString
	 *            目标字符�?
	 * @return true 相同,false 不同
	 */
	public static boolean matche(String sourceString, String targetString) {
		if (null == sourceString || null == targetString) {
			return false;
		}
		if (sourceString.length() != targetString.length()) {
			return false;
		}
		if (sourceString.equals(targetString)) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串转�?
	 * 
	 * @param sourceString
	 *            源字符串
	 * @param targerCharset
	 *            目标字符�?UTF-8,GBK,GB2312,ISO-8859-1)
	 * @return 返回转码后的字符
	 * 
	 * @exception UnsupportedEncodingException 你的编码不支持�?可能是你给你的编码书写错误或者是其他编码异常�?
	 */
	public static String transformEncode(String sourceString,
			String targerCharset) throws UnsupportedEncodingException {
		if (isEmpty(sourceString) || isEmpty(sourceString)) {
			return null;
		}
		String result;
		result = new String(sourceString.getBytes(targerCharset), targerCharset);
		return result;
	}

	/**
	 * 判断是否是有效的手机�?br>
	 * &nbsp;&nbsp;移动�?34�?35�?36�?37�?38�?39�?50�?51�?57(TD)�?58�?59�?87�?88 <br>
	 * &nbsp;&nbsp;联�?�?30�?31�?32�?52�?55�?56�?85�?86<br>
	 * &nbsp;&nbsp;电信�?33�?53�?80�?89、（1349卫�?�?
	 * 
	 * @param number
	 *            待验证的手机�?
	 * @return true 有效，false 无效
	 */
	public static boolean isMobileNumber(String number) {
		if (isEmpty(number)) {
			return false;
		}
		String regEx = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(number);
		return mat.matches();
	}

	/**
	 * 判断是否是有效的ip
	 * 
	 * @param ip
	 *            待验证的ip
	 * @return true 有效，false 无效
	 */
	public static boolean isIp(String ip) {
		if (isEmpty(ip)) {
			return false;
		}
		String regEx = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(ip);
		return mat.matches();
	}
}
