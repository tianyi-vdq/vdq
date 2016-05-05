package com.tianyi.yw.common.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	private static PropertiesUtil propertiesUtil;
	 
	 public static PropertiesUtil getInstance() {
		 if(propertiesUtil==null){
			 propertiesUtil = new PropertiesUtil(); 
		 }
		  return propertiesUtil;
	} 

	public String getParamsProperty(String name) {

		Properties p = new Properties();

		try {
			p.load(getClass().getClassLoader().getResourceAsStream("params.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String param = p.getProperty(name);
		System.out.println("param:::::"+param);
		return param;
	}

	
	 
	public static void main(String[] args) {

		//getInstance().setParamsProperty("AUTO_NO","0002");
		System.out.println(getInstance().getParamsProperty("AUTO_NO"));
		//System.out.println(getInstance().getParamsProperty("OPENFIRE_IP"));
//		System.out.println(getInstance().getParamsProperty("VIDEO_ACCESSIP"));
//		System.out.println(getInstance().getParamsProperty("VIDEO_ACCESSPORT"));
	}

}
