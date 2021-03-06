package com.tianyi.yw.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
 














import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.common.utils.ConstantsResult;
import com.tianyi.yw.common.utils.StringUtil;
import com.tianyi.yw.dao.DeviceDiagnosisMapper; 
import com.tianyi.yw.dao.DeviceMapper;
import com.tianyi.yw.dao.DeviceStatusMapper;
import com.tianyi.yw.dao.DeviceStatusRecordMapper;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.DeviceStatus; 
import com.tianyi.yw.model.DeviceStatusRecord;
import com.tianyi.yw.model.Server; 
import com.tianyi.yw.service.DataUtilService;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.LogService;
import com.tianyi.yw.service.ParamService;
import com.tianyi.yw.service.ServerService;
import com.tianyi.yw.service.TaskService;

@Service
public class DataUtilServiceImpl implements DataUtilService {
    
	@Resource
	private TaskService taskService;

	@Resource
	private LogService logService;

	@Resource
	private DignosisService diagnosisService;

	@Resource
	private DeviceService deviceService;

	@Resource
	private ParamService parameService;

	@Resource
	private ServerService serverService;

	@Resource
	private DeviceDiagnosisMapper dignosisMapper;

	@Resource
	private DeviceStatusMapper deviceStatusMapper;
	@Resource
	private DeviceMapper deviceMapper;
	@Resource
	private DeviceStatusRecordMapper deviceStatusRecordMapper;


	@Autowired
	private  RabbitTemplate rabbitTemplate;
	
