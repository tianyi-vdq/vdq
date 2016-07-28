package com.tianyi.yw.web.controller.winService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.StringUtil;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.Parame;
import com.tianyi.yw.model.Server;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.model.TaskItem;
import com.tianyi.yw.service.DataUtilService;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.LogService;
import com.tianyi.yw.service.ParamService;
import com.tianyi.yw.service.ServerService;
import com.tianyi.yw.service.TaskRunning;
import com.tianyi.yw.service.TaskService;

@Scope("prototype")
@Controller
@RequestMapping("/dataUtil")
public class DataUtilAction {
	
	@Resource 
	private TaskService taskService;
	
	@Resource 
	private DataUtilService dataUtilService;
	
	@Resource 
	private TaskRunning taskRunService;
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

	 /**
	 * 任务执行 立即执行 , 更改任务状态
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * {@value id}
	 * @return js
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonloadTaskRun.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Task> RunTask(
			@RequestParam(value = "id", required = false) final Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("任务启动失败!");
		try { 
			Task task = taskService.getTaskById(id);
			task.setFlag(1);
			taskService.saveOrUpdateTask(task); 
			js.setCode(0);
			js.setMessage("任务启动成功,正在执行!");
		} catch (Exception e) {
			e.printStackTrace();
			js.setMessage("任务启动失敗,详细:"+e.getMessage());
		}
		return js;
	}
	/**
	 * 任务执行 立即执行
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * {@value id}
	 * @return js
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonloadTaskRunRightNow.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public void RunTaskNow(
			@RequestParam(value = "id", required = false) final Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		taskRunService.TaskRun(id);
	} 
	
	@ResponseBody
	@RequestMapping(value = "/jsonLoadDeviceList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Device> deviceList(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(new Integer(1));
		js.setMessage("加载设备列表失败!");
		try{
			List<Device> ls = new ArrayList<Device>();
			Device d = new Device(); 
			ls = deviceService.getDeviceList(d);
			js.setList(ls);
			js.setCode(0);
			js.setMessage("加载设备列表成功！"); 
		}catch(Exception ex){
			ex.printStackTrace();
			logService.writeLog(1, "加载设备列表失败!","未读取到设备数据!详细："+ ex.getMessage());
		}
		return js;
	}
	 
	

	
	@ResponseBody
	@RequestMapping(value = "/jsonLoadTaskList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Task> jsonLoadTaskList(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("加载当前有效任务失败!");
		try{ 
			Task task = new Task();
			task.setFlag(1);
			List<Task> eff = taskService.getEffectTaskList(task); 
			js.setList(eff);
			js.setCode(0);
			js.setMessage("加载当前有效任务成功！"); 
		}catch(Exception ex){
			ex.printStackTrace();
			logService.writeLog(1, "加载当前有效任务失败!", "未读取到数据！详细："+ex.getMessage());
		}
		return js;
	}
	
	/**
	 * 服务器分配
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadDiagnosisList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<DeviceDiagnosis>  DiagnosisList(
			@RequestParam(value = "count", required = false) Integer count,
			HttpServletRequest request,HttpServletResponse response) {
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("未获取到参数！");
		try{
			if(count != null){
				js = dataUtilService.DiagnosisList(count,request,response);
				js.setCode(0);
				js.setMessage("获取诊断点位设备成功！");
			}else{
				js.setCode(1);
				js.setMessage("未获取到参数！");
			}
		}catch(Exception ex){ 
			ex.printStackTrace();			
			js.setMessage("未获取到参数！详细："+ex.getMessage());
			logService.writeLog(1, "获取诊断点位设备失败！",js.getMessage());
		}
		return js;
	}
	
	  
	  /**
	   * 根据任务id，获取结果诊断项
	   * @param taskId
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping(value = "jsonLoadTaskItemType.do", produces = { "text/html;charset=UTF-8" })
	  public JsonResult<TaskItem> getTaskItemType(
			  @RequestParam(value = "id", required = false) Integer taskId,
			  HttpServletRequest request, HttpServletResponse response){
		  JsonResult<TaskItem> js = new JsonResult<TaskItem>();
		  js.setCode(1);
		  js.setMessage("获取结果诊断项失败！");
		  try{
			  TaskItem ti = new TaskItem();
			  List<TaskItem> tilist = new ArrayList<TaskItem>();
			  if(taskId != null && taskId != 0){
				  ti.setTaskId(taskId);
				  tilist=taskService.getTaskItemList(ti);
				  js.setList(tilist);
				  js.setCode(0);
				  js.setMessage("获取诊断项成功！"); 
			  }else{
				  js.setCode(1);
				  js.setMessage("获取结果诊断项失败！");
			  }
		  }catch(Exception ex){
			  ex.printStackTrace();
			  js.setCode(1);
			  js.setMessage("获取结果诊断项失败！详细："+ex.getMessage());
			  logService.writeLog(1, "获取结果诊断项失败！",js.getMessage());
		  }
		  return js;
	  }
	  
	  
	  /**
	   * 判断诊断结果，存入设备状态
	   * @param deviceId
	   * @param score
	   * @param request
	   * @param response
	   * @return
	   */
	    @ResponseBody
		@RequestMapping(value = "/jsonSaveDiagnosis.do", produces = { "text/html;charset=UTF-8" })
		public String DeviceDiagnosis(
				@RequestParam(value = "id", required = false) int deviceId, 
				@RequestParam(value = "score", required = false) String score,
				HttpServletRequest request,HttpServletResponse response){ 
	    	String result = "0";
			try{
				if(deviceId >0){
					if(dataUtilService.DeviceDiagnosis(deviceId, score, request, response)){
						result =  "1";
					} 
				}else{
					logService.writeLog(1, "保存数据失败", "保存数据失败！详细：客户端数据异常，保存诊断结果失败！");
				}
			}catch(Exception ex){
				ex.printStackTrace(); 
				logService.writeLog(1, "保存数据失败", "详细："+ex.getMessage());
			} 
			return result;
		}
	    
	    /** 
		 * IP状态监测接口
		 * @param request
		 * @param response
		 * @param ip
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/checkIP.do")
		public JsonResult<Server> checkIP(  @RequestParam(value = "ip", required = false) String ip,
				HttpServletRequest request, HttpServletResponse response) {
			JsonResult<Server> js = new JsonResult<Server>();
			  js.setCode(1);
			  js.setMessage("获取结果诊断项失败！");
			  try{
				  if(!StringUtil.isEmpty(ip)){
					dataUtilService.CheckIP(ip, request, response);
					js.setCode(0);
					js.setMessage("服务启动成功!");
				  }else{
						js.setMessage("服务启动检测!");
						logService.writeLog(1, "诊断服务启用出现异常", "诊断服务启用出现异常,详细:未获取到客户端ip");
				  }
			  }catch(Exception ex){ 
					js.setMessage("服务启动失败!");
					logService.writeLog(1, "诊断服务启用出现异常", "诊断服务启用出现异常,详细:"+ex.getMessage());
			  }
			  return js;
		}
		
}
































