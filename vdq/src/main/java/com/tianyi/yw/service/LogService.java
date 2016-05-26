package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.LogType;

public interface LogService {

	List<Log> getLogList(Log log);

	int getLogCount(Log log);
	
	List<LogType> getLogTypeList(LogType logType);

	List<Log> getExistLog(Log p);

	void saveOrUpdateLog(Log log);
	
	LogType getLogTypeById(Integer id);

}


//public interface DeviceService {
//
//	int getDeviceCount(Device device);
//
//	List<Device> getDeviceList(Device device);
//
//	List<Device> getExistDevicePoint(Device p);
//
//	void saveOrUpdateDevicepoint(Device device);
//
//	Device getPointDeviceById(Integer pointId);
//
//
//}
