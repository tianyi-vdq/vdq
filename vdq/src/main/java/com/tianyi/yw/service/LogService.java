package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Log;

public interface LogService {

	List<Log> getLogList(Log log);

	int getLogCount(Log log);

}
