package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.TaskTime;

public interface TaskTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskTime record);

    int insertSelective(TaskTime record);

    TaskTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskTime record);

    int updateByPrimaryKey(TaskTime record);

	List<TaskTime> getTaskTimeList(TaskTime taskTime);
	
	int getTaskTimeCount(TaskTime record);

}