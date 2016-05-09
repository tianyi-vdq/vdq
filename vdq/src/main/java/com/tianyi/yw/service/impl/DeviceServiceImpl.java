package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.DeviceMapper;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.service.DeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {


	@Resource
	private DeviceMapper deviceMapper;
	@Override
	public int getDeviceCount(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceCount(device);
	}

	@Override
	public List<Device> getDeviceList(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getDeviceList(device);
	}

	@Override
	public List<Device> getExistProjectPoint(Device device) {
		// TODO Auto-generated method stub
		return deviceMapper.getExistProjectPoint(device);
	}

	@Override
	public void saveOrUpdateProjectpoint(Device device) {
		// TODO Auto-generated method stub
		
	}



}