	/**
	 * 服务器分配（等待调度）
	 * 
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public JsonResult<DeviceDiagnosis> DiagnosisList(int count,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		DeviceDiagnosis dg = new DeviceDiagnosis();
		Server server = new Server();
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		List<DeviceDiagnosis> dglist = new ArrayList<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("加载数据失败!");
//		Lock lock = new ReentrantLock();
//		lock.lock();
		try {
			// dg.setCountSize(count); 
			//if (count < Constants.DEFAULT_QUEUE_SIZE) {
				try {
					//一次获取count条
					dg.setCountSize(count);// - count);
					dglist = diagnosisService.getList(dg);
					// 获取请求端ip
					String ip = getIpAddress(request); 
					if (dglist.size() != 0) { 
							server.setIpaddress(ip);
							server = serverService.selectByIp(server); 
						for (DeviceDiagnosis d : dglist) { 
							d.setCheckTime(new Date());
							d.setCheckServerId(server.getId());
							d.setCheckServer(ip);
							diagnosisService.updatebyselective(d);
						}
						for (DeviceDiagnosis d : dglist) { 
							d.setCheckTime(null);  
							d.setEndTime(null);
						} 
						js.setCode(0);
						js.setMessage("加载数据成功!");
						js.setList(dglist);
					}
				} catch (Exception ex) {
					logService.writeLog(5, "服务器分配任务出错！", "");
					ex.printStackTrace();
				}
			//}
		} finally {
			//lock.unlock();// 释放锁
		}
		return js;
	}
	/**
	 * request获取ip
	 * 
	 * @param request
	 * @return
	 */
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 服务器分配(自主分配)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public void DiagnosisList1(int count, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		DeviceDiagnosis dg = new DeviceDiagnosis();
		Server server = new Server();
		String method = "/vdq/dataUtil/getDiagnosisList.do";
		List<DeviceDiagnosis> list = new ArrayList<DeviceDiagnosis>();
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			// 不足10条，补足10条
			if (count < 10) {
				try {
					dg.setCountSize(10 - count);
					list = diagnosisService.getList(dg);
					// 获取请求端ip
					String ip = getIpAddress(request);
					int port = request.getRemotePort();
					if (list.size() != 0) {
						// 判断请求ip是否为空、是否有权限
						if (ip != null) {
							if (ip.equals("0:0:0:0:0:0:0:1")) {
								server.setIpaddress("127.0.0.1");
								server.setPort(8080);
							} else {
								server.setIpaddress(ip);
								server.setPort(port);
							}
							server = serverService.selectByIp(server);
							if (server != null) {
								String basepath = "http://"
										+ server.getIpaddress() + ":"
										+ server.getPort();
								String urlname = basepath + method
										+ "?DeviceDiagnosisList=" + list;
								try {
									URL url = new URL(urlname); // 把字符串转换为URL请求地址
									HttpURLConnection connection = (HttpURLConnection) url
											.openConnection();// 打开连接
									connection.connect();// 连接会话
								} catch (Exception e) {
									e.printStackTrace();
									logService.writeLog(1, "任务发送服务器出错！",
											"");
								}
							}
							for (DeviceDiagnosis d : list) {
								d.setCheckTime(new Date());
								d.setCheckServerId(server.getId());
								diagnosisService.updatebyselective(d);
							}
						}
					}
				} catch (Exception ex) {
					logService.writeLog(5, "服务器分配任务出错！", "");
					ex.printStackTrace();
				}
			}
		} finally {
			lock.unlock();// 释放锁
		}
	}

	/**
	 * 判断诊断结果，存入设备状态
	 * 
	 * @param deviceId
	 * @param score
	 * @param request
	 * @param response
	 * @param record
	 * @return
	 */
	@Override
	public boolean DeviceDiagnosis(int deviceId, String score,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("诊断失败!");
		int logType = 5;
		boolean isOk = false;
		try { 
			DeviceDiagnosis deviceDis = dignosisMapper.selectByDeviceId(deviceId); 
			//诊断结果为正常 , 只进行一次诊断
			if(score.equals(ConstantsResult.CHECK_RESULT_OK)||score.equals(ConstantsResult.CHECK_RESULT_SUCCESS)){
				deviceDis.setCheckResult(ConstantsResult.CHECK_TIMES);
				deviceDis.setEndTime(new Date());
				dignosisMapper.updateByPrimaryKeySelective(deviceDis); 
				updateDeviceStatus(deviceId,score);
				updateDeviceStatusRecord(deviceId);
			}else{ 
				//诊断结果为异常 , 进行3次重复诊断 
				int checkTimes = deviceDis.getCheckTimes();
				if(checkTimes<ConstantsResult.CHECK_TIMES){
					deviceDis.setCheckTimes(checkTimes+1);
					deviceDis.setCheckResult(null);
					deviceDis.setCheckServer(null);
					deviceDis.setCheckServerId(0);
					deviceDis.setCheckTime(new Date()); 
					String oldRtsp = deviceDis.getDeviceRtsp();
					String rtspStr = oldRtsp.substring(0, oldRtsp.length()-36); 
					UUID uuid = UUID.randomUUID();
					rtspStr += uuid.toString();
					deviceDis.setDeviceRtsp(rtspStr);
					dignosisMapper.updateByPrimaryKeySelective(deviceDis); 
					updateDeviceStatus(deviceId,score);
				}else{
					deviceDis.setCheckTimes(ConstantsResult.CHECK_TIMES);
					deviceDis.setCheckResult(ConstantsResult.CHECK_RESULT_STATUS_OK);
					deviceDis.setEndTime(new Date()); 
					dignosisMapper.updateByPrimaryKeySelective(deviceDis); 
					updateDeviceStatus(deviceId,score);
					updateDeviceStatusRecord(deviceId);
					try{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						Date date = new Date();
						String dateStr = sdf.format(date);
						String fileName = dateStr+"_exception.log";
						String filePath = "c:\\usr\\"+fileName;
						File file = new File(filePath);
						if(!file.exists()){
							file.createNewFile();
						}
						String fileData = deviceDis.getEndTime()+"---"+deviceDis.getDeviceName()+"----"+deviceDis.getDeviceRtsp()+"\r\n";
						byte contents[] = fileData.getBytes() ;
						OutputStream out = null ;
						try{
							out = new FileOutputStream(file,true) ;
						}
						catch (FileNotFoundException e) 
						{
							e.printStackTrace();
						}
						try{ 
							// 将byte数组写入到文件之中  
							out.write(contents) ; 
						}  
						catch (IOException e1) 
						{
						    e1.printStackTrace();
						}
						try{
							out.close() ;
						} 
						catch (IOException e2) 
						{
							e2.printStackTrace();
						} 
					}catch(Exception ex){
						ex.printStackTrace();
					}
					//3次诊断诊断完成 , 结果异常, 则调用mq服务推送消息
					//公安内网, 没有MQ服务, 取消 推送业务
					/*if(pushMessageToMQ(deviceDis)){ 
						logService.writeLog(logType, "诊断结果异常, 结果已推送到MQ服务！");
					}else{
						logService.writeLog(logType, "诊断结果异常, 结果推送到MQ服务,出错！");
					} */
				} 
			}
			isOk = true;
		} catch (Exception ex) {
			logService.writeLog(logType, "推送MQ消息到服务器出错！");
			//ex.printStackTrace();
		}
		return isOk;
	}


	private void updateDeviceStatusRecord(int deviceId) {
		// TODO Auto-generated method stub
		DeviceStatus ds =  deviceStatusMapper.selectByDeviceId(deviceId); 
		if(ds!= null){
			ds.setId(0); 
			if(StringUtil.isEmpty(ds.getShotUrl())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date date = new Date();
				String dateStr = sdf.format(date);
				String fileName = dateStr+"_"+ds.getPointId();
				String filePath = "source/caputrue/a/"+dateStr+"/"+fileName+".bmp";
				ds.setShotUrl(filePath);
			}
			deviceStatusRecordMapper.insertRecord(ds);
		}
	}
	private boolean pushMessageToMQ(DeviceDiagnosis deviceDis) {
		// TODO Auto-generated method stub
		Device device = deviceMapper.selectByPrimaryKey(deviceDis.getDeviceId());
		DeviceStatus devicestatus = deviceStatusMapper.selectByDeviceId(deviceDis.getDeviceId());
		String resultStr = "点位视频诊断异常,详细:"; 
		String exceptionStr = "";
		if(devicestatus != null){
			if(devicestatus.getNetworkStatus() !=null && devicestatus.getNetworkStatus()==Constants.CHECK_RESULT_EXCEPTION){
				exceptionStr += "网络连接异常;";
			}else{
				if(devicestatus.getStreamStatus() !=null && devicestatus.getStreamStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "数据流捕获异常;";
				}
				if(devicestatus.getSignStatus() !=null && devicestatus.getSignStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "视频信号异常;";
				}
				if(devicestatus.getNoiseStatus() !=null && devicestatus.getNoiseStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "雪花噪声异常;";
				}
				if(devicestatus.getColorStatus() !=null && devicestatus.getColorStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "色彩丢失异常;";
				}
				if(devicestatus.getFrameFrozenStatus() !=null && devicestatus.getFrameFrozenStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面冻结异常;";
				}
				if(devicestatus.getFrameFuzzyStatus() !=null && devicestatus.getFrameFuzzyStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面模糊异常;";
				}
				if(devicestatus.getFrameShadeStatus() !=null && devicestatus.getFrameShadeStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面遮挡异常;";
				}
				if(devicestatus.getFrameDisplacedStatus() !=null && devicestatus.getFrameDisplacedStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面移位异常;";
				}
				if(devicestatus.getFrameStripStatus() !=null && devicestatus.getFrameStripStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面彩条异常;";
				}
				if(devicestatus.getFrameColorcaseStatus() !=null && devicestatus.getFrameColorcaseStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面偏色异常;";
				}
				if(devicestatus.getLightExceptionStatus() !=null && devicestatus.getLightExceptionStatus()==Constants.CHECK_RESULT_EXCEPTION){
					exceptionStr += "画面亮度异常;";
				}
			}
		}
		if(!StringUtil.isEmpty(exceptionStr)){
			
			if(exceptionStr.contains("数据流捕获异常") && exceptionStr.contains("画面冻结异常")){
				exceptionStr = "前端点位无视频信号";
			}
			else if(exceptionStr.contains("数据流捕获异常") && exceptionStr.contains("画面亮度异常"))
			{
				exceptionStr = "前端点位无视频信号"; 
			}else{
				if(exceptionStr.contains("画面冻结异常") && exceptionStr.contains("画面亮度异常")){
					exceptionStr = "前端点位无视频信号";
				}else if(exceptionStr.contains("色彩丢失异常") && exceptionStr.contains("画面亮度异常")){
					exceptionStr = "前端点位无视频信号";
				}else{
					exceptionStr = "数据流捕获异常";
				}
			}
		}
		resultStr += exceptionStr;
		boolean isSuccess = false;
		JSONObject json = new JSONObject();
		json.element("cameraId", device.getPointId());
		json.element("cameraName",device.getPointName());
		json.element("deviceIp",device.getIpAddress());
		json.element("faultCode", Constants.ROUTEDATA_YWALARM_VIDEO_CODE);
		json.element("faultContent", resultStr);
		json.element("faultType", Constants.ROUTEDATA_YWALARM_VIDEO_TYPE);
		try {
			rabbitTemplate.convertAndSend(Constants.ROUTEDATA_YWALARM_VIDEO,json.toString());
			isSuccess = true;
		} catch (AmqpException ex) {
			// TODO Auto-generated catch block
			logService.writeLog(5, "推送异常信息到易维ＭＱ服务错误！", "");
			//ex.printStackTrace();
		}
		return isSuccess;
	}

	private void updateDeviceStatus(int deviceId, String score) {
		// TODO Auto-generated method stub
		DeviceStatus ds = new DeviceStatus();
		ds = deviceStatusMapper.selectByDeviceId(deviceId);
		if(ds != null){ 
			if(StringUtil.isEmpty(ds.getShotUrl())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date date = new Date();
				String dateStr = sdf.format(date);
				String fileName = dateStr+"_"+ds.getPointId();
				String filePath = "source/caputrue/a/"+dateStr+"/"+fileName+".bmp";
				ds.setShotUrl(filePath);
			}
			if(score.equals(ConstantsResult.CHECK_RESULT_OK)||score.equals(ConstantsResult.CHECK_RESULT_SUCCESS)){
				ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_OK); 
				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);  
				ds.setBlackScreenStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
			}else if(score.equals(ConstantsResult.CHECK_RESULT_NULL)){
				ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION); 
				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setBlackScreenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
			}else{
				String[] scores = score.split("\\,");
				for(String s :scores){
					ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
					ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
					if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_NOISE)) {
						ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_SIGN)) {
						ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_COLOR)) {
						ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_FROZEN)) {
						ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_SHADE)) {
						ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_FUZZY)) {
						ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_DISPLACED)) {
						ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_COLORCASE)) {
						ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_LIGHTEXCEPTION)) {
						ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_BLACKSCREEN)) {
						ds.setBlackScreenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
						ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					}else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_STRIP)) {
						ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					}
				}
			} 
			ds.setRecordTime(new Date());
			ds.setCreateTime(new Date());
			deviceStatusMapper.updateByPrimaryKeySelective(ds);
		}else{
			ds = new DeviceStatus();
			Device d = deviceMapper.selectByPrimaryKey(deviceId);
			if(d!= null){
				ds.setPointId(d.getPointId());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String dateStr = sdf.format(date);
			String fileName = dateStr+"_"+ds.getPointId();
			String filePath = "source/caputrue/a/"+dateStr+"/"+fileName+".bmp";
			ds.setShotUrl(filePath);
			ds.setId(0);
			ds.setDeviceId(deviceId); 
			ds.setRecordTime(new Date());
			ds.setCreateTime(new Date());
			if(score.equals(ConstantsResult.CHECK_RESULT_OK)||score.equals(ConstantsResult.CHECK_RESULT_SUCCESS)){
				ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_OK); 
				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
				ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);  
				ds.setBlackScreenStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
			}else if(score.equals(ConstantsResult.CHECK_RESULT_NULL)){
				ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION); 
				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
