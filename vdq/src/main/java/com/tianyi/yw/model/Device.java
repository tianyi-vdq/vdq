package com.tianyi.yw.model;


import com.tianyi.yw.common.utils.Page;

public class Device extends Page {
    private Integer id;

    private String pointId;

    private String pointNumber;

    private String pointName;

    private String pointNaming;

    private String type;

    private String address;

    private String ipAddress;

    private Integer areaId;
    
    private String rtspUrl;
    
    private Integer flag;
    
    private String areaName;
    //自定义
    private String platformId;
    
    private String deviceKey;
    
    private Integer parentId;
    
    private Integer groupId;
    
    private String[] Ids;
    
    private Integer taskId;
    
    private Integer checkTimes;
    
    private Integer serverId;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(String pointNumber) {
        this.pointNumber = pointNumber;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPointNaming() {
        return pointNaming;
    }

    public void setPointNaming(String pointNaming) {
        this.pointNaming = pointNaming;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRtspUrl() {
		return rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String[] getIds() {
		return Ids;
	}

	public void setIds(String[] ids) {
		Ids = ids;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getDeviceKey() {
		return deviceKey;
	}

	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getCheckTimes() {
		return checkTimes;
	}

	public void setCheckTimes(Integer checkTimes) {
		this.checkTimes = checkTimes;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}



}