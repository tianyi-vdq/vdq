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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.Parame;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.model.TaskItem;
import com.tianyi.yw.service.DataUtilService;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.LogService;
import com.tianyi.yw.service.ParamService;
import com.tianyi.yw.service.ServerService;
import com.tianyi.yw.service.TaskService;

@Scope("prototype")
@Controller
@RequestMapping("/dataUtil")
public class DataUtilAction {
	
	@Resource(name = "taskService")
	private TaskService taskService;
	
	@Resource(name = "dataUtilService")
	private DataUtilService dataUtilService;
	
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
	 * 日志写入接口
	 * @param request
	 * @param response
	 * @param stringLog
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveLog.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Log> SaveLog(HttpServletRequest request, HttpServletResponse response,Log log) {
		JsonResult<Log> js = new JsonResult<Log>();
		js.setCode(new Integer(1));
		js.setMessage("写入日志失败!");	
		Log p = new Log();
		try
		{
			p.setTypeId(log.getTypeId());
			p.setContent(new String(log.getContent().getBytes("iso8859-1"), "utf-8"));
			p.setDescription(new String(log.getDescription().getBytes("iso8859-1"), "utf-8"));
			p.setCreateTime(new Date());
		    if(p.getContent() != null && p.getContent() != "" && p.getTypeId() != null && p.getTypeId() != '0')
		    {
		    	logService.saveOrUpdateLog(p);
		    	js.setCode(new Integer(0));
		    	js.setMessage("保存日志成功！");
		     }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			
		return js; 		
	}
	
	
	
	
	/**
	 * 任务列表接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadTaskList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Task> TaskList(
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("加载任务列表失败!");
	
		try {
			List<Task> tasklist = new ArrayList<Task>();
			Task p = new Task();
			tasklist = taskService.getTaskList(p);
			js.setList(tasklist);
			js.setCode(0);
			js.setMessage("加载任务列表成功!");				
			} catch (Exception ex) {
			ex.printStackTrace();
		}
		return js;
	}
	
 
	
	
	@ResponseBody
	@RequestMapping(value = "/jsonLoadDeviceList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Device> deviceList(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(new Integer(1));
		js.setMessage("加载设备列表失败!");
		List<Device> ls = new ArrayList<Device>();
		Device d = new Device();
		try{
			ls = deviceService.getDeviceList(d);
			js.setList(ls);
			js.setCode(0);
			js.setMessage("加载设备列表成功！");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	@ResponseBody
	@RequestMapping(value = "/jsonLoadParameList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Parame>  parameList(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<Parame> js = new JsonResult<Parame>();
		js.setCode(new Integer(1));
		js.setMessage("加载参数列表失败!");
		List<Parame> ls = new ArrayList<Parame>();
		Parame p = new Parame();
		try{
			ls = parameService.getParameList(p);
			js.setList(ls);
			js.setCode(0);
			js.setMessage("加载参数列表成功！");
		}catch(Exception ex){
			ex.printStackTrace();
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
		if(count != null){
			js = dataUtilService.DiagnosisList(count,request,response);
		}else{
			js.setCode(1);
			js.setMessage("未获取到参数！");
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
		  TaskItem ti = new TaskItem();
		  List<TaskItem> tilist = new ArrayList<TaskItem>();
		  if(taskId != null && taskId != 0){
			  ti.setTaskId(taskId);
			  tilist=taskService.getTaskItemList(ti);
			  js.setList(tilist);
			  js.setCode(0);
			  js.setMessage("获取诊断项成功！");
			  return js;
		  }else{
			  js.setCode(1);
			  js.setMessage("获取结果诊断项失败！");
			  return js;
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
	    @ResponseBody
		@RequestMapping(value = "/jsonSaveDiagnosis.do", produces = { "text/html;charset=UTF-8" })
		public JsonResult<DeviceDiagnosis> DeviceDiagnosis(
				@RequestParam(value = "deviceId", required = false) Integer deviceId,
				@RequestParam(value = "score", required = false) String score,
				HttpServletRequest request,HttpServletResponse response){
			JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
			if(deviceId != null && score != null){
				js = dataUtilService.DeviceDiagnosis(deviceId, score, request, response);
			}else{
				js.setMessage("参数获取失败！");
				js.setCode(1);
			}
			return js;
		}
}
































