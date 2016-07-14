package com.tianyi.yw.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.common.utils.StringUtil;
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
		if(StringUtil.isEmpty(log.getDescription())){
			log.setDescription("无");
		}
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

	@Override
	public LogType getLogTypeById(Integer id)
	{
		return logTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public void writeLog(int type, String content) {
		// TODO Auto-generated method stub
		Log log = new Log();
		log.setTypeId(type);
		log.setContent(content);
		log.setCreateTime(new Date());
		logMapper.insertSelective(log);		
	}

	@Override
	public void writeLog(int type, String content, String description) {
		// TODO Auto-generated method stub
		Log log = new Log();
		log.setTypeId(type);
		log.setContent(content);
		log.setDescription(description);
		log.setCreateTime(new Date());
		logMapper.insertSelective(log);	
	}

}


