package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.Log; 
import com.tianyi.yw.model.LogType;
import com.tianyi.yw.service.LogService; 

@Scope("prototype")
@Controller
@RequestMapping("/log")
public class LogAction extends BaseAction 
{
	
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
	@RequestMapping(value = "/logList.do",method=RequestMethod.GET)
	public String logList(Log log,LogType logType,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
	{ 
		//内容，描述
		if (log.getSearchName() != null	&& log.getSearchName().length() > 0) 
		{
			String searchName = new String(log.getSearchName().getBytes("iso8859-1"), "utf-8");
			log.setSearchName(searchName);			
		}
		//时间
		if (log.getSearchTimes() != null	&& log.getSearchTimes().length() > 0) 
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
			Date date1;
			try {
				date1 = sdf.parse(log.getSearchTimes());
				log.setSearchTime(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//类型
		if (log.getSearchTypeIds() != null	&& log.getSearchTypeIds().length() > 0) 
		{
			int date2 = Integer.parseInt(log.getSearchTypeIds());
			log.setSearchTypeId(date2);			
		}
		//分页
		if (log.getPageNo() == null)
			log.setPageNo(1);
		log.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		int totalCount = 0;
		SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式 
		List<Log> loglist = new ArrayList<Log>();
		List<LogType> logTypelist = new ArrayList<LogType>();
		try{
			logTypelist =  logService.getLogTypeList(logType);
			loglist =  logService.getLogList(log);
			totalCount = logService.getLogCount(log);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
		
		for(Log t:loglist)
		{
            t.setCreateTimes( Format.format( t.getCreateTime() ));
		    
		}
		log.setTotalCount(totalCount); 
		request.setAttribute("Log", log); 
		request.setAttribute("Loglist", loglist); 
		request.setAttribute("LogTypelist", logTypelist); 
		return "web/log/logList";
	}
	
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrUpdateLog.do", method=RequestMethod.POST)
	public JsonResult<Log> SaveOrUpdateTask(Log log,HttpServletRequest request, HttpServletResponse response)
	{
		JsonResult<Log> js = new JsonResult<Log>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败!");
		try {
			if (log.getId() == null || log.getId() == 0) 
			{
				log.setId(0);
			}
			
			if (log.getContent() != null) 
			{
				Log p = new Log();
				p.setContent(log.getContent());
				if (log.getId() > 0) 
				{
					p.setId(log.getId());
				}
				List<Log> lc = logService.getExistLog(p);
			if(lc.size()==0)
			{   
				//获取createTime
				log.setCreateTime(new Date()) ; 

				logService.saveOrUpdateLog(log);
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
	
	
}


