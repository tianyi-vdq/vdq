package com.tianyi.yw;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
public class DateTest {
	public static void main(String[] args) throws ParseException {
		Date d = new Date();
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long startTime = d.getTime();
		long endTime = d1.getTime();
		long result = (endTime - startTime);
		System.out.println(result);
	}
}