//				ds.setBlackScreenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
			}else{
				String[] scores = score.split("\\,");
				for(String s :scores){
					ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
					ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_OK);
					if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_NOISE)) {
						ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_SIGN)) {
						ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_COLOR)) {
						ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_FROZEN)) {
						ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_SHADE)) {
						ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_FUZZY)) {
						ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_DISPLACED)) {
						ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_COLORCASE)) {
						ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_LIGHTEXCEPTION)) {
						ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					}else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_BLACKSCREEN)) {
						ds.setBlackScreenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
						ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					}else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_STRIP)) {
						ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					}
				}
			} 
			deviceStatusMapper.insert(ds); 
		}
	}
	
	
	/**
	 * ip监测
	 */
	@Override
	public void CheckIP(String ip, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Server server = new Server();
		List<Server> serverlist = new ArrayList<Server>(); 
		server.setIpaddress(ip);
		serverlist = serverService.getAllListByIP(server);
		if (serverlist != null){
			for (Server s : serverlist) {
				s.setStatus(1);
				s.setFlag(1);
				serverService.updatebyselective(s);
			} 
		}
		List<DeviceDiagnosis> lst = new ArrayList<DeviceDiagnosis>();
		lst = dignosisMapper.selectLatestDevice();
		if(lst.size()>0){
			for(DeviceDiagnosis dd : lst){
				dd.setCheckResult(null); 
				dd.setCheckServerId(0);
				dignosisMapper.updateByPrimaryKey(dd);
			}
		}
	}
}
