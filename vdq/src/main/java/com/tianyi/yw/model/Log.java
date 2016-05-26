package com.tianyi.yw.model;

import java.util.Date;

import com.tianyi.yw.common.utils.Page;

public class Log extends Page {
    private Integer id;

    private String content;

    private Integer typeId;

    private Date createTime;
    
    private String createTimes;
    
    private String type;
    
    private Date searchTime;
    
    private Date searchTimeNext;
    
    private String searchTimes;
    
    private String searchTypeIds;
    
    private int searchTypeId;

    private String description;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getCreateTimes() {
		return createTimes;
	}

	public void setCreateTimes(String createTimes) {
		this.createTimes = createTimes;
	}

	public Date getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(Date searchTime) {
		this.searchTime = searchTime;
	}

	public String getSearchTimes() {
		return searchTimes;
	}

	public void setSearchTimes(String searchTimes) {
		this.searchTimes = searchTimes;
	}

	public String getSearchTypeIds() {
		return searchTypeIds;
	}

	public void setSearchTypeIds(String searchTypeIds) {
		this.searchTypeIds = searchTypeIds;
	}

	public int getSearchTypeId() {
		return searchTypeId;
	}

	public void setSearchTypeId(int searchTypeId) {
		this.searchTypeId = searchTypeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getSearchTimeNext() {
		return searchTimeNext;
	}

	public void setSearchTimeNext(Date searchTimeNext) {
		this.searchTimeNext = searchTimeNext;
	}

	

}