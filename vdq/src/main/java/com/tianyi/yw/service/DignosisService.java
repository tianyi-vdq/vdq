package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.DiagnosisItemType;

public interface DignosisService {
	List<DiagnosisItemType> getDiagnosisList();
	
	boolean updateValue(DiagnosisItemType diagnosisItemType);
	
	void insert(DeviceDiagnosis deviceDiagnosis);
	
	void updatebyselective(DeviceDiagnosis deviceDiagnosis);
	
	List<DeviceDiagnosis> getList(DeviceDiagnosis deviceDiagnosis);
	
	DeviceDiagnosis selectByPrimaryKey(int deviceDiagnosisId);

	void clear();

	void insertDeviceList(List<Device> list);

	int getCheckResultList();

	DiagnosisItemType getExistDiagnosisType(Integer itemTypeId);

	DeviceDiagnosis getExistDiagnosis(
			DeviceDiagnosis dg);

}
