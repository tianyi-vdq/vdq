package com.tianyi.yw.service;

import java.util.List; 

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem;
import com.tianyi.yw.model.DeviceStatus;

public interface DeviceService {

	int getDeviceCount(Device device);
	
	int getDeviceStatusCount(DeviceStatus deviceStatus);

	List<Device> getDeviceList(Device device);

	List<Device> getExistDevicePoint(Device p);
	
	List<DeviceStatus> getDeviceStatusList(DeviceStatus deviceStatus);

	void saveOrUpdateDevicepoint(Device device);

	Device getPointDeviceById(Integer pointId);

	void saveDevicepoint(Device device);

	List<Device> getAreaListByAreaId(Device device);

	List<DeviceGroup> getGroupList(DeviceGroup group);

	List<Device> getDeviceListByGroupId(Device group);

	int getTotalCountByGroupId(Device device);

	void deleteMemberById(DeviceGroupItem dgi);

	DeviceGroup getDeviceGroupById(Integer groupId);

	List<DeviceGroup> getExistGroup(DeviceGroup dg);

	void saveOrUpdateGroup(DeviceGroup deviceGroup);

	List<Device> getExistDevice(Device d);

	void saveGroupMember(DeviceGroupItem dgi);

	List<Device> getExistDeviceById(DeviceGroupItem dgi);

	int getDeviceCountSearch(Device d);

	void stopOrStartDeviceById(Device d);

	List<DeviceGroup> getExistGroupByDeviceId(DeviceGroupItem dg);

	List<Device> getDeviceListByFlag(Device d);

	int getDeviceCountByFlag(Device d);

	List<Device> getAllDeviceList();
	
	void saveOrUpdateDeviceStatus(DeviceStatus ds);



}
