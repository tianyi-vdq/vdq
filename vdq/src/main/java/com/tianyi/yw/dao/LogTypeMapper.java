package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.LogType;
@MyBatisRepository
public interface LogTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogType record);

    int insertSelective(LogType record);

    LogType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogType record);

    int updateByPrimaryKey(LogType record);
    
    List<LogType> getLogTypeList(LogType logType);
}