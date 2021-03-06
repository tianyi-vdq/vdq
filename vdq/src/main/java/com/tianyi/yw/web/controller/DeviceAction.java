package com.tianyi.yw.web.controller;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.common.utils.StringUtil;
import com.tianyi.yw.model.Device;  
import com.tianyi.yw.model.DeviceStatus; 
import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem; 
import com.tianyi.yw.model.DeviceStatusRecord;
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
		if(StringUtil.isEmpty(device.getStartTime())){
			device.setStartTime(null);
		}		
		if(StringUtil.isEmpty(device.getEndTime())){
			device.setEndTime(null);
		}
		try{
			//去t_device取满足要求的数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟  
			devicelist =  deviceService.getDeviceList(device); 
			for(Device d : devicelist){
				if(d.getFlag()==1){
					d.setLockTimes(sdf.format(d.getLockTime()));
				}else if(d.getFlag() == 2){
					d.setDelTimes(sdf.format(d.getDelTime()));
				} 
			}
			//t_device的记录数
			totalCount = deviceService.getDeviceCount(device);
		}catch(Exception ex){ 
			ex.printStackTrace();
		}
		String jspStr = "web/device/deviceList";
		if(device.getFlag() ==1){
			 jspStr = "web/device/deviceLockList";
		}else if(device.getFlag() == 2){
			 jspStr = "web/device/deviceDelList";
		}
		//通过request绑定对象传到前台
		device.setTotalCount(totalCount); 
		request.setAttribute("Device", device); 
		request.setAttribute("Devicelist", devicelist); 
		return jspStr;
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟  
		if (deviceStatus.getPageNo() == null)
			deviceStatus.setPageNo(1);
		deviceStatus.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<DeviceStatus> deviceStatuslist = new ArrayList<DeviceStatus>();
		int totalCount =  0;
		try{			
			deviceStatuslist =  deviceService.getDeviceStatusList(deviceStatus);
			for(DeviceStatus ds :deviceStatuslist){
				ds.setRecordTimes(sdf.format(ds.getRecordTime())); 
				ds.setAreaName(ds.getPointId().substring(0, 3));
			}
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
	 * 设备则诊断状态历史纪录列表
	 * @param deviceStatus
	 * @param request
	 * @param response
	 * @return web/device/deviceStatus
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/deivceStatusRecord.do", method=RequestMethod.GET)
	public String deivceStatusRecord(
			DeviceStatusRecord deviceStatusRecord,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{ 	

		if(deviceStatusRecord.getSearchPointNumber() != null && deviceStatusRecord.getSearchPointNumber().length() > 0)
		{
			String pointNumber = URLDecoder.decode(deviceStatusRecord.getSearchPointNumber(),"utf-8"); 
			//String pointNumber = new String(deviceStatus.getSearchPointNumber().getBytes("iso8859-1"), "utf-8");
			deviceStatusRecord.setSearchPointNumber(pointNumber);
		}

        if(StringUtil.isEmpty(deviceStatusRecord.getSchBeginTime())){
        	deviceStatusRecord.setSchBeginTime(null);
        }else{
        	deviceStatusRecord.setSchBeginTime(deviceStatusRecord.getSchBeginTime()+" 00:00:00");
        }
        if(StringUtil.isEmpty(deviceStatusRecord.getSchEndTime())){
        	deviceStatusRecord.setSchEndTime(null);
        }else{
        	deviceStatusRecord.setSchEndTime(deviceStatusRecord.getSchEndTime()+" 23:59:59");
        }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟  
		if (deviceStatusRecord.getPageNo() == null)
			deviceStatusRecord.setPageNo(1);
		deviceStatusRecord.setPageSize(Constants.DEFAULT_PAGE_SIZE);  
		List<DeviceStatusRecord> deviceStatuslist = new ArrayList<DeviceStatusRecord>();
		int totalCount =  0;
		try{			
			deviceStatuslist =  deviceService.getDeviceStatusRecordList(deviceStatusRecord);
			for(DeviceStatusRecord ds :deviceStatuslist){
				ds.setRecordTimes(sdf.format(ds.getRecordTime())); 
			}
			totalCount = deviceService.getDeviceStatusRecordCount(deviceStatusRecord); 
		}catch(Exception ex){ 
			ex.printStackTrace();
		}	
		deviceStatusRecord.setTotalCount(totalCount); 
		request.setAttribute("DeviceStatusRecord", deviceStatusRecord); 
		request.setAttribute("DeviceStatusRecordlist", deviceStatuslist); 
		return "web/device/deviceStatusRecord";
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
			@RequestParam(value = "description", required = false) String description,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		JsonResult<Device> js = new JsonResult<Device>();
		Device d = new Device();
		//List<DeviceGroup> deviceGroup = new ArrayList<DeviceGroup>();
		DeviceGroupItem dg = new DeviceGroupItem();
		dg.setDeviceId(deviceId);
		if(flag == 1){
			js.setMessage("启用失败!");
			d.setLockTime(null);
			d.setDescription(null);
			d.setFlag(0);
		}else if(flag ==0){
			String desc = URLDecoder.decode(description,"utf-8"); 
			js.setMessage("停用失败!");
			d.setLockTime(new Date());
			d.setDescription(desc);
			d.setFlag(1);
		}else if(flag == 2){
			String desc = URLDecoder.decode(description,"utf-8"); 
			js.setMessage("删除失败!");
			d.setDescription(desc);
			d.setDelTime(new Date());
			d.setFlag(2);
		}else if(flag == 3){
			js.setMessage("恢复失败!");
			d.setDelTime(null);
			d.setDescription(null);
			d.setFlag(0);
		}
		js.setCode(1);
		d.setId(deviceId);
		try{
			deviceService.stopOrStartDeviceById(d);
			js.setCode(0);
			if(flag == 1){
				js.setMessage("启用成功!");
			}else if(flag == 0){ 
				js.setMessage("停用成功!"); 
			}else if(flag == 2){
				js.setMessage("删除成功!"); 
			}else{
				js.setMessage("恢复成功!"); 
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
