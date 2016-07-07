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
	
	/**
	 * 服务器分配（等待调度）
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public JsonResult<DeviceDiagnosis> DiagnosisList(int count,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		DeviceDiagnosis dg = new DeviceDiagnosis();
		Server server = new Server();
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		List<DeviceDiagnosis> dglist = new ArrayList<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("加载数据失败!");
		Lock lock = new ReentrantLock();
		lock.lock();
		try{
		//dg.setCountSize(8);
		//不足10条，补足10条
		if(count < 10){
			try{
				dg.setCountSize(10-count);
				dglist = diagnosisService.getList(dg);
				//获取请求端ip
				String ip = getIpAddress(request);
				int port = request.getRemotePort();
				if(dglist.size() != 0){
					//判断请求ip是否为空、是否有权限
					if(ip != null){								
						if(ip.equals("0:0:0:0:0:0:0:1")){
							server.setIpaddress("127.0.0.1");
							server.setPort(8080);
						}else{
							server.setIpaddress(ip);
							server.setPort(port);
						}
						server = serverService.selectByIp(server);
						if(server.getId() == null){													
							js.setMessage("无权访问!");
							return js;
						}													
					}else{
						js.setMessage("ip获取失败!");
						return js;
					}					
					for(DeviceDiagnosis d:dglist){
						d.setCheckTime(new Date());
						d.setCheckServerId(server.getId());	
						diagnosisService.updatebyselective(d);
					}
					js.setCode(0);
					js.setMessage("加载数据成功!");
					js.setList(dglist);
				}
			}catch(Exception ex){
				logService.writeLog(5, "服务器分配任务出错！", ex.getMessage());
				ex.printStackTrace();
			}			
		}
		}finally {    
		        lock.unlock();// 释放锁    
       } 
		return js;
	}

	  /**
	   * request获取ip
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
		 * @param request
		 * @param response
		 * @return
		 */
	@Override
	public void DiagnosisList1(int count,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		DeviceDiagnosis dg = new DeviceDiagnosis();
		Server server = new Server();
		String method = "/vdq/dataUtil/getDiagnosisList.do";
		List<DeviceDiagnosis> list = new ArrayList<DeviceDiagnosis>();
		Lock lock = new ReentrantLock();
		lock.lock();
		try{
		//不足10条，补足10条
		if(count < 10){
			try{
				dg.setCountSize(10-count);
				list = diagnosisService.getList(dg);
				//获取请求端ip
				String ip = getIpAddress(request);
				int port = request.getRemotePort();
				if(list.size() != 0){
					//判断请求ip是否为空、是否有权限
					if(ip != null)
					{								
						if(ip.equals("0:0:0:0:0:0:0:1"))
						{
							server.setIpaddress("127.0.0.1");
							server.setPort(8080);
						}else{
							server.setIpaddress(ip);
							server.setPort(port);
						}						
				        server = serverService.selectByIp(server);
				        if(server != null){
				        	 String basepath = "http://"+server.getIpaddress()+":"+server.getPort();
				        	 String urlname = basepath + method + "?DeviceDiagnosisList=" + list;
				             try {
				                URL url = new URL(urlname);    // 把字符串转换为URL请求地址
				                HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
				                connection.connect();// 连接会话
				             } catch (Exception e) {
				                 e.printStackTrace();
				                logService.writeLog(1, "任务发送服务器出错！",e.getMessage()); 
				             }
				        }
					    for(DeviceDiagnosis d:list)
					    {
					    	d.setCheckTime(new Date());
					    	d.setCheckServerId(server.getId());	
					    	diagnosisService.updatebyselective(d);
				    	}					   
					}
				}
			}catch(Exception ex){
				logService.writeLog(5, "服务器分配任务出错！", ex.getMessage());
				ex.printStackTrace();
			}			
		}
		}finally {    
	        lock.unlock();// 释放锁    
        } 
	}
	
	/**
	   * 判断诊断结果，存入设备状态
	   * @param deviceId
	   * @param score
	   * @param request
	   * @param response
	   * @return
	   */
	@Override
	public JsonResult<DeviceDiagnosis> DeviceDiagnosis( Integer deviceId,String score,
			HttpServletRequest request,HttpServletResponse response){
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("诊断失败!");		
		int logType = 5;
		DeviceDiagnosis dg = new DeviceDiagnosis();
		DeviceStatus ds = new DeviceStatus();
		ds.setDeviceId(deviceId);
		List<TaskItem> tilist = new ArrayList<TaskItem>();
		TaskItem ti = new TaskItem();
		DiagnosisItemType dit = new DiagnosisItemType();
		try{
		if(deviceId != null && deviceId != 0){
			//根据设备ID，获取临时表这条数据。
			dg.setDeviceId(deviceId);			
			dg = diagnosisService.getExistDiagnosis(dg);			
			//根据任务ID,取到诊断项目集合。
			if(dg.getTaskId() != null && dg.getTaskId() != 0){
				ti.setTaskId(dg.getTaskId());
				tilist = taskService.getTaskItemList(ti);
				//把score转换成int型数组，然后做个判断。判断数组长度是否等于TypeList的长度.失败返回js.("诊断分值不完整")
				String[] scores = score.split(";");
				//判断诊断项目和诊断分值是否为空
				if(scores.length > 0 && tilist.size() != 0)
				{
					//判断诊断项目和诊断分值数量是否相等
					if(scores.length == tilist.size())
					{
						int ok=0,exception=0,alarm=0,sco=-1,type=0;
						String s;
						//解析诊断分值
						for(int i=0;i<scores.length;i++)
						{
							s = scores[i];
							String[] s1 = s.split(",");
							if(s1.length != 2){
								js.setMessage("诊断分值不明确!");
								return js;
							}
							type = Integer.valueOf(s1[0]);
							sco = Integer.valueOf(s1[1]);
							//根据诊断项目类型获取相应诊断标准 ，按诊断分值判断诊断结果
							dit = diagnosisService.getExistDiagnosisType(type);
							if(sco > 0 && sco <= dit.getValue1())
							{
								//异常,插入设备状态
								insertDeviceStatus(type,1,ds);
								exception++;
								continue;
							}else if(dit.getValue1() < sco && sco <= dit.getValue2())
							{
								//警告,插入设备状态
								insertDeviceStatus(type,2,ds);
								alarm++;
								continue;
							}else if(sco > dit.getValue2() && sco <= 100)
							{
								//正常,插入设备状态
						    	insertDeviceStatus(type,3,ds);
								ok++;
								continue;
							}else
							{
								//失败,插入设备状态
							    insertDeviceStatus(type,4,ds);
								//fail++;
								continue;
							}													
						}
						//对诊断结果做处理
						//正常
						if(ok == tilist.size())
						{
							dg.setEndTime(new Date());
							dg.setCheckResult(1);
							dg.setCheckTimes(dg.getCheckTimes()+1);
							//更新临时表相关数据
						     diagnosisService.updatebyselective(dg);	
							ds.setCreateTime(new Date());
							ds.setId(0);
							//存入设备状态
					       	deviceService.saveOrUpdateDeviceStatus(ds);
							js.setCode(0);
							js.setMessage("诊断成功！诊断结果为正常！");
							return js;
						}
						//异常
						else if(exception > 0)
						{								
							dg.setCheckTimes(dg.getCheckTimes()+1);
							//诊断不为正常的设备多诊断两次
							if(dg.getCheckTimes() == 3)
							{
								dg.setEndTime(new Date());
								dg.setCheckResult(2);
								//dg.setCheckTimes(0);
								//更新临时表相关数据
						       	diagnosisService.updatebyselective(dg);
								ds.setCreateTime(new Date());
								ds.setId(0);
								//存入设备状态
						       	deviceService.saveOrUpdateDeviceStatus(ds);
								js.setCode(0);
								js.setMessage("诊断成功！诊断结果为异常！");
								return js;
							}else{
								dg.setCheckTime(null);
								dg.setCheckServerId(0);
					      		diagnosisService.updatebyselective(dg);
								js.setCode(0);
								js.setMessage("诊断成功！第"+dg.getCheckTimes()+"次诊断结果为异常！等待重新诊断！");
								return js;
							}
						}
						//警告
						else if(alarm + ok == tilist.size() && alarm > 0){
							dg.setCheckTimes(dg.getCheckTimes()+1);
							//诊断不为正常的设备多诊断两次
							if(dg.getCheckTimes() == 3)
							{
								dg.setEndTime(new Date());
								dg.setCheckResult(3);
								//dg.setCheckTimes(0);
								//更新临时表相关数据
						       	diagnosisService.updatebyselective(dg);								
								ds.setCreateTime(new Date());
								ds.setId(0);
								//存入设备状态
								deviceService.saveOrUpdateDeviceStatus(ds);

								js.setCode(0);
								js.setMessage("诊断成功！诊断结果为警告！");
								return js;
							}else{
								dg.setCheckTime(null);
								dg.setCheckServerId(0);
						       	diagnosisService.updatebyselective(dg);								
								js.setCode(0);
								js.setMessage("诊断成功！第"+dg.getCheckTimes()+"次诊断结果为警告！等待重新诊断！");
								return js;
							}
						}
						//失败
						else
						//if(fail > 0)
						{
							dg.setCheckTimes(dg.getCheckTimes()+1);
							//诊断不为正常的设备多诊断两次
							if(dg.getCheckTimes() == 3)
							{
								dg.setEndTime(new Date());
								dg.setCheckResult(4);
								//dg.setCheckTimes(0);
								//更新临时表相关数据
								try{
							    	diagnosisService.updatebyselective(dg);
								}catch(Exception ex){
									logService.writeLog(logType, "更新临时表数据失败！");
									ex.printStackTrace();				
								}
								ds.setCreateTime(new Date());
								ds.setId(0);
								//存入设备状态
					      		deviceService.saveOrUpdateDeviceStatus(ds);
								js.setCode(0);
								js.setMessage("诊断成功！诊断结果为失败！");
								return js;
							}else{
								dg.setCheckTime(null);
								dg.setCheckServerId(0);
					      		diagnosisService.updatebyselective(dg);								
								js.setCode(0);
								js.setMessage("诊断成功！第"+dg.getCheckTimes()+"次诊断结果为失败！等待重新诊断！");
								return js;
							}
					}
				}else{
					js.setMessage("诊断分值有误！");
					return js;
				}
			}
		  }		
		}
		}catch(Exception ex){
			logService.writeLog(logType, "诊断分值后台服务度出错！",ex.getMessage());
			ex.printStackTrace();				
		}
		return js;
	}
	  

	  /**
	   * 根据诊断项目类型插入设备状态
	   * @param typeid
	   * @param status
	   * @param ds
	   * @return
	   */
	  private DeviceStatus insertDeviceStatus(int typeid,int status,DeviceStatus ds){
		  TaskItemType tit = new TaskItemType();
		  tit = taskService.getTaskItemTypeById(typeid);
		  if(tit != null){
			  if("拉流".equals(tit.getName())){
				  ds.setStreamStatus(status);
			  }else if("雪花噪音".equals(tit.getName())){
				  ds.setNoiseStatus(status);
			  }else if("信号缺失".equals(tit.getName())){
				  ds.setSignStatus(status);
			  }else if("色彩丢失".equals(tit.getName())){
				  ds.setColorStatus(status);
			  }else if("画面冻结".equals(tit.getName())){
				  ds.setFrameFrozenStatus(status);
			  }else if("画面遮挡".equals(tit.getName())){
				  ds.setFrameShadeStatus(status);
			  }else if("画面模糊".equals(tit.getName())){
				  ds.setFrameFuzzyStatus(status);
			  }else if("画面移位".equals(tit.getName())){
				  ds.setFrameDisplacedStatus(status);
			  }else if("画面彩条".equals(tit.getName())){
				  ds.setFrameStripStatus(status);
			  }else if("画面偏色".equals(tit.getName())){
				  ds.setFrameColorcaseStatus(status);
			  }else if("亮度异常".equals(tit.getName())){
				  ds.setLightExceptionStatus(status);
			  }
		  }
		return ds;	  
	  }
}
