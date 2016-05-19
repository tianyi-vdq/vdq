package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.Area; 

@Scope("prototype")
@Controller
@RequestMapping("/area")
public class AreaAction  extends BaseAction {
	/**
	 * 区域管理
	 * @param area
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/areaList.do")
	public String areaList(
			Area area,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (area.getPageNo() == null)
			area.setPageNo(1);
		area.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		return "web/area/areaList";
	}	
	/**
	 * 区域信息管理：新增、编辑
	 * @param area
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/areaInfo.do")
	public String areaInfo(
			Area area,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (area.getPageNo() == null)
			area.setPageNo(1);
		area.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		return "web/area/areaInfo";
	}	
}
