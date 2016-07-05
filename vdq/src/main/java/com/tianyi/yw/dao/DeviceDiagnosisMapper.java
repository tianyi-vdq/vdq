package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.DeviceDiagnosis;

public interface DeviceDiagnosisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceDiagnosis record);

    int insertSelective(DeviceDiagnosis record);

    DeviceDiagnosis selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceDiagnosis record);

    int updateByPrimaryKey(DeviceDiagnosis record);

	List<DeviceDiagnosis> getList(DeviceDiagnosis deviceDiagnosis);
}