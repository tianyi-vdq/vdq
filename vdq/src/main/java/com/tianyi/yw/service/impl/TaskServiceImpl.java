package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.TaskMapper;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.service.TaskService;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Resource
	private TaskMapper taskMapper;
	@Override
	public List<Task> getTaskList(Task task) {
		// TODO Auto-generated method stub
		return taskMapper.getTaskList(task);
	}

	@Override
	public int getTaskCount(Task task) {
		// TODO Auto-generated method stub
		return taskMapper.getTaskCount(task);
	}

	@Override
	public void saveOrUpdateTask(Task task) {
		// TODO Auto-generated method stub
		if(task.getId()>0){
			taskMapper.updateByPrimaryKeySelective(task);
		}else{
			taskMapper.insertSelective(task);
		}
	}

	@Override
	public List<Task> getExistTask(Task task) {
		// TODO Auto-generated method stub
		return taskMapper.getExistTask(task);
	}
 
	@Override
	public Task getTaskById(Integer id)
	{
		// TODO Auto-generated method stub
		return taskMapper.selectByPrimaryKey(id);
	}
	
	
}

