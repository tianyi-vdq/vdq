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
import com.tianyi.yw.model.Device; 
import com.tianyi.yw.service.DeviceService;

@Scope("prototype")
@Controller
@RequestMapping("/device")
public class DeviceAction  extends BaseAction{

	@Resource(name = "deviceService")
	private DeviceService deviceService;
	/**
	 * 视频点位设备管理
	 * @param device
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deviceList.do")
	public String deviceList(
			Device device,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (device.getPageNo() == null)
			device.setPageNo(1);
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		List<Device> devicelist = deviceService.getDeviceList(device);
		 
		int totalCount = deviceService.getDeviceCount(device);
		 
		device.setTotalCount(totalCount); 
		request.setAttribute("Device", device); 
		request.setAttribute("Devicelist", devicelist); 
		return "web/device/deviceList";
	}	
	/**
	 * 视频点位设备管理
	 * @param device
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deviceInfo.do")
	public String deviceInfo(
			Device device,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		request.setAttribute("Device", device);  
		return "web/device/deviceInfo";
	}	
	
	/**
	 * 视频点位设备管理
	 * @param device
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deviceStatus.do")
	public String deviceStatus(
			Device device,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		request.setAttribute("Device", device);  
		return "web/device/deviceStatus";
	}	
}
