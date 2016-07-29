package com.tianyi.yw.common.utils;
/**
 * @author lq
 *
 */
public class ConstantsResult {
	public static final Integer CHECK_TIMES = 3;//诊断次数
	public final static String CHECK_RESULT_NULL = "0"; // 视频连接异常返回值
	public final static String CHECK_RESULT_OK = "0,0"; // 视频诊断正常返回值
	public static final Integer CHECK_RESULT_STATUS_OK = 3;// z诊断结果正常
	public static final Integer CHECK_RESULT_STATUS_EXCEPTION = 1;//视频诊断结果异常
	public static final String CHECK_RESULT_STATUS_NETWORK = "0";//视频诊断故障 类型  网络连接
	public static final String CHECK_RESULT_STATUS_STREAM = "1";//视频诊断故障 类型  拉流
	public static final String CHECK_RESULT_STATUS_SIGN = "2";//视频诊断故障 类型 信号缺失
	public static final String CHECK_RESULT_STATUS_FROZEN = "3";//视频诊断故障 类型  画面冻结
	public static final String CHECK_RESULT_STATUS_COLOR = "4";//视频诊断故障 类型  色彩丢失
	public static final String CHECK_RESULT_STATUS_NOISE = "5";//视频诊断故障 类型  雪花噪声
	public static final String CHECK_RESULT_STATUS_SHADE = "6";//视频诊断故障 类型  遮挡
	public static final String CHECK_RESULT_STATUS_FUZZY = "7";//视频诊断故障 类型  模糊
	public static final String CHECK_RESULT_STATUS_DISPLACED = "8";//视频诊断故障 类型  移位
	public static final String CHECK_RESULT_STATUS_STRIP = "9";//视频诊断故障 类型  彩条
	public static final String CHECK_RESULT_STATUS_COLORCASE = "10";//视频诊断故障 类型  偏色
	public static final String CHECK_RESULT_STATUS_LIGHTEXCEPTION = "11";//视频诊断故障 类型  亮度异常
	public static final String CHECK_RESULT_STATUS_BLACKSCREEN = "14";//视频诊断故障 类型  黑屏
	 
}
