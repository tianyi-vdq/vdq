package com.tianyi.yw.service;

import java.util.List;

import com.tianyi.yw.model.DiagnosisItemType;

public interface DignosisService {
	List<DiagnosisItemType> getDiagnosisList();
	boolean updateValue(DiagnosisItemType diagnosisItemType);
}
