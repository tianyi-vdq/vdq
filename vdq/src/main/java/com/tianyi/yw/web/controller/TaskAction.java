package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Scope("prototype")
@Controller
@RequestMapping("/task")
public class TaskAction {
	/**
	 * 视频点位检测参数设置
	 *  
	 * @param request
	 * @param response
	 * @return 
	 */
	@RequestMapping(value = "/taskList.do")
	public String taskList( 
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		 
		return "web/parame/taskList";
	}	
}
