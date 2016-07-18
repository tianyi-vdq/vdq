package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.fabric.xmlrpc.base.Array;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.model.TaskItem;
import com.tianyi.yw.model.TaskItemType;
import com.tianyi.yw.model.TaskTime;
import com.tianyi.yw.service.DignosisService;
import com.tianyi.yw.service.TaskService;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;

@Scope("prototype")
@Controller
@RequestMapping("/task")
public class TaskAction extends BaseAction {

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "dignosisService")
	private DignosisService dignosisService;
	/**
	 * 任务列表
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * @return web/task/taskList
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/taskList.do", method = RequestMethod.GET)
	public String taskList(Task task, TaskItem taskItem,
			TaskItemType taskItemType, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException,
			ParseException {
		// searchName
		if (task.getSearchName() != null && task.getSearchName().length() > 0) {
			String searchName = URLDecoder.decode(task.getSearchName(),"utf-8"); 
			task.setSearchName(searchName);
//			String searchName = new String(task.getSearchName().getBytes("iso8859-1"), "utf-8");
//			task.setSearchName(searchName);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟  
		if (task.getPageNo() == null)
			task.setPageNo(1);
		task.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		List<Task> tasklist = new ArrayList<Task>();
		List<TaskTime> taskTimelist = new ArrayList<TaskTime>();
		TaskTime taskTime = new TaskTime();
		int totalCount = 0;
		int flagCount = 0; 
		try { 
			tasklist = taskService.getTaskList(task);
			totalCount = taskService.getTaskCount(task);
			flagCount = taskService.getRunTaskCount(task);			
			for(Task t : tasklist) { 
				taskTime.setTaskId(t.getId());
				taskTimelist = taskService.getTaskTimeList(taskTime);
				t.setRunTimes(taskTimelist.size()); 			
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        task.setFlagCount(flagCount);
		task.setTotalCount(totalCount);
		request.setAttribute("Task", task);
		request.setAttribute("Tasklist", tasklist);
		return "web/task/taskList";
	}

	/**
	 * 任务保存
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * @return js
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrUpdateTask.do", method = RequestMethod.POST)
	public JsonResult<Task> SaveOrUpdateTask(Task task,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败!");		
		try {
			if (task.getId() == null || task.getId() == 0) {
				task.setId(0);
			}
			if (task.getStartTimes() != null) {
				String startTimes = task.getStartTimes();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				task.setStartTime(sdf.parse(startTimes));
			}
			if("".equals(task.getItemTypeId()) || task.getItemTypeId() == null){
				js.setMessage("保存失败！至少选择一个诊断项目！");
				return js;
			}
			String message = "";
			//执行中的任务只能停止，不可编辑
			if(task.getId() != 0 && task.getId() != null){
				Task t = new Task();
				t = taskService.getTaskById(task.getId());
				//task.setName(t.getName());
				if(t.getFlag() == 1 && task.getFlag() == 1){
					js.setMessage("任务执行中不可编辑！");
					return js;
				}
			}
			if (task.getName() != null ) {
				Task p = new Task();
				String name = task.getName();
				p.setName(name);
				if (task.getId() > 0) {
					p.setId(task.getId());
				}
				//启动判断
				if(task.getFlag() == 1)
				{			  
				    int flagCount = taskService.getRunTaskCount(task);
				    if(flagCount == 1)
				    {
				    	task.setFlag(0);
				    	message = "当前已有任务执行，不能立即执行任务！";
				    } 
				} 
				task.setCreateTime(new Date());
				
				taskService.saveOrUpdateTask(task); 
				js.setCode(new Integer(0)); 
				js.setMessage("保存成功!"+message);
				return js; 
			} else {
				js.setMessage("任务名称不能为空!");
				return js;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return js;
	}

	/**
	 * 任务新建/编辑
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * {@value id}
	 * @return web/task/taskInfo
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/taskInfo.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String editTask(
			@RequestParam(value = "id", required = false) Integer id,
			HttpServletRequest req, HttpServletResponse res) throws ParseException {
		TaskItemType taskItemType = new TaskItemType();
		List<TaskItemType> taskItemTypelist = new ArrayList<TaskItemType>();
		Task task = new Task();
		TaskTime taskTime = new TaskTime();
		List<TaskTime> taskTimelist = new ArrayList<TaskTime>();
		task.setId(id);
		taskTime.setTaskId(id);
		int timeCount = 0; 
		if(task.getId()>0){ 
			task = taskService.getTaskById(task.getId());
			
			//首次执行时间，其他执行时间
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			taskTimelist = taskService.getTaskTimeList(taskTime);
			if(taskTimelist.size()>0){
				timeCount = taskTimelist.size();
				task.setFirstTimes(sdf.format(taskTimelist.get(0).getStartTime()));
				taskTimelist.remove(0);  
				String st = "";
				for(TaskTime tt : taskTimelist){
					st += sdf.format(tt.getStartTime())+",";
				} 
				task.setStartedTimes(st);
			} 
		} 
		taskItemTypelist = taskService.getTaskItemTypeList(taskItemType);
		req.setAttribute("Task", task);
		req.setAttribute("TaskItemTypelist", taskItemTypelist); 
		req.setAttribute("TimeListCount", timeCount);
		return "web/task/taskInfo";

	}


	

	 /**
	 * 任务执行
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
			@RequestParam(value = "id", required = false) Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("任务启动失败!");		
		try {			
			Task task = taskService.getTaskById(id);
			//诊断空项目判断
			if(task.getItemTypeId() == null){
				js.setMessage("请选择至少一个诊断项目！");
				return js;
			}
			task.setFlag(1);
			taskService.saveOrUpdateTask(task);			
			js.setCode(new Integer(0));
			js.setMessage("任务启动成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return js;
	}
	

	 /**
	 * 任务停止
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * {@value id}
	 * @return js
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonloadTaskStop.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Task> jsonloadTaskStop(
			@RequestParam(value = "id", required = false) Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("任务终止失败!");
		try {
			Task task = taskService.getTaskById(id);
			task.setFlag(0);
			taskService.saveOrUpdateTask(task);
			dignosisService.clear();
			js.setCode(new Integer(0));
			js.setMessage("任务终止成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	 /**
	 * 删除执行
	 * 
	 * @param task
	 * @param request
	 * @param response
	 * {@value id}
	 * @return js
	 */
	@ResponseBody
	@RequestMapping(value = "/jsondeleteTaskById.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Task> jsondeleteTaskById(
			@RequestParam(value = "id", required = true) Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("删除失败!");
		try { 
			taskService.deleteTaskById(id);
			js.setCode(new Integer(0));
			js.setMessage("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	
}
