package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.TaskTime;

@MyBatisRepository
public interface TaskTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskTime record);

    int insertSelective(TaskTime record);

    TaskTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskTime record);

    int updateByPrimaryKey(TaskTime record);

	List<TaskTime> getTaskTimeList(TaskTime taskTime);
	
	int getTaskTimeCount(TaskTime record);

	List<TaskTime> getAllTaskTimeList(Integer taskId);

	void deleteByTaskId(Integer id);
}
