package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Device;

public interface DeviceService {

	int getDeviceCount(Device device);

	List<Device> getDeviceList(Device device);

	List<Device> getExistDevicePoint(Device p);

	void saveOrUpdateDevicepoint(Device device);

	Device getPointDeviceById(Integer pointId);

	void saveDevicepoint(Device device);


}
