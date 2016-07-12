package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.common.utils.StringUtil;
import com.tianyi.yw.dao.TaskItemMapper;
import com.tianyi.yw.dao.TaskItemTypeMapper;
import com.tianyi.yw.dao.TaskMapper;
import com.tianyi.yw.dao.TaskTimeMapper;
import com.tianyi.yw.model.Task;
import com.tianyi.yw.model.TaskItem;
import com.tianyi.yw.model.TaskItemType;
import com.tianyi.yw.model.TaskTime;
import com.tianyi.yw.service.TaskService;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Resource
	private TaskMapper taskMapper;
	
	@Resource
	private TaskItemMapper taskItemMapper;
	
	@Resource
	private TaskItemTypeMapper taskItemTypeMapper;
	
	@Resource
	private TaskTimeMapper taskTimeMapper;
	
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
	public int getRunTaskCount(Task task) {
		// TODO Auto-generated method stub
		return taskMapper.getRunTaskCount(task);
	}
	
	@Override
	public void saveOrUpdateTask(Task task) {
		// TODO Auto-generated method stub
		if(task.getId()>0){
			taskMapper.updateByPrimaryKeySelective(task);
		}else{
			taskMapper.insert(task);
		}
		saveOrUpdateTaskItem(task);
	}
	
	private void saveOrUpdateTaskItem(Task task) {
		// TODO Auto-generated method stub
		if(!StringUtil.isEmpty(task.getItemTypeId())){
			taskItemMapper.deleteItemByTaskId(task.getId());
			String[] ids = task.getItemTypeId().split(",");
			if(ids.length>0){
				for(String id :ids){
					TaskItem ti = new TaskItem();
					ti.setItemTypeId(Integer.parseInt(id));
					ti.setTaskId(task.getId());
					taskItemMapper.insert(ti);
				}
			}
		}
	}

	@Override
	public void saveOrUpdateTaskItem(TaskItem taskIem) {
		// TODO Auto-generated method stub
			taskItemMapper.insert(taskIem);
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
		return taskMapper.getTaskById(id);
	}
	
	@Override
	public TaskItemType getTaskItemTypeById(Integer id)
	{
		return taskItemTypeMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TaskItem> getTaskItemList(TaskItem taskItem)
	{
		return  taskItemMapper.getTaskItemList(taskItem);
	}
	
	@Override
    public List<TaskItemType> getTaskItemTypeList(TaskItemType taskItemType)
    {
	    return  taskItemTypeMapper.getTaskItemTypeList(taskItemType);
    }
	
	@Override
	public void deleteTaskById(Integer id)
	{
		taskMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<TaskItem> getTaskItemListById(TaskItem taskItem) {
		// TODO Auto-generated method stub
		return taskItemMapper.getTaskItemListById(taskItem);
	}

	@Override
	public List<TaskTime> getAllTaskTimeList(Integer taskId) {
		// TODO Auto-generated method stub
		return taskTimeMapper.getAllTaskTimeList(taskId);
	}

}

