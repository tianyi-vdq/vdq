package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.DeviceGroup;
import com.tianyi.yw.model.DeviceGroupItem;
@MyBatisRepository
public interface DeviceGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceGroup record);

    int insertSelective(DeviceGroup record);

    DeviceGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceGroup record);

    int updateByPrimaryKey(DeviceGroup record);

	List<DeviceGroup> getGroupList(DeviceGroup group);

	DeviceGroup getDeviceGroupById(Integer groupId);

	List<DeviceGroup> getExistGroup(DeviceGroup dg);

	List<DeviceGroup> getExistGroupByDeviceId(DeviceGroupItem deviceId);
}