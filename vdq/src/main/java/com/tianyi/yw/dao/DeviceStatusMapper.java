package com.tianyi.yw.dao;

import com.tianyi.yw.model.DeviceStatus;
@MyBatisRepository
public interface DeviceStatusMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceStatus record);

    int insertSelective(DeviceStatus record);

    DeviceStatus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceStatus record);

    int updateByPrimaryKey(DeviceStatus record);
}