package com.tianyi.yw.model;

import java.util.Date;

import com.tianyi.yw.common.utils.Page;

public class TaskTime  extends Page{
    private Integer id;

    private Integer taskId;

    private Date startTime;
    
    private String startTimes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

	public String getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(String startTimes) {
		this.startTimes = startTimes;
	}
}