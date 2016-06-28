package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.DiagnosisItemTypeMapper;
import com.tianyi.yw.model.DiagnosisItemType;
import com.tianyi.yw.service.DignosisService;

@Service("dignosisService")
public class DignosisServiceImpl implements DignosisService {
	
	@Resource
	private DiagnosisItemTypeMapper diagnosisItemTypeMapper;
	
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

}
