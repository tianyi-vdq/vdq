package com.tianyi.yw.dao;

import com.tianyi.yw.model.DeviceGroup;
@MyBatisRepository
public interface DeviceGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceGroup record);

    int insertSelective(DeviceGroup record);

    DeviceGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceGroup record);

    int updateByPrimaryKey(DeviceGroup record);
}