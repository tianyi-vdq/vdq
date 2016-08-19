package com.tianyi.yw.model;

import java.util.Date;

import com.tianyi.yw.common.utils.Page;

public class DeviceStatusRecord extends Page{
    private Integer id;

    private Integer deviceId;

    private Integer networkStatus;

    private Integer streamStatus;

    private Integer noiseStatus;

    private Integer signStatus;

    private Integer colorStatus;

    private Integer frameFrozenStatus;

    private Integer frameShadeStatus;

    private Integer frameFuzzyStatus;

    private Integer frameDisplacedStatus;

    private Integer frameStripStatus;

    private Integer frameColorcaseStatus;

    private Integer lightExceptionStatus;

    private Integer blackScreenStatus;

    private Date recordTime;

    private Date createTime;

    private String shotUrl;
    
    private Integer searchStatusId;
 
    private Integer searchExceptionId;
    
    private String pointId;
    
    public String getRecordTimes() {
		return recordTimes;
	}

	public void setRecordTimes(String recordTimes) {
		this.recordTimes = recordTimes;
	}
 
	private String recordTimes;
    
    private String searchPointNumber;
    
    private String areaName;

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

    public Integer getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(Integer networkStatus) {
        this.networkStatus = networkStatus;
    }

    public Integer getStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(Integer streamStatus) {
        this.streamStatus = streamStatus;
    }

    public Integer getNoiseStatus() {
        return noiseStatus;
    }

    public void setNoiseStatus(Integer noiseStatus) {
        this.noiseStatus = noiseStatus;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public Integer getColorStatus() {
        return colorStatus;
    }

    public void setColorStatus(Integer colorStatus) {
        this.colorStatus = colorStatus;
    }

    public Integer getFrameFrozenStatus() {
        return frameFrozenStatus;
    }

    public void setFrameFrozenStatus(Integer frameFrozenStatus) {
        this.frameFrozenStatus = frameFrozenStatus;
    }

    public Integer getFrameShadeStatus() {
        return frameShadeStatus;
    }

    public void setFrameShadeStatus(Integer frameShadeStatus) {
        this.frameShadeStatus = frameShadeStatus;
    }

    public Integer getFrameFuzzyStatus() {
        return frameFuzzyStatus;
    }

    public void setFrameFuzzyStatus(Integer frameFuzzyStatus) {
        this.frameFuzzyStatus = frameFuzzyStatus;
    }

    public Integer getFrameDisplacedStatus() {
        return frameDisplacedStatus;
    }

    public void setFrameDisplacedStatus(Integer frameDisplacedStatus) {
        this.frameDisplacedStatus = frameDisplacedStatus;
    }

    public Integer getFrameStripStatus() {
        return frameStripStatus;
    }

    public void setFrameStripStatus(Integer frameStripStatus) {
        this.frameStripStatus = frameStripStatus;
    }

    public Integer getFrameColorcaseStatus() {
        return frameColorcaseStatus;
    }

    public void setFrameColorcaseStatus(Integer frameColorcaseStatus) {
        this.frameColorcaseStatus = frameColorcaseStatus;
    }

    public Integer getLightExceptionStatus() {
        return lightExceptionStatus;
    }

    public void setLightExceptionStatus(Integer lightExceptionStatus) {
        this.lightExceptionStatus = lightExceptionStatus;
    }

    public Integer getBlackScreenStatus() {
        return blackScreenStatus;
    }

    public void setBlackScreenStatus(Integer blackScreenStatus) {
        this.blackScreenStatus = blackScreenStatus;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getSearchPointNumber() {
		return searchPointNumber;
	}

	public void setSearchPointNumber(String searchPointNumber) {
		this.searchPointNumber = searchPointNumber;
	}

	public Integer getSearchStatusId() {
		return searchStatusId;
	}

	public void setSearchStatusId(Integer searchStatusId) {
		this.searchStatusId = searchStatusId;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public Integer getSearchExceptionId() {
		return searchExceptionId;
	}

	public void setSearchExceptionId(Integer searchExceptionId) {
		this.searchExceptionId = searchExceptionId;
	}

	public String getShotUrl() {
		return shotUrl;
	}

	public void setShotUrl(String shotUrl) {
		this.shotUrl = shotUrl;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}