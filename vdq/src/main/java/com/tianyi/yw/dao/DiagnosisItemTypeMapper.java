package com.tianyi.yw.dao;

import java.util.List;

import com.tianyi.yw.model.DiagnosisItemType;
@MyBatisRepository
public interface DiagnosisItemTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiagnosisItemType record);

    int insertSelective(DiagnosisItemType record);

    DiagnosisItemType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiagnosisItemType record);

    int updateByPrimaryKey(DiagnosisItemType record);

    //返回诊断标准的所有信息
	List<DiagnosisItemType> getDiagnosisList();
	//更改诊断标准值
	 int updateValue(DiagnosisItemType diagnosisItemType);

	DiagnosisItemType getExistDiagnosisType(Integer itemTypeId);
}