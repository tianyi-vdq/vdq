package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceGroup;
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

	List<Device> getExistDevicePoint(Device device);

	List<Device> getAreaListByAreaId(Device device);

	List<Device> getExistDevice(Device d);

	int getTotalCountByGroupId(Device device);

	int getDeviceCountSearch(Device d);

	void update(Device d);

	List<Device> getDeviceListByFlag(Device d);

	int getDeviceCountByFlag(Device d);

	
}