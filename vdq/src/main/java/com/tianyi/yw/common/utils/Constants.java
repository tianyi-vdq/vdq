/**
 * copy right 2012 sctiyi all rights reserved
 * create time:涓嬪崍03:08:21
 * author:ftd
 */
package com.tianyi.yw.common.utils;


/**
 * @author ftd
 *
 */
public class Constants {

	/**
	 * 绯荤粺榛樿绠＄悊鍛樿处鎴�	 */
	public static final String ADMINISTRATOR_ACCOUNT="admin";
	
	public static final String USER_SESSION_NAME = "userInfo";
	public static final String USER_SESSION_FUNCTION = "userFunctions";
	public static final String CURRENT_MENU_ID = "__currentMenuId";
	
	public static final String THE_REALM_NAME="userRealm";
	public final static  String USER_INFO = "USER_INFO";
	
	public static final int IMAGE_RESIZE_WIDTH = 150;
	public static final int IMAGE_RESIZE_HEIGHT = 150;

	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAULT_QUEUE_SIZE = 1;
	/**
	 * api鎺ュ彛杩斿洖鐘讹拷?鍏叡鐮佽〃 start
	 */
	public static final Integer API_RESULT_SUCCESS = 0;
	public static final Integer API_RESULT_FAILURE = 1;
	public static final Integer API_RESULT_TIMEOUT = 95;
	public static final Integer API_RESULT_SUBMIT_DUPLICATE = 96;
	public static final Integer API_RESULT_PARAMTER_ERROR = 97;
	public static final Integer API_RESULT_TOKEN_ERROR = 98;
	public static final Integer API_RESULT_ORTHER_ERROR = 99;
	/**
	 * api鎺ュ彛杩斿洖鐘讹拷?鍏叡鐮佽〃 end
	 */
	
	/**
	 * 鏁版嵁鐘讹拷?锛屽惎鐢拷?鍋滅敤
	 */
	public static final Integer STATUS_ENABLE = 0;
	public static final Integer STATUS_DISABLE = 1;

	public static final Integer CHECK_RESULT_NORMAL = 3;
	public static final Integer CHECK_RESULT_EXCEPTION = 1;
	public static final Integer CHECK_RESULT_FAIL = 4;
	public static final Integer CHECK_RESULT_WARN = 2;
	/**
	 * 鏈�柊姹囨姤鏃堕棿娈�	 */
	
	public static final Integer LATEST_TIME =10;
	
	/** openfire鍙厤缃�*/
	public final static String BLOWFISHCODE = PropertiesUtil.getInstance().getParamsProperty("BLOWFISHCODE"); 
	public final static String OPENFIRE_IP = PropertiesUtil.getInstance().getParamsProperty("OPENFIRE_IP");  
	public final static String OPENFIRE_DOMAIN = PropertiesUtil.getInstance().getParamsProperty("OPENFIRE_DOMAIN"); 
	public final static String APK_VERSION = PropertiesUtil.getInstance().getParamsProperty("APK_VERSION"); 
	public final static String INIT_PASSWORD = "111111";
	public static final String ROUTEDATA_YWALARM_VIDEO = "routeData.ywAlarm.video";//视频诊断故障 
	public static final String ROUTEDATA_YWALARM_VIDEO_CODE = "109015";//视频诊断故障编码
	public static final String ROUTEDATA_YWALARM_VIDEO_TYPE = "B1";//视频诊断故障 类型
	public static final String ROUTEDATA_YWALARM_VIDEO_IP = "25.30.9.244";

}
