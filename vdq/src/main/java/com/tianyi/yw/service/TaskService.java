package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Task;

public interface TaskService {

	List<Task> getTaskList(Task task);

	int getTaskCount(Task task);

}