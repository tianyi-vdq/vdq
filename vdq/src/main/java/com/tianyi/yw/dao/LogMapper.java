package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Log;
@MyBatisRepository
public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer id);
    
    int getLogCount(Log log);
    
    List<Log> getLogList(Log log);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);

    List<Log> getExistLog(Log log) ;
}