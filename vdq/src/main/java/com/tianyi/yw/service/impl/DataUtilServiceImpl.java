package com.tianyi.yw.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.tianyi.yw.dao.DeviceDiagnosisMapper; 
import com.tianyi.yw.dao.DeviceStatusMapper;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.DeviceStatus; 
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
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			// dg.setCountSize(8);
			// 不足10条，补足10条
			//if (count < Constants.DEFAULT_QUEUE_SIZE) {
				try {
					//一次获取8条
					dg.setCountSize(Constants.DEFAULT_QUEUE_SIZE);// - count);
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
						} 
						js.setCode(0);
						js.setMessage("加载数据成功!");
						js.setList(dglist);
					}
				} catch (Exception ex) {
					logService.writeLog(5, "服务器分配任务出错！", ex.getMessage());
					ex.printStackTrace();
				}
			//}
		} finally {
			lock.unlock();// 释放锁
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
											e.getMessage());
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
					logService.writeLog(5, "服务器分配任务出错！", ex.getMessage());
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
			if(score.equals(ConstantsResult.CHECK_RESULT_OK)){
				deviceDis.setCheckResult(ConstantsResult.CHECK_TIMES);
				deviceDis.setEndTime(new Date());
			}else{ 
				//诊断结果为异常 , 进行3次重复诊断 
				int checkTimes = deviceDis.getCheckTimes();
				if(checkTimes<ConstantsResult.CHECK_TIMES){
					deviceDis.setCheckTimes(checkTimes+1);
					deviceDis.setCheckResult(null);
					deviceDis.setCheckServer(null);
					deviceDis.setCheckServerId(0);
					deviceDis.setCheckTime(new Date()); 
				}else{
					deviceDis.setCheckTimes(ConstantsResult.CHECK_TIMES);
					deviceDis.setCheckResult(ConstantsResult.CHECK_RESULT_STATUS_OK);
					deviceDis.setEndTime(new Date()); 
					//3次诊断诊断完成 , 结果异常, 则调用mq服务推送消息
					if(pushMessageToMQ(deviceDis)){ 
						logService.writeLog(logType, "诊断结果异常, 结果已推送到MQ服务！");
					}else{
						logService.writeLog(logType, "诊断结果异常, 结果推送到MQ服务,出错！");
					}
					
				} 
			}
			dignosisMapper.updateByPrimaryKeySelective(deviceDis); 
			updateDeviceStatus(deviceId,score);
			isOk = true;
		} catch (Exception ex) {
			logService.writeLog(logType, "诊断分值后台服务度出错！", ex.getMessage());
			//ex.printStackTrace();
		}
		return isOk;
	}

	private boolean pushMessageToMQ(DeviceDiagnosis deviceDis) {
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		JSONObject json = new JSONObject();
		json.element("cameraId", deviceDis.getDeviceId());
		json.element("cameraName",deviceDis.getDeviceName());
		json.element("deviceIp", Constants.ROUTEDATA_YWALARM_VIDEO_IP);
		json.element("faultCode", Constants.ROUTEDATA_YWALARM_VIDEO_CODE);
		json.element("faultContent", "点位视频诊断异常,请注意查收");
		json.element("faultType", Constants.ROUTEDATA_YWALARM_VIDEO_TYPE);
		try {
			rabbitTemplate.convertAndSend(Constants.ROUTEDATA_YWALARM_VIDEO,json.toString());
			isSuccess = true;
		} catch (AmqpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	private void updateDeviceStatus(int deviceId, String score) {
		// TODO Auto-generated method stub
		DeviceStatus ds = new DeviceStatus();
		ds = deviceStatusMapper.selectByDeviceId(deviceId);
		if(ds != null){ 
			if(score.equals(ConstantsResult.CHECK_RESULT_OK)){
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
			}else if(score.equals(ConstantsResult.CHECK_RESULT_NULL)){
				ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION); 
				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
			}else{
				String[] scores = score.split("\\,");
				for(String s :scores){
					if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_NETWORK)) {
						ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_STREAM)) {
						ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_NOISE)) {
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
					}
				}
			} 
			ds.setRecordTime(new Date());
			ds.setCreateTime(new Date());
			deviceStatusMapper.updateByPrimaryKeySelective(ds);
		}else{
			ds = new DeviceStatus();
			ds.setId(0);
			ds.setDeviceId(deviceId); 
			ds.setRecordTime(new Date());
			ds.setCreateTime(new Date());
			if(score.equals(ConstantsResult.CHECK_RESULT_OK)){
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
			}else if(score.equals(ConstantsResult.CHECK_RESULT_NULL)){
				ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION); 
				ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setNoiseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameShadeStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameStripStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setSignStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setColorStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameFrozenStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameFuzzyStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameDisplacedStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setFrameColorcaseStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
				ds.setLightExceptionStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
			}else{
				String[] scores = score.split("\\,");
				for(String s :scores){
					if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_NETWORK)) {
						ds.setNetworkStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_STREAM)) {
						ds.setStreamStatus(ConstantsResult.CHECK_RESULT_STATUS_EXCEPTION);
					} else if (s.equals(ConstantsResult.CHECK_RESULT_STATUS_NOISE)) {
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
	}
}
