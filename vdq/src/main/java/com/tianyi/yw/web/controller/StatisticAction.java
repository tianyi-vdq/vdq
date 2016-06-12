package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tianyi.yw.common.utils.Constants;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.service.DeviceService;

@Scope("prototype")
@Controller
@RequestMapping("/statistic")
public class StatisticAction  extends BaseAction{
	
	@Resource(name = "deviceService")
	private DeviceService deviceService;

	/**
	 * 统计模块 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	/*@RequestMapping(value = "/faultStatistic.do")
	public String deviceList( 
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		return "web/statistic/faultStatistic";
	}	*/
	
	@RequestMapping(value = "/faultStatistic.do", method=RequestMethod.GET)
	public String deviceList(
			Device device,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		//判断搜索栏是否为空，不为空则转为utf-8编码
		if (device.getSearchName() != null
				&& device.getSearchName().length() > 0) {
			String searchName = new String(device.getSearchName().getBytes(
					"iso8859-1"), "utf-8");
			device.setSearchName(searchName);			
		}
		//设置页面初始值及页面尺寸
		if (device.getPageNo() == null)
			device.setPageNo(1);
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<Device> devicelist = new ArrayList<Device>();
		int totalCount =  0;
		try{
			//去t_device取满足要求的数据
			devicelist =  deviceService.getDeviceList(device);
			//t_device的记录数
			totalCount = deviceService.getDeviceCount(device);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
		//通过request绑定对象传到前台
		device.setTotalCount(totalCount); 
		request.setAttribute("Device", device); 
		request.setAttribute("Devicelist", devicelist); 
		return "web/statistic/faultStatistic";
	}	
}
