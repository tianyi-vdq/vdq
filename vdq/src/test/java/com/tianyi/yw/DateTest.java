package com.tianyi.yw;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DateTest {
	public static void main(String[] args) throws ParseException {
		String s1 = "1990-01-24 15:00";
		String s2 = "2000-03-12 23:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date d1 = sdf.parse(s1);
		Date d2 = sdf.parse(s2);
		if(d1.getTime()>d2.getTime()){
			System.out.println("s1大于s2");
		}else if(d1.getTime()<d2.getTime()){
			System.out.println("s1小于s2");
		}else{
			System.out.println("s1等于s2");
		}
	}
}
