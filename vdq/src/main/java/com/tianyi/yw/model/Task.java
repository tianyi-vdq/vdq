package com.tianyi.yw.model;


import java.util.Date; 

import com.tianyi.yw.common.utils.Page;

public class Task extends Page {
    private Integer id;

    private String name;
    
    private Date startTime;
    
    private String startTimes;
    
	private Date startedTime;
    
    private String startedTimes;

    private Date createTime;

    private String createTimes;
    
    private Integer runIntervals;

    private Integer runTimes;

    private Integer runCount;

    private Integer flag;  
    //task执行前提
    private Integer flagCount;  
  

    private Date endTime;
    
    private String endTimes;
     
    /**
     * 增加前台显示字段，诊断项目名称
     * @return
     */
    private String itemTypeName;
    
    private String itemTypeId;
    
    private Boolean status;
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getRunIntervals() {
        return runIntervals;
    }

    public void setRunIntervals(Integer runIntervals) {
        this.runIntervals = runIntervals;
    }

    public Integer getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(Integer runTimes) {
        this.runTimes = runTimes;
    }

    public Integer getRunCount() {
        return runCount;
    }

    public void setRunCount(Integer runCount) {
        this.runCount = runCount;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCreateTimes() {
		return createTimes;
	}

	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}

	public String getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(String startTimes) {
		this.startTimes = startTimes;
	}

	public String getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}

	public String getStartedTimes() {
		return startedTimes;
	}

	public void setStartedTimes(String startedTimes) {
		this.startedTimes = startedTimes;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public void setStartedTime(Date startedTime) {
		this.startedTime = startedTime;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public String getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(String itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
 
	public Integer getFlagCount() {
		return flagCount;
	}

	public void setFlagCount(Integer flagCount) {
		this.flagCount = flagCount;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
 
}
