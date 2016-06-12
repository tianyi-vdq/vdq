package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Device;
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


}
