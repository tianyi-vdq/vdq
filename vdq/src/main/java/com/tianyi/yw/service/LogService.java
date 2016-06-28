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



