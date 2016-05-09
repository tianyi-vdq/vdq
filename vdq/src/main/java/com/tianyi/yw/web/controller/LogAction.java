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
import com.tianyi.yw.model.Log; 
import com.tianyi.yw.service.LogService; 

@Scope("prototype")
@Controller
@RequestMapping("/log")
public class LogAction extends BaseAction {
	
	@Resource(name = "logService")
	private LogService logService;
	
	/**
	 * 
	 * 日志管理管理
	 * @param log
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/logList.do")
	public String logList(
			Log log,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (log.getPageNo() == null)
			log.setPageNo(1);
		log.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		List<Log> loglist = logService.getLogList(log);
		 
		int totalCount = logService.getLogCount(log);
		 
		log.setTotalCount(totalCount); 
		request.setAttribute("Log", log); 
		request.setAttribute("Loglist", loglist); 
		return "web/log/logList";
	}
}
