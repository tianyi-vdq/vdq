package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Task;
import com.tianyi.yw.model.TaskItem;
import com.tianyi.yw.model.TaskItemType;
import com.tianyi.yw.model.TaskTime;

public interface TaskService {

	List<Task> getTaskList(Task task);

	int getTaskCount(Task task);

	void saveOrUpdateTask(Task task) ;

	List<Task> getExistTask(Task task);

	Task getTaskById(Integer id);
	
	List<TaskItemType> getTaskItemTypeList(TaskItemType taskItemType);
	
	TaskItemType getTaskItemTypeById(Integer id);
	
	List<TaskItem> getTaskItemList(TaskItem taskItem);

	void deleteTaskById(Integer id);

	void saveOrUpdateTaskItem(TaskItem taskIem);

	int getRunTaskCount(Task task);

	List<TaskItem> getTaskItemListById(TaskItem taskItem);

	List<TaskTime> getTaskTimeList(TaskTime taskTime);
	
	int getTaskTimeCount(TaskTime taskTime);
	
	List<TaskTime> getAllTaskTimeList(Integer integer);
}
