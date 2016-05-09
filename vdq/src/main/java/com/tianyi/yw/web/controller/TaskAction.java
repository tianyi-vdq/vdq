package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tianyi.yw.common.utils.Constants;
 
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.TaskService;

@Scope("prototype")
@Controller
@RequestMapping("/task")
public class TaskAction extends BaseAction{
	
	@Resource(name = "taskService")
	private TaskService taskService;
	
	/**
	 * 任务管理
	 * @param task
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/taskList.do")
	public String taskList(
			Task task,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (task.getPageNo() == null)
			task.setPageNo(1);
		task.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		List<Task> devicelist = taskService.getTaskList(task);
		 
		int totalCount = taskService.getTaskCount(task);
		 
		task.setTotalCount(totalCount); 
		request.setAttribute("Task", task); 
		request.setAttribute("Tasklist", devicelist); 
		return "web/task/taskList";
	}
}
