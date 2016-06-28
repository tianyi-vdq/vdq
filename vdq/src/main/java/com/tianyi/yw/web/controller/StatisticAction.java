package com.tianyi.yw.web.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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
import com.tianyi.yw.model.DeviceStatus;
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
	//@RequestMapping(value = "/faultStatistic.do")
	@RequestMapping(value = "/faultStatistic.do", method=RequestMethod.GET)
	public String deviceStatusList(
			DeviceStatus deviceStatus,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 	
		if (deviceStatus.getPageNo() == null)
			deviceStatus.setPageNo(1);
		deviceStatus.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<DeviceStatus> deviceStatuslist = new ArrayList<DeviceStatus>();
		int totalCount =  0;
		try{			
			deviceStatuslist =  deviceService.getDeviceStatusList(deviceStatus);
			totalCount = deviceService.getDeviceStatusCount(deviceStatus);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");		
			for(DeviceStatus ds:deviceStatuslist )
			{				
				ds.setCreateTimes(sdf.format(ds.getCreateTime()));
				ds.setRecordTimes(sdf.format(ds.getRecordTime()));			
			}
		}catch(Exception ex){ 
			ex.printStackTrace();
		}	
		deviceStatus.setTotalCount(totalCount); 
		request.setAttribute("DeviceStatus", deviceStatus); 
		request.setAttribute("DeviceStatuslist", deviceStatuslist); 
		return "web/statistic/faultStatistic";
	}	
}
