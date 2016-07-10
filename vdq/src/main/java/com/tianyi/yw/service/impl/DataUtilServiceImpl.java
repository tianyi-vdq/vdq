package com.tianyi.yw.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.dao.DeviceDiagnosisMapper;
import com.tianyi.yw.dao.DeviceMapper;
import com.tianyi.yw.dao.DeviceStatusMapper;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.DeviceStatus;
import com.tianyi.yw.model.DiagnosisItemType;
import com.tianyi.yw.model.Server;
import com.tianyi.yw.model.TaskItem;
import com.tianyi.yw.model.TaskItemType;
import com.tianyi.yw.service.DataUtilService;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.LogService;
import com.tianyi.yw.service.ParamService;
import com.tianyi.yw.service.ServerService;
import com.tianyi.yw.service.TaskService;

@Service("dataUtilService")
public class DataUtilServiceImpl implements DataUtilService {

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "logService")
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
			if (count < 10) {
				try {
					dg.setCountSize(10 - count);
					dglist = diagnosisService.getList(dg);
					// 获取请求端ip
					String ip = getIpAddress(request);
					int port = request.getRemotePort();
					if (dglist.size() != 0) {
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
							if (server.getId() == null) {
								js.setMessage("无权访问!");
								return js;
							}
						} else {
							js.setMessage("ip获取失败!");
							return js;
						}
						for (DeviceDiagnosis d : dglist) {
							d.setCheckTime(new Date());
							d.setCheckServerId(server.getId());
							diagnosisService.updatebyselective(d);
						}
						js.setCode(0);
						js.setMessage("加载数据成功!");
						js.setList(dglist);
					}
				} catch (Exception ex) {
					logService.writeLog(5, "服务器分配任务出错！", ex.getMessage());
					ex.printStackTrace();
				}
			}
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
	public void DeviceDiagnosis(int deviceId, int taskItemId, int score,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("诊断失败!");
		int logType = 5;
		try {
			DeviceDiagnosis deviceDis = dignosisMapper.selectByDeviceId(deviceId);
			DeviceStatus ds = deviceStatusMapper.selectByDeviceId(deviceId);
			if (score == 0) {
				if(ds!= null){ 
					ds.setNetworkStatus(4);
				}
				deviceStatusMapper.updateByPrimaryKeySelective(ds);
			} else if (score > 80) {
				if (deviceDis != null) {
					deviceDis.setCheckResult(score);
					deviceDis.setEndTime(new Date());
					deviceDis.setCheckTimes(1);
					dignosisMapper.updateByPrimaryKeySelective(deviceDis);
				}
				if (ds != null) {
					if (taskItemId == 1) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 2) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 3) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 4) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 5) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 6) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 7) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 8) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 9) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 10) {
						ds.setNetworkStatus(1);
					} else if (taskItemId == 11) {
						ds.setNetworkStatus(1);
					}
				}
			} else {
				logService.writeLog(logType, "诊断分值为0！");
			}
		} catch (Exception ex) {
			logService.writeLog(logType, "诊断分值后台服务度出错！", ex.getMessage());
			ex.printStackTrace();
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
		if (ip != null) {
			server.setIpaddress(ip);
			serverlist = serverService.getAllListByIP(server);
			if (serverlist != null)
				for (Server s : serverlist)
					if (s.getFlag() == 0 || s.getStatus() == 0) {
						s.setStatus(1);
						s.setFlag(1);
						serverService.updatebyselective(s);
					}
		}
	}
}
