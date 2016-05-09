package com.tianyi.yw.dao;

import com.tianyi.yw.model.Log;
@MyBatisRepository
public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);
}