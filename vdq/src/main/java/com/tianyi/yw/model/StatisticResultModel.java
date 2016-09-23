package com.tianyi.yw.model;

import java.util.Date;
import java.util.List;

import com.tianyi.yw.common.utils.Page;

public class StatisticResultModel extends Page {

	private Integer id;
	private Integer deviceId;
	private String deviceNumber;
	private String ipAddress;
	private String recordDate;
	private Date recordTime;
	private String resultValue;
	private List<String> dateStrList;
	private List<String> valueList;
	private List<Integer> idList;
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
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	public List<String> getDateStrList() {
		return dateStrList;
	}
	public void setDateStrList(List<String> dateStrList) {
		this.dateStrList = dateStrList;
	}
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	public List<Integer> getIdList() {
		return idList;
	}
	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}
	public List<String> getValueList() {
		return valueList;
	}
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	} 
	
}
