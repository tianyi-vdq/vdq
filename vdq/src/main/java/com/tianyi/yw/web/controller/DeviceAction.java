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
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
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
	@ResponseBody
	@RequestMapping(value = "/deviceList.do", method=RequestMethod.GET)
	public String deviceList(
			Device device,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		if (device.getSearchName() != null
				&& device.getSearchName().length() > 0) {
			String searchName = new String(device.getSearchName().getBytes(
					"iso8859-1"), "utf-8");
			device.setSearchName(searchName);			
		}
		if (device.getPageNo() == null)
			device.setPageNo(1);
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<Device> devicelist = new ArrayList<Device>();
		int totalCount =  0;
		try{
			devicelist =  deviceService.getDeviceList(device);
			totalCount = deviceService.getDeviceCount(device);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
		 
		device.setTotalCount(totalCount); 
		request.setAttribute("Device", device); 
		request.setAttribute("Devicelist", devicelist); 
		return "web/device/deviceList";
	}	
	/**
	 * 新增设备
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrUpdatePoint.do", method=RequestMethod.POST)
	public JsonResult<Device> SaveOrUpdateDevice(Device device,
			HttpServletRequest request, HttpServletResponse response){
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败!");
		try {
			if (device.getId() == null || device.getId() == 0) {
				device.setId(0);
			}
			if (device.getPointNumber() != null) {
				Device p = new Device();
				p.setPointNumber(device.getPointNumber());
				if (device.getId() > 0) {
					p.setId(device.getId());
				}
				List<Device> lc = deviceService.getExistProjectPoint(p);
				if (lc.size() == 0) {
					deviceService.saveOrUpdateProjectpoint(device);
					js.setCode(new Integer(0));
					js.setMessage("保存成功!");
				} else {
					js.setMessage("点位编号已存在!");
				}
			} else {
				js.setMessage("点位编号不能为空!");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	
	
	
}
