package com.tianyi.yw.model;

import java.util.Date;

public class DeviceDiagnosis {
    private Integer id;

    private Integer deviceId;

    private String deviceName;

    private String deviceRtsp;

    private Date checkTime;

    private Date endTime;

    private Integer checkTimes;

    private Integer checkResult;

    private Integer checkServerId;

    private String checkServer;
    
    private Integer taskId;
    
    private Integer countSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceRtsp() {
        return deviceRtsp;
    }

    public void setDeviceRtsp(String deviceRtsp) {
        this.deviceRtsp = deviceRtsp;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCheckTimes() {
        return checkTimes;
    }

    public void setCheckTimes(Integer checkTimes) {
        this.checkTimes = checkTimes;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public Integer getCheckServerId() {
        return checkServerId;
    }

    public void setCheckServerId(Integer checkServerId) {
        this.checkServerId = checkServerId;
    }

    public String getCheckServer() {
        return checkServer;
    }

    public void setCheckServer(String checkServer) {
        this.checkServer = checkServer;
    }

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getCountSize() {
		return countSize;
	}

	public void setCountSize(Integer countSize) {
		this.countSize = countSize;
	}
}