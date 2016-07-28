package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.DeviceDiagnosisMapper;
import com.tianyi.yw.dao.DiagnosisItemTypeMapper;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.DeviceDiagnosis;
import com.tianyi.yw.model.DiagnosisItemType;
import com.tianyi.yw.service.DignosisService;

@Service
public class DignosisServiceImpl implements DignosisService {
	
	@Resource
	private DiagnosisItemTypeMapper diagnosisItemTypeMapper;
	@Resource
	private DeviceDiagnosisMapper deviceDiagnosisMapper;
	
	
	@Override
	public List<DiagnosisItemType> getDiagnosisList() {
		// TODO Auto-generated method stub
		return diagnosisItemTypeMapper.getDiagnosisList();
	}

	@Override
	public boolean updateValue(DiagnosisItemType diagnosisItemType) {
		int i = diagnosisItemTypeMapper.updateValue(diagnosisItemType);
		return i>0;
	}

	@Override
	public void insert(DeviceDiagnosis deviceDiagnosis) {
		// TODO Auto-generated method stub
		deviceDiagnosisMapper.insert(deviceDiagnosis);
	}

	@Override
	public void updatebyselective(DeviceDiagnosis deviceDiagnosis) {
		// TODO Auto-generated method stub
		deviceDiagnosisMapper.updateByPrimaryKeySelective(deviceDiagnosis);
	}

	@Override
	public List<DeviceDiagnosis> getList(DeviceDiagnosis deviceDiagnosis) {
		// TODO Auto-generated method stub
		return deviceDiagnosisMapper.getList(deviceDiagnosis);
	}

	@Override
	public DeviceDiagnosis selectByPrimaryKey(int deviceDiagnosisId) {
		// TODO Auto-generated method stub
		return deviceDiagnosisMapper.selectByPrimaryKey(deviceDiagnosisId);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		deviceDiagnosisMapper.clear();
	}

	@Override
	public void insertDeviceList(List<Device> list) {
		// TODO Auto-generated method stub
		deviceDiagnosisMapper.insertDeviceList(list);
	}

	@Override
	public int getCheckResultList() {
		// TODO Auto-generated method stub
		return deviceDiagnosisMapper.getCheckResultList();
	}
	public DeviceDiagnosis getExistDiagnosis(DeviceDiagnosis dg) {
		// TODO Auto-generated method stub
		return deviceDiagnosisMapper.getExistDiagnosis(dg);
	}

	@Override
	public DiagnosisItemType getExistDiagnosisType(Integer itemTypeId) {
		// TODO Auto-generated method stub
		return diagnosisItemTypeMapper.getExistDiagnosisType(itemTypeId);
	}

}
