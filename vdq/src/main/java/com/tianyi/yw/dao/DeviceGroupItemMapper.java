package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem;
@MyBatisRepository
public interface DeviceGroupItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceGroupItem record);

    int insertSelective(DeviceGroupItem record);

    DeviceGroupItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceGroupItem record);

    int updateByPrimaryKey(DeviceGroupItem record);

	List<Device> getDeviceListByGroupId(Device group);

	int getTotalCountByGroupId(Device device);

	void deleteMemberById(DeviceGroupItem dgi);

	void saveGroupMember(DeviceGroupItem dgi);

	List<Device> getExistDeviceById(DeviceGroupItem dgi);
}