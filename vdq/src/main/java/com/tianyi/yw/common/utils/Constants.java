/**
 * copy right 2012 sctiyi all rights reserved
 * create time:下午03:08:21
 * author:ftd
 */
package com.tianyi.yw.common.utils;


/**
 * @author ftd
 *
 */
public class Constants {

	/**
	 * 系统默认管理员账户
	 */
	public static final String ADMINISTRATOR_ACCOUNT="admin";
	
	public static final String USER_SESSION_NAME = "userInfo";
	public static final String USER_SESSION_FUNCTION = "userFunctions";
	public static final String CURRENT_MENU_ID = "__currentMenuId";
	
	public static final String THE_REALM_NAME="userRealm";
	public final static  String USER_INFO = "USER_INFO";
	
	public static final int IMAGE_RESIZE_WIDTH = 150;
	public static final int IMAGE_RESIZE_HEIGHT = 150;

	public static final int DEFAULT_PAGE_SIZE = 10;
	/**
	 * api接口返回状�?公共码表 start
	 */
	public static final Integer API_RESULT_SUCCESS = 0;
	public static final Integer API_RESULT_FAILURE = 1;
	public static final Integer API_RESULT_TIMEOUT = 95;
	public static final Integer API_RESULT_SUBMIT_DUPLICATE = 96;
	public static final Integer API_RESULT_PARAMTER_ERROR = 97;
	public static final Integer API_RESULT_TOKEN_ERROR = 98;
	public static final Integer API_RESULT_ORTHER_ERROR = 99;
	/**
	 * api接口返回状�?公共码表 end
	 */
	
	/**
	 * 数据状�?，启用�?停用
	 */
	public static final Integer STATUS_ENABLE = 0;
	public static final Integer STATUS_DISABLE = 1;
	
	/**
	 * 最新汇报时间段
	 */
	
	public static final Integer LATEST_TIME =10;
	
	/** openfire可配置*/
	public final static String BLOWFISHCODE = PropertiesUtil.getInstance().getParamsProperty("BLOWFISHCODE"); 
	public final static String OPENFIRE_IP = PropertiesUtil.getInstance().getParamsProperty("OPENFIRE_IP");  
	public final static String OPENFIRE_DOMAIN = PropertiesUtil.getInstance().getParamsProperty("OPENFIRE_DOMAIN"); 
	public final static String APK_VERSION = PropertiesUtil.getInstance().getParamsProperty("APK_VERSION"); 
	public final static String INIT_PASSWORD = "111111";
}
