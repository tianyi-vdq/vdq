package com.tianyi.yw.model;

public class DiagnosisItemType {
    private Integer id;

    private TaskItemType itemTypeId;

    private Integer value1;

    private Integer value2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

  
  

	public TaskItemType getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(TaskItemType itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public Integer getValue1() {
        return value1;
    }

    public void setValue1(Integer value1) {
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
    }

	@Override
	public String toString() {
		return "DiagnosisItemType [id=" + id + ", itemTypeId=" + itemTypeId
				+ ", value1=" + value1 + ", value2=" + value2 + "]";
	}

    
}