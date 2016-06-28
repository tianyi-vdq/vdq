package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.DeviceGroupItemMapper;
import com.tianyi.yw.dao.DeviceGroupMapper;
import com.tianyi.yw.dao.DeviceMapper;
import com.tianyi.yw.dao.DeviceStatusMapper;
import com.tianyi.yw.model.Device; 
import com.tianyi.yw.model.DeviceStatus; 
import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem; 
import com.tianyi.yw.service.DeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {


	@Resource
	private DeviceMapper deviceMapper;
	@Resource
	private DeviceGroupMapper deviceGroupMapper;
	@Resource 
	private DeviceGroupItemMapper deviceGroupItemMapper;
	 
	
	@Resource
	private DeviceStatusMapper deviceStatusMapper;
	
	@Override
	public int getDeviceCount(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceCount(device);
	}
	
	@Override
	public int getDeviceStatusCount(DeviceStatus deviceStatus)
	{
		return deviceStatusMapper.getDeviceStatusCount(deviceStatus);
	}

	@Override
	public List<Device> getDeviceList(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceList(device);
	}
	
	@Override
	public List<DeviceStatus> getDeviceStatusList(DeviceStatus deviceStatus)
	{
		return deviceStatusMapper.getDeviceStatusList(deviceStatus);
	}

	@Override
	public List<Device> getExistDevicePoint(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getExistDevicePoint(device);
	}

	@Override
	public void saveOrUpdateDevicepoint(Device device) {
		// TODO Auto-generated method stub
		if(device.getId()>0){
			deviceMapper.updateByPrimaryKeySelective(device);
		}else{
			deviceMapper.insert(device);
		}
	}

	@Override
	public Device getPointDeviceById(Integer pointId) {
		// TODO Auto-generated method stub
		return deviceMapper.selectByPrimaryKey(pointId);
	}

	@Override
	public void saveDevicepoint(Device device) {
		// TODO Auto-generated method stub
		deviceMapper.insert(device);
	}

	@Override
	public List<Device> getAreaListByAreaId(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getAreaListByAreaId(device);
	}

	@Override
	public List<DeviceGroup> getGroupList(DeviceGroup group) {
		// TODO Auto-generated method stub
		return deviceGroupMapper.getGroupList(group);
	}

	@Override
	public List<Device> getDeviceListByGroupId(Device group) {
		// TODO Auto-generated method stub
		return deviceGroupItemMapper.getDeviceListByGroupId(group);
	}

	@Override
	public int getTotalCountByGroupId(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getTotalCountByGroupId(device);
	}

	@Override
	public void deleteMemberById(DeviceGroupItem dgi) {
		// TODO Auto-generated method stub
		deviceGroupItemMapper.deleteMemberById(dgi);
	}

	@Override
	public DeviceGroup getDeviceGroupById(Integer groupId) {
		// TODO Auto-generated method stub
		return deviceGroupMapper.getDeviceGroupById(groupId);
	}

	@Override
	public List<DeviceGroup> getExistGroup(DeviceGroup dg) {
		// TODO Auto-generated method stub
		return deviceGroupMapper.getExistGroup(dg);
	}

	@Override
	public void saveOrUpdateGroup(DeviceGroup deviceGroup) {
		// TODO Auto-generated method stub
		if(deviceGroup.getId()>0){
			deviceGroupMapper.updateByPrimaryKeySelective(deviceGroup);
		}else{
			deviceGroupMapper.insert(deviceGroup);
		}
	}

	@Override
	public List<Device> getExistDevice(Device d) {
		// TODO Auto-generated method stub
		return deviceMapper.getExistDevice(d);
	}

	@Override
	public void saveGroupMember(DeviceGroupItem dgi) {
		// TODO Auto-generated method stub
		deviceGroupItemMapper.insert(dgi);
	}

	@Override
	public List<Device> getExistDeviceById(DeviceGroupItem dgi) {
		// TODO Auto-generated method stub
		return deviceGroupItemMapper.getExistDeviceById(dgi);
	}

	@Override
	public int getDeviceCountSearch(Device d) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceCountSearch(d);
	}

	@Override
	public void stopOrStartDeviceById(Device d) {
		// TODO Auto-generated method stub
		deviceMapper.update(d);
	}

	@Override
	public List<DeviceGroup> getExistGroupByDeviceId(DeviceGroupItem deviceId) {
		// TODO Auto-generated method stub
		return deviceGroupMapper.getExistGroupByDeviceId(deviceId);
	}

	@Override
	public List<Device> getDeviceList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Device> getDeviceListByFlag(Device d) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceListByFlag(d);
	}

	@Override
	public int getDeviceCountByFlag(Device d) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceCountByFlag(d);
	}




}
