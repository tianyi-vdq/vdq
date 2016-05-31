package com.tianyi.yw.dao;

import com.tianyi.yw.model.DeviceGroupItem;
@MyBatisRepository
public interface DeviceGroupItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceGroupItem record);

    int insertSelective(DeviceGroupItem record);

    DeviceGroupItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceGroupItem record);

    int updateByPrimaryKey(DeviceGroupItem record);
}