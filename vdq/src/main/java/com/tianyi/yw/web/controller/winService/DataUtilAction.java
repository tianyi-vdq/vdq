package com.tianyi.yw.web.controller.winService;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.LogService;
import com.tianyi.yw.service.TaskService;

@Scope("prototype")
@Controller
@RequestMapping("/dataUtil")
public class DataUtilAction {
	
	@Resource(name = "taskService")
	private TaskService taskService;
	
	@Resource(name = "logService")
	private LogService logService;
	

	
	/** 
	 * 日志写入接口
	 * @param request
	 * @param response
	 * @param stringLog
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonWriteLog.do")
	public JsonResult<Log> SaveLog(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "log", required = false) String stringLog) {
		JsonResult<Log> js = new JsonResult<Log>();
		js.setCode(new Integer(1));
		js.setMessage("写入日志失败!");
		String[] arrLog = stringLog.split(",");
		Log log = new Log();		
		try {
			String content = new String(arrLog[0].getBytes("iso8859-1"), "utf-8");
			log.setContent(content);
		} catch (UnsupportedEncodingException e1) {
			
			e1.printStackTrace();
		}	
		log.setTypeId(Integer.parseInt(arrLog[1]));		
		log.setCreateTimes(arrLog[2]);		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			log.setCreateTime(sdf.parse(log.getCreateTimes()));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		if(arrLog.length >= 3)
		{
	    	log.setDescription(arrLog[3]);
	    	try {			
		    	String description = new String(arrLog[3].getBytes("iso8859-1"), "utf-8");
			    log.setContent(description);
	        	} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
		    }
		}
		if(log.getContent() != null && log.getContent() != ""
				&& log.getCreateTime() != null && log.getTypeId() != null && log.getTypeId() != '0')
		{
			logService.saveOrUpdateLog(log);
			js.setCode(new Integer(0));
			js.setMessage("保存日志成功！");
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
	

}
