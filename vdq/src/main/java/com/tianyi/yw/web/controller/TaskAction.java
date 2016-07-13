package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
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
			String searchName = new String(task.getSearchName().getBytes("iso8859-1"), "utf-8");
			task.setSearchName(searchName);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
		if ((task.getStartedTimes() != null
				&& !task.getStartedTimes().equals(""))
				|| (task.getEndTimes() != null && !task.getEndTimes().equals(""))) {
			if(!(task.getStartedTimes() != null
					&& !task.getStartedTimes().equals(""))){
				task.setStartedTimes("2000-01-01");
			}
			if(!(task.getEndTimes() != null && !task.getEndTimes().equals(""))){
				task.setEndTimes("2050-01-01");
			}
			Date date1 = sdf.parse(task.getStartedTimes());
			Date date2 = sdf.parse(task.getEndTimes());
			if (date1.before(date2)) {	
											
				task.setStartedTime(date1);
				Calendar c = Calendar.getInstance();
				c.setTime(date2);				
				c.add(Calendar.DATE, 1);
				task.setEndTime(c.getTime());
			}
		}
		if (task.getPageNo() == null)
			task.setPageNo(1);
		task.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		List<Task> tasklist = new ArrayList<Task>();
		int totalCount = 0;
		int flagCount = 0;
		try {
			SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			tasklist = taskService.getTaskList(task);
			totalCount = taskService.getTaskCount(task);
			flagCount = taskService.getRunTaskCount(task);			
			for(Task t : tasklist) {
				t.setCreateTimes(Format.format(t.getCreateTime()));
				t.setStartTimes(Format.format(t.getStartTime()));				
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
			String message = null;
			//执行中的任务只能停止，不可编辑
			if(task.getId() != 0 && task.getId() != null){
				Task t = new Task();
				t = taskService.getTaskById(task.getId());
				task.setName(t.getName());
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
				    	message = "当前已有任务执行，启动失败！";
				    }else if(flagCount == 0)
				    {
				    	task.setFlag(1);
				    }
				}
				List<Task> lc = taskService.getExistTask(p);
				if (lc.size() == 0) {
					task.setCreateTime(new Date());
					
					taskService.saveOrUpdateTask(task); 
					js.setCode(new Integer(0));
					if(message != null)
					{
						js.setMessage("保存成功!"+message);
					}else
					{
						js.setMessage("保存成功!");
					}
					
				} else {
					js.setMessage("任务已存在!");
				}
			} else {
				js.setMessage("任务名称不能为空!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return js;
	}

	/**
	 * 任务新建
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
		List<String> list = new ArrayList<String>(); //临时集合，排序任务执行时间
		task.setId(id);
		taskTime.setTaskId(id);
		if(task.getId()>0){
			int timeCount = 0;
			task = taskService.getTaskById(task.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//设置所有执行时间String，启动时间
			if(task != null){
				taskTimelist = taskService.getTaskTimeList(taskTime);
				timeCount = taskService.getTaskTimeCount(taskTime);
				task.setRunTimes(timeCount);
				String at = null;
				String startTime = null;
				if(taskTimelist.size() != 0){
					for(TaskTime t:taskTimelist){										
						/*if(!task.getStartTime().before(t.getStartTime())){
							task.setStartTime(t.getStartTime());
						}*/
						startTime = sdf.format(t.getStartTime());
						list.add(startTime);
						/*if(at == null){
							at = sdf.format(t.getStartTime());
						}else{
							at += "," + sdf.format(t.getStartTime());
						}*/
					}
					Collections.sort(list); //对任务执行时间排序
					//Collections.reverse(list);
					//task.setAllTimes(at);
					//task.setStartTimes(sdf.format(task.getStartTime()));	
					req.setAttribute("timeList",list);
				}
								
			}
		}  
		taskItemTypelist = taskService.getTaskItemTypeList(taskItemType);
		req.setAttribute("Task", task);
		req.setAttribute("TaskItemTypelist", taskItemTypelist);
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
