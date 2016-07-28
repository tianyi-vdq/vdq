package com.tianyi.yw.web.controller;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.Device;  
import com.tianyi.yw.model.DeviceStatus; 
import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem; 
import com.tianyi.yw.service.AreaService;
import com.tianyi.yw.service.DeviceService;

@Scope("prototype")
@Controller
@RequestMapping("/device")
public class DeviceAction  extends BaseAction{

	@Resource
	private DeviceService deviceService;
	@Resource
	private AreaService areaService;
	/**
	 * 视频点位设备管理
	 * @param device
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deviceList.do", method=RequestMethod.GET)
	public String deviceList(
			Device device,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 
		//判断搜索栏是否为空，不为空则转为utf-8编码
		if (device.getSearchName() != null
				&& device.getSearchName().length() > 0) {
			String searchName = new String(device.getSearchName().getBytes("iso8859-1"), "utf-8");
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
		return "web/device/deviceList";
	}	
	
	/**
	 * 设备状态列表
	 * @param deviceStatus
	 * @param request
	 * @param response
	 * @return web/device/deviceStatus
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deviceStatus.do", method=RequestMethod.GET)
	public String deviceStatusList(
			DeviceStatus deviceStatus,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 	

		if(deviceStatus.getSearchPointNumber() != null && deviceStatus.getSearchPointNumber().length() > 0)
		{
			String pointNumber = URLDecoder.decode(deviceStatus.getSearchPointNumber(),"utf-8"); 
			//String pointNumber = new String(deviceStatus.getSearchPointNumber().getBytes("iso8859-1"), "utf-8");
			deviceStatus.setSearchPointNumber(pointNumber);
		}
		
		if (deviceStatus.getPageNo() == null)
			deviceStatus.setPageNo(1);
		deviceStatus.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<DeviceStatus> deviceStatuslist = new ArrayList<DeviceStatus>();
		int totalCount =  0;
		try{			
			deviceStatuslist =  deviceService.getDeviceStatusList(deviceStatus);
			totalCount = deviceService.getDeviceStatusCount(deviceStatus); 
		}catch(Exception ex){ 
			ex.printStackTrace();
		}	
		deviceStatus.setTotalCount(totalCount); 
		request.setAttribute("DeviceStatus", deviceStatus); 
		request.setAttribute("DeviceStatuslist", deviceStatuslist); 
		return "web/device/deviceStatus";
	}		

	/** 
	 * 新增,编辑设备
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrUpdatePoint.do", method=RequestMethod.POST)
	public JsonResult<Device> SaveOrUpdateDevice(Device device,
			HttpServletRequest request, HttpServletResponse response){
		//新建一个json对象 并赋初值
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(new Integer(1));
		js.setMessage("保存失败!");
		try {
			//如为新增，则给id置0
			if (device.getId() == null || device.getId() == 0) {
				device.setId(0);
				device.setFlag(0);
			} 
			Device d = new Device();
			d.setPointNumber(device.getPointNumber());
			d.setPlatformId(device.getPlatformId());
			//如为编辑，则给新建device对象赋传来的设备id值
			if (device.getId() > 0) {
				d.setId(device.getId());
			}
			//根据设备编号和id去数据库匹配，如编辑，则可以直接保存；如新增，则需匹配设备编号是否重复
			List<Device> lc = deviceService.getExistDevicePoint(d);
			if (lc.size() == 0) {  
				deviceService.saveOrUpdateDevicepoint(device);
				js.setCode(new Integer(0));
				js.setMessage("保存成功!");
			} else {
				js.setMessage("设备编号已存在!");
			} 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	/**
	 * 点位设备信息编辑，新增
	 */
	@RequestMapping(value="/deviceInfo.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String editPointDevice(
			@RequestParam(value="pointId", required = false)Integer pointId,
			HttpServletRequest req,HttpServletResponse res){
		//判断是设备新增还是设备编辑，新增，不给deviceInfo界面传值；编辑，需要deviceInfo界面传值
		if(pointId != null && pointId != 0){
			Device  device = new Device();
			try{
			device = deviceService.getPointDeviceById(pointId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			req.setAttribute("Device", device);
		}
		return "web/device/deviceInfo";
	} 

	@ResponseBody
	@RequestMapping(value = "/jsonLoadStopOrStartDevice.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Device> stopOrStartDevice(
			@RequestParam(value = "deviceId", required = false) Integer deviceId,
			@RequestParam(value = "flag", required = false) Integer flag,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Device> js = new JsonResult<Device>();
		Device d = new Device();
		List<DeviceGroup> deviceGroup = new ArrayList<DeviceGroup>();
		DeviceGroupItem dg = new DeviceGroupItem();
		dg.setDeviceId(deviceId);
		if(flag == 1){
			js.setMessage("启用失败!");
			d.setFlag(0);
		}else{
			js.setMessage("停用失败!");
			d.setFlag(1);
		}
		js.setCode(1);
		d.setId(deviceId);
		try{
			if(flag == 1){
				deviceService.stopOrStartDeviceById(d);
				js.setMessage("启用成功!");
				js.setCode(0);
			}else{
				deviceGroup = deviceService.getExistGroupByDeviceId(dg);
				if(deviceGroup.size() == 0){
					deviceService.stopOrStartDeviceById(d);
					js.setMessage("停用成功!");
					js.setCode(0);
				}else{
					js.setMessage("该设备属于其他分组成员，不能停用!");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return js;
	} 
	
	/**
	 * 设备状态 
	 * @param deviceNumber  设备编号参数
	 * @param statusId  设备状态参数 
	 * @param pageNo  当前页数
	 * @param request
	 * @param response
	 * @return web/device/jsonloadDeviceStatus
	 * @throws UnsupportedEncodingException
	 */

	@ResponseBody
	@RequestMapping(value = "/jsonloadDeviceStatus.do", method=RequestMethod.GET)
	public JsonResult<DeviceStatus> jsonloadDeviceStatus(
			@RequestParam(value = "deviceNumber", required = false) String deviceNumber,
			@RequestParam(value = "statusId", required = false) Integer statusId,
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 	
		JsonResult<DeviceStatus> js = new JsonResult<DeviceStatus>();
		DeviceStatus deviceStatus = new DeviceStatus();
		if(deviceNumber != null && deviceNumber.length() > 0)
		{
			//String pointNumber = URLDecoder.decode(deviceStatus.getSearchPointNumber(),"utf-8"); 
			//String pointNumber = new String(deviceNumber.getBytes("iso8859-1"), "utf-8");
			deviceStatus.setSearchPointNumber(deviceNumber);
		}
		if(statusId != null && statusId>0){
			deviceStatus.setSearchStatusId(statusId); 
		} 
		if (pageNo == null){
			deviceStatus.setPageNo(1);
		}
		else{
			deviceStatus.setPageNo(pageNo);
		}
		deviceStatus.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<DeviceStatus> deviceStatuslist = new ArrayList<DeviceStatus>();
		int totalCount =  0;
		try{			
			deviceStatuslist =  deviceService.getDeviceStatusList(deviceStatus);
			totalCount = deviceService.getDeviceStatusCount(deviceStatus); 
			deviceStatus.setTotalCount(totalCount); 
			deviceStatus.setSearchPointNumber(null);
			js.setCode(0);
			js.setMessage("获取设备状态列表成功");
			js.setObj(deviceStatus);
			js.setList(deviceStatuslist); 
		}catch(Exception ex){ 
			ex.printStackTrace();
		}	
		return js;
	}	
}
