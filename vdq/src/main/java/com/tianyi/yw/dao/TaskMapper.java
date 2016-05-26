package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.Task;
@MyBatisRepository
public interface TaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);
     
    List<Task> getTaskList(Task task);
    
    int getTaskCount(Task task);

	List<Task> getExistTask(Task task);

	Task getTaskById(Integer id);

}

