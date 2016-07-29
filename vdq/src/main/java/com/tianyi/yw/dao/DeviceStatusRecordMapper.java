package com.tianyi.yw.dao;

import com.tianyi.yw.model.DeviceStatus;
import com.tianyi.yw.model.DeviceStatusRecord;
@MyBatisRepository
public interface DeviceStatusRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceStatusRecord record);
    
    int insertRecord(DeviceStatus record);

    int insertSelective(DeviceStatusRecord record);

    DeviceStatusRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceStatusRecord record);

    int updateByPrimaryKey(DeviceStatusRecord record);

	DeviceStatusRecord selectByDeviceId(int deviceId);
}