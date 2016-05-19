
package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.TaskService;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;


@Scope("prototype")
@Controller
@RequestMapping("/task")
public class TaskAction extends BaseAction
{

	@Resource(name = "taskService")
	private TaskService taskService;

	/**
	 * 任务管理
	 * @param task
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/taskList.do", method=RequestMethod.GET)
	public String taskList(Task task,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ParseException
	{ 
	
		
	   //searchName
		if (task.getSearchName() != null&& task.getSearchName().length() > 0) 
		{
			String searchName = new String(task.getSearchName().getBytes("iso8859-1"), "utf-8");
			task.setSearchName(searchName);			
		}


		if(task.getStartedTimes() != null && !task.getStartedTimes().equals("") 
				&& task.getEndTimes() != null &&!task.getEndTimes().equals(""))
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
			Date date1 = sdf.parse(task.getStartedTimes());  
			Date date2 = sdf.parse(task.getEndTimes());  
			if(date1.before(date2))
		    {
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
		try
		{   
			SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式 
			tasklist = taskService.getTaskList(task);
			totalCount = taskService.getTaskCount(task);
			for(Task t:tasklist)
			{
	            t.setCreateTimes( Format.format( t.getCreateTime() ));
			    t.setStartTimes( Format.format( t.getStartTime() )); 
		
			}
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}

		task.setTotalCount(totalCount); 
		request.setAttribute("Task", task); 
		request.setAttribute("Tasklist",tasklist); 
		return "web/task/taskList";
	}


	
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrUpdateTask.do", method=RequestMethod.POST)
	public JsonResult<Task> SaveOrUpdateTask(Task task,HttpServletRequest request, HttpServletResponse response)
	{
		JsonResult<Task> js = new JsonResult<Task>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败!");
		try {
			if (task.getId() == null || task.getId() == 0) 
			{
				task.setId(0);
			}
			if(task.getStartTimes() != null){
				String startTimes = task.getStartTimes();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				task.setStartTime(sdf.parse(startTimes));
			}
			if (task.getName() != null) 
			{
				Task p = new Task();
				String name = new String(task.getName().getBytes("iso8859-1"), "utf-8");
				p.setName(name);
				if (task.getId() > 0) 
				{
					p.setId(task.getId());
				}
				List<Task> lc = taskService.getExistTask(p);
			if(lc.size()==0)
			{   
				//时间格式转换出错
//				SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
    			task.setCreateTime(new Date()) ; 
//				Date date3 = Format.parse(task.getStartTimes());
//				task.setStartTime(date3) ;
				taskService.saveOrUpdateTask(task);
				js.setCode(new Integer(0));
				js.setMessage("保存成功!");
			} else 	{
				js.setMessage("任务已存在!");
			}
			}else {
				js.setMessage("任务名称不能为空!"); 
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	
	@RequestMapping(value="/taskInfo.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String editTask(
			@RequestParam(value="id", required = false)Integer id,
			HttpServletRequest req,HttpServletResponse res){
		Task task = taskService.getTaskById(id);
		req.setAttribute("Task", task);
		return "web/task/taskInfo";
	}
	
	
}

