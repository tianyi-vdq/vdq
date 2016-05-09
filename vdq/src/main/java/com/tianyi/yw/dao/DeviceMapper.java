package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Device;
@MyBatisRepository
public interface DeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);

	List<Device> getDeviceList(Device device);

	int getDeviceCount(Device device);

	List<Device> getExistProjectPoint(Device device);
	
}