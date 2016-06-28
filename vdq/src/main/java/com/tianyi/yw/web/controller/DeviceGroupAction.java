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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.Area;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem;
import com.tianyi.yw.service.AreaService;
import com.tianyi.yw.service.DeviceService;

@Scope("prototype")
@Controller
@RequestMapping("/deviceGroup")
public class DeviceGroupAction extends BaseAction{
	
	@Resource
	private DeviceService deviceService;
	@Resource(name = "areaService")
	private AreaService areaService;
	/**
	 * 分组列表加载
	 * 
	 */
	@RequestMapping(value = "/groupList.do", method=RequestMethod.GET)
	public String groupList(
			Device device,DeviceGroup group,
			@RequestParam(value="groupId", required = false)Integer groupId,
			HttpServletRequest request, HttpServletResponse response) 
					throws UnsupportedEncodingException{
		if(device.getPageNo() == null){
			device.setPageNo(1);
		}
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		int totalCount = 0;
		//页面加载默认加载第一个分组的成员
		if(groupId != null){
			device.setGroupId(groupId);
		}else{
			device.setGroupId(1);
		}
		List<Device> devicelist = new ArrayList<Device>();
		List<DeviceGroup> deviceGroupList = new ArrayList<DeviceGroup>();
		try {
			devicelist = deviceService.getDeviceListByGroupId(device);
			deviceGroupList = deviceService.getGroupList(group);
			totalCount = deviceService.getTotalCountByGroupId(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		device.setTotalCount(totalCount);
		request.setAttribute("device",device);
		request.setAttribute("Devicelist", devicelist);
		request.setAttribute("groupName", deviceGroupList);
		return "web/deviceGroup/groupList";
	}
	/**
	 * 删除分组成员
	 * @param id
	 * @param groupId
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonDeleteMember.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<DeviceGroupItem> deleteMemberById(
			@RequestParam(value = "Id", required = false) Integer id,
			@RequestParam(value = "groupId", required = false) Integer groupId,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<DeviceGroupItem> js = new JsonResult<DeviceGroupItem>();
		js.setCode(new Integer(1));
		js.setMessage("删除失败!");
		DeviceGroupItem dgi = new DeviceGroupItem();
		if(id != null){
			dgi.setDeviceId(id);
		}
		if(groupId != null){
			dgi.setGroupId(groupId);
		}
		try {
			deviceService.deleteMemberById(dgi);
			js.setCode(0);
			js.setMessage("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	/**
	 * 新增，编辑组名界面
	 * @param groupId
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/groupInfo.do", method=RequestMethod.GET)
	public String groupInfo(
			@RequestParam(value="groupId", required = false)Integer groupId,
			HttpServletRequest request, HttpServletResponse response) 
					throws UnsupportedEncodingException{
		DeviceGroup dg = new DeviceGroup();
		if(groupId != 0){
			dg = deviceService.getDeviceGroupById(groupId);
		}else{
			dg.setId(groupId);
		}
		request.setAttribute("group", dg);
		return "web/deviceGroup/groupInfo";
	}
	/**
	 * 新增，编辑组名，返回json结果给前台
	 * @param deviceGroup
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveOrupdateGroup.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<DeviceGroup> saveOrUpdateGroup(
			DeviceGroup deviceGroup,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<DeviceGroup> js = new JsonResult<DeviceGroup>();
		js.setMessage("保存失败!");
		js.setCode(1);
		try {
			if (deviceGroup.getId() == null || deviceGroup.getId() == 0) {
				deviceGroup.setId(0);
			}
			if(deviceGroup.getName() != null){
				DeviceGroup dg = new DeviceGroup();
				dg.setName(deviceGroup.getName());
				if(deviceGroup.getId()>0){
					dg.setId(deviceGroup.getId());
				}
				List<DeviceGroup> ls = deviceService.getExistGroup(dg);
				if(ls.size() == 0){
					deviceService.saveOrUpdateGroup(deviceGroup);
					js.setCode(0);
					js.setMessage("保存成功!");
				}else{
					js.setMessage("组名已存在!");
				}
			}else{
				js.setMessage("组名不能为空!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
		
	}
	/**
	 * 分组成员界面
	 * @param group
	 * @param groupId
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/memberList.do", method=RequestMethod.GET)
	public String memberList(
			Device device,
			@RequestParam(value="groupId", required = false)Integer groupId,
			@RequestParam(value="pageNo", required = false)Integer pageNo,
			HttpServletRequest request, HttpServletResponse response) 
					throws UnsupportedEncodingException{
		try{
			if(groupId != null && groupId != 0){
				DeviceGroup dg = new DeviceGroup();
				dg = deviceService.getDeviceGroupById(groupId);
				request.setAttribute("group", dg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(device.getGroupId() != null){
			device.setGroupId(device.getGroupId());
		}
		if(device.getPageNo() == null){
			device.setPageNo(1);
		}
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		List<Device> devicelist = new ArrayList<Device>();
		List<Device> deviceAllList = new ArrayList<Device>();
		Device d = new Device();
		if(pageNo == null){
			d.setPageNo(1);
		}else{
			d.setPageNo(pageNo);
		}
		d.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		int total = 0;
		int count = 0;
		try {
			devicelist = deviceService.getDeviceListByGroupId(device);
			total = deviceService.getTotalCountByGroupId(device);
			deviceAllList = deviceService.getDeviceListByFlag(d);
			count = deviceService.getDeviceCountByFlag(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		device.setTotalCount(total);
		d.setTotalCount(count);
		request.setAttribute("device", device);
		request.setAttribute("Devicelist", devicelist);
		request.setAttribute("Device", d);
		request.setAttribute("DeviceAlllist", deviceAllList);
		return "web/deviceGroup/memberInfo";
	}
	
	/**
	 * 编辑分组成员界面
	 * @param groupId
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/editMember.do", method=RequestMethod.GET)
	public String editMember(
			@RequestParam(value="groupId", required = false)Integer groupId,
			HttpServletRequest request, HttpServletResponse response) 
					throws UnsupportedEncodingException{
		if(groupId!=null){
			DeviceGroup group = new DeviceGroup();
			group.setId(groupId);
			request.setAttribute("group", group);
		}
		return "web/deviceGroup/editMember";
	}
	/**
	 * 加载区域列表，按照树形结构显示
	 * 异步加载，节点展开时再触发此方法，加载子节点
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadAreaTreeList.do")
	public List<Area> getAreaList(
			@RequestParam(value = "pid", required = false) Integer pid,
			HttpServletRequest request, HttpServletResponse response) {
		Area area = new Area();
		if (pid != null){
			area.setParentId(pid);
		}else {
			area.setParentId(new Integer(0));
		}
		List<Area> list = new ArrayList<Area>();
		list = areaService.getAreaListByParentId(area);
		for(Area a:list){
			a.setText(a.getName());
			a.setState("closed");
			Area areaCh = new Area();
			areaCh.setParentId(a.getId());
			List<Area> list1 = new ArrayList<Area>();
			list1 = areaService.getAreaListByParentId(areaCh);
			if(list1.size() > 0){
				for(Area a1:list1){
					a1.setText(a1.getName());
					a1.setState("closed");
				}
			}else{
				a.setChildren(new ArrayList<Area>());
				a.setState("open");
			}
		}
		/*if(list.size() > 0){
			for(Area a:list){
				a.setText(a.getName());
				a.setState("closed");
			}
			parentArea.setChildren(list);
			parentArea.setState("open");
		}*/
		return list;// json.toString();
	}
	/**
	 * 根据区域id，设备名，设备编号查询设备（or）
	 * 遍历区域id（in）
	 * @param areaIds
	 * @param deviceName
	 * @param deviceNumber
	 * @param pageNumber
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadDeviceList.do")
	public JsonResult<Device> jsonLoadDeviceList(
			@RequestParam(value = "areaIds", required = false) String areaIds,
			@RequestParam(value = "deviceName", required = false) String deviceName,
			@RequestParam(value = "deviceNumber", required = false) String deviceNumber,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(1);
		js.setMessage("查询失败!");
		Device d = new Device();
		List<Device> listDevice = new ArrayList<Device>();
		if(areaIds != null && areaIds != ""){
			String s = areaIds.substring(0, areaIds.lastIndexOf(","));
			String[] ls = s.split(",");
			d.setIds(ls);
		}
		if(deviceName != null && deviceName != ""){
			String Name = new String(deviceName.getBytes(
					"iso8859-1"), "utf-8");
			d.setPointName(Name);
		}
		if(deviceNumber != null && deviceNumber != ""){
			String Name = new String(deviceNumber.getBytes(
					"iso8859-1"), "utf-8");
			d.setPointNumber(Name);
		}
		if(pageNumber != null && !pageNumber.endsWith("undefined") && pageNumber != ""){
			d.setPageNo(Integer.valueOf(pageNumber));
		}else{
			d.setPageNo(1);
		}
		d.setPageSize(Constants.DEFAULT_PAGE_SIZE);
		int total = 0;
		try {
			listDevice = deviceService.getExistDevice(d);
			total = deviceService.getDeviceCountSearch(d);
			d.setTotalCount(total);
			js.setObj(d);
			js.setCode(0);
			js.setMessage("查询成功");
			js.setList(listDevice);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;
	}
	/**
	 * 保存选中设备成员至选中分组
	 * @param groupId
	 * @param deviceId
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonSaveMember.do")
	public JsonResult<DeviceGroupItem> jsonSaveMember(
			@RequestParam(value = "groupId", required = false) Integer groupId,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		JsonResult<DeviceGroupItem> js = new JsonResult<DeviceGroupItem>();
		js.setCode(1);
		js.setMessage("保存失败!");
		DeviceGroupItem dgi = new DeviceGroupItem();
		try{
			if(groupId != null){
				dgi.setGroupId(groupId);
			}
			if(deviceId != null){
				String s = deviceId.substring(0, deviceId.lastIndexOf(","));
				String[] arrayIds = s.split(",");
				//遍历deviceId，插入不存在的分组成员
				for(String dI:arrayIds){
					dgi.setDeviceId(Integer.parseInt(dI));
					//保存之前数据库做判断是否已有该分组成员
					List<Device> l = deviceService.getExistDeviceById(dgi);
					if(l.size() == 0){
						deviceService.saveGroupMember(dgi);
					}else{
						continue;
					}
				}
			}
			js.setCode(0);
			js.setMessage("保存成功!");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	/**
	 * 分页加载分组成员
	 * @param device
	 * @param group
	 * @param groupId
	 * @param pageNumber
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsonLoadDeviceListById.do", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8" })
	public JsonResult<Device> jsonLoadDeviceListById(
			Device device,DeviceGroup group,
			@RequestParam(value = "groupId", required = false) Integer groupId,
			@RequestParam(value = "Number", required = false) String pageNumber,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(new Integer(1));
		js.setMessage("获取数据失败!");
		if (pageNumber != null && !pageNumber.endsWith("undefined")){
			device.setPageNo(Integer.valueOf(pageNumber));
		}else{
			device.setPageNo(1);
		}
		device.setPageSize(Constants.DEFAULT_PAGE_SIZE); 
		if (groupId != null) {
			device.setGroupId(groupId);
		}
		List<Device> deviceList = new ArrayList<Device>();
		int total = 0;
		try {
			deviceList = deviceService.getDeviceListByGroupId(device);
			total = deviceService.getTotalCountByGroupId(device);
			device.setTotalCount(total);
			js.setObj(device);
			js.setCode(0);
			js.setList(deviceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return js;

	}
	@ResponseBody
	@RequestMapping(value = "/jsonLoadExistDeviceList.do")
	public JsonResult<Device> jsonLoadExistDeviceList(
			@RequestParam(value = "groupId", required = false) Integer groupId,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(1);
		js.setMessage("加载失败!");
		try {
			if(groupId == null || pageNumber == null){
				js.setMessage("参数传递异常!");
				return js;
			}
			Device d = new Device();
			List<Device> ld = new ArrayList<Device>();
			int total = 0;
			d.setPageNo(pageNumber);
			d.setPageSize(Constants.DEFAULT_PAGE_SIZE);
			d.setGroupId(groupId);
			ld = deviceService.getDeviceListByGroupId(d);
			total = deviceService.getTotalCountByGroupId(d);
			d.setTotalCount(total);
			js.setCode(0);
			js.setObj(d);
			js.setList(ld);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return js;
	}
	@ResponseBody
	@RequestMapping(value = "/jsonLoadAllDeviceList.do")
	public JsonResult<Device> jsonLoadAllDeviceList(
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(1);
		js.setMessage("加载失败!");
		try {
			if(pageNumber == null){
				js.setMessage("参数传递异常!");
				return js;
			}
			Device d = new Device();
			List<Device> ld = new ArrayList<Device>();
			int total = 0;
			d.setPageNo(pageNumber);
			d.setPageSize(Constants.DEFAULT_PAGE_SIZE);
			ld = deviceService.getDeviceListByFlag(d);
			total = deviceService.getDeviceCountByFlag(d);
			d.setTotalCount(total);
			js.setCode(0);
			js.setObj(d);
			js.setList(ld);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return js;
	}
}
