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
@RequestMapping("/statistic")
public class StatisticAction  extends BaseAction{

	/**
	 * 统计模块 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/faultStatistic.do")
	public String deviceList( 
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		return "web/statistic/faultStatistic";
	}	
}
