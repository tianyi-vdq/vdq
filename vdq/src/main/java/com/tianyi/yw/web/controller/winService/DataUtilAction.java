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
import com.tianyi.yw.model.DiagnosisItemType;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.Parame;
import com.tianyi.yw.model.Server;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.model.TaskItem;
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
		DeviceDiagnosis dg = new DeviceDiagnosis();
		Server server = new Server();
		JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
		List<DeviceDiagnosis> dglist = new ArrayList<DeviceDiagnosis>();
		js.setCode(1);
		js.setMessage("加载数据失败!");
		//dg.setCountSize(8);
		if(count == null)
			count = 10;
		if(count != null && count < 10){
			try{
				dg.setCountSize(10-count);
				dglist = diagnosisService.getList(dg);
				String ip = getIpAddress(request);
				if(dglist.size() != 0){
					for(DeviceDiagnosis d:dglist){
						d.setCheckTime(new Date());
						if(ip != null){
							server.setIpaddress(ip);
							server = serverService.selectByIp(server);
							if(server.getId() != null){
								d.setCheckServerId(server.getId());
							}else{
								js.setMessage("无权访问!");
								return js;
							}
							
							diagnosisService.updatebyselective(d);
						}else{
							js.setMessage("ip获取失败!");
							return js;
						}						
					}
					js.setCode(0);
					js.setMessage("加载数据成功!");
					js.setList(dglist);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}			
		}
		return js;
	}
	
	  /**
	   * request获取ip
	   * @param request
	   * @return
	   */
	  public static String getIpAddress(HttpServletRequest request) { 
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
	  
	  
	    @ResponseBody
		@RequestMapping(value = "/jsonSaveDiagnosis.do", produces = { "text/html;charset=UTF-8" })
		public JsonResult<DeviceDiagnosis> DeviceDiagnosis(
				@RequestParam(value = "deviceId", required = false) Integer deviceId,
				@RequestParam(value = "score", required = false) String score,
				HttpServletRequest request,HttpServletResponse response){
			JsonResult<DeviceDiagnosis> js = new JsonResult<DeviceDiagnosis>();
			js.setCode(1);
			js.setMessage("加载参数列表失败!");
			DeviceDiagnosis dg = new DeviceDiagnosis();
			List<TaskItem> tilist = new ArrayList<TaskItem>();
			TaskItem ti = new TaskItem();
			DiagnosisItemType dit = new DiagnosisItemType();
			if(deviceId != null && deviceId != 0){
				//根据deviceId，获取diagnosis这条数据。
				dg.setDeviceId(deviceId);
				dg = diagnosisService.getExistDiagnosis(dg);
				//根据diagnosis.taskId,取到itemTypeList集合。(关联TypeName)
				if(dg.getTaskId() != null && dg.getTaskId() != 0){
					ti.setTaskId(dg.getTaskId());
					tilist = taskService.getTaskItemList(ti);
					//把score转换成int型数组，然后做个判断。判断数组长度是否等于TypeList的长度.失败返回js.("诊断分值不完整")
					String[] scores = score.split(",");
					if(scores.length > 0 && tilist.size() != 0)
					{
						if(scores.length == tilist.size())
						{
							int i=0,ok=0,exception=0,alarm=0,fail=0,sco=-1;
							String s;
							//遍历list。根据每个TypeId获取type_value表这条数据判断。
							for(TaskItem t:tilist)
							{
								s = scores[i];
								sco = Integer.valueOf(s);
								//根据t.getItemTypeId()获取dit 
								dit = diagnosisService.getExistDiagnosisType(t.getItemTypeId());
								if(sco >= 0 && sco < dit.getValue1())
								{
									//异常，
									exception++;
									break;
								}else if(dit.getValue1() <= sco && sco <= dit.getValue2())
								{
									//警告
									alarm++;
								}else if(sco > dit.getValue2() && sco <= 100)
								{
									//正常
									ok++;
								}else
								{
									//失败
									fail++;
									break;
								}
								i++;							
							}
							//正常
							if(ok == tilist.size())
							{
								dg.setEndTime(new Date());
								dg.setCheckResult(200);
								diagnosisService.updatebyselective(dg);
								js.setCode(0);
								js.setMessage("诊断成功！诊断结果为正常！");
								return js;
							}
							//异常
							if(exception > 0)
							{
								dg.setCheckTime(null);
								dg.setCheckServerId(0);
								dg.setCheckTimes(dg.getCheckTimes()+1);
								if(dg.getCheckTimes() == 3)
								{
									dg.setEndTime(new Date());
									dg.setCheckResult(201);
									dg.setCheckTimes(0);
									diagnosisService.updatebyselective(dg);
									js.setCode(0);
									js.setMessage("诊断成功！诊断结果为异常！");
									return js;
								}else{
									diagnosisService.updatebyselective(dg);
								}
							}
							//警告
							if(alarm + ok == tilist.size() && alarm > 0){
								dg.setEndTime(new Date());
								dg.setCheckResult(202);
								diagnosisService.updatebyselective(dg);
								js.setCode(0);
								js.setMessage("诊断成功！诊断结果为警告！");
								return js;
							}
							//失败
							if(fail > 0)
							{
								dg.setEndTime(new Date());
								dg.setCheckResult(203);
								diagnosisService.updatebyselective(dg);
								js.setCode(0);
								js.setMessage("诊断成功！诊断结果为失败！");
								return js;
							}
						}else{
							js.setMessage("诊断项目有误！");
							return js;
						}
					}else{
						js.setMessage("诊断分值有误！");
						return js;
					}
				}
			}		
			
			return js;
		}


}
