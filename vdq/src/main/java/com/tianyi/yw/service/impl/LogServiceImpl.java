package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.LogMapper;
import com.tianyi.yw.dao.LogTypeMapper;
import com.tianyi.yw.model.Log;
import com.tianyi.yw.model.LogType;
import com.tianyi.yw.service.LogService;
@Service("logService")
public class LogServiceImpl implements LogService {

	@Resource
	private LogMapper logMapper;
	@Resource
	private LogTypeMapper logTypeMapper;
	
	@Override
	public List<Log> getLogList(Log log) 
	{
		// TODO Auto-generated method stub
		return logMapper.getLogList(log);
	}

	@Override
	public int getLogCount(Log log) 
	{
		// TODO Auto-generated method stub
		return logMapper.getLogCount(log);
	}
	
	@Override
	public List<LogType> getLogTypeList(LogType logType)
	{
		return logTypeMapper.getLogTypeList(logType);
	}
	
	@Override
	public void saveOrUpdateLog(Log log) {
		// TODO Auto-generated method stub
		if(log.getId()>0){
			logMapper.updateByPrimaryKeySelective(log);
		}else{
			logMapper.insertSelective(log);
		}
	}

	@Override
	public List<Log> getExistLog(Log log) {
		// TODO Auto-generated method stub
		return logMapper.getExistLog(log);
	}

}


