package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Device;

public interface DeviceService {

	int getDeviceCount(Device device);

	List<Device> getDeviceList(Device device);

	List<Device> getExistProjectPoint(Device device);

	void saveOrUpdateProjectpoint(Device device);


}
