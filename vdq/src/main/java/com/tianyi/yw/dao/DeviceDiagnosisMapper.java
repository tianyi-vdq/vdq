package com.tianyi.yw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceDiagnosis;
@MyBatisRepository
public interface DeviceDiagnosisMapper {
    void deleteByPrimaryKey(Integer id);

    void insert(DeviceDiagnosis record);

    void insertSelective(DeviceDiagnosis record);

    DeviceDiagnosis selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(DeviceDiagnosis record);

    void updateByPrimaryKey(DeviceDiagnosis record);

	List<DeviceDiagnosis> getList(DeviceDiagnosis deviceDiagnosis);

	void clear();

	void insertDeviceList(List<Device> devicelist);

	int getCheckResultList();
	
	DeviceDiagnosis getExistDiagnosis(DeviceDiagnosis dg);

	DeviceDiagnosis selectByDeviceId(int id);
}
