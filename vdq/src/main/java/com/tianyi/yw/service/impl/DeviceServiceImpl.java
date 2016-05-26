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
			deviceMapper.insertSelective(device);
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
		deviceMapper.insertSelective(device);
	}



}
