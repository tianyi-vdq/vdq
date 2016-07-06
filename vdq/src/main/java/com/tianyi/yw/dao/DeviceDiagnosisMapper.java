package com.tianyi.yw.dao;

import java.util.List;

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
}