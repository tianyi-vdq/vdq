package com.tianyi.yw.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.ParameMapper;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Parame;
import com.tianyi.yw.service.ParamService;
@Service
public class ParamServiceImpl implements ParamService {
	
	@Resource
	private ParameMapper paramMapper;
	@Override
	public List<Parame> getParameList(Parame parame) {
		// TODO Auto-generated method stub
		return paramMapper.getParameList(parame);
	}
	@Override
	public int getParameCount(Parame parame) {
		// TODO Auto-generated method stub
		return paramMapper.getParameCount(parame);
	}
	@Override
	public List<Parame> getExistParame(Parame p) {
		// TODO Auto-generated method stub
		return paramMapper.getExistParame(p);
	}
	@Override
	public void saveOrUpdateParame(Parame parame) {
		// TODO Auto-generated method stub
		if(parame.getId()>0){
			paramMapper.updateByPrimaryKeySelective(parame);
		}else{
			paramMapper.insert(parame);
		}
	}
	@Override
	public Parame getParameById(Integer parameId) {
		// TODO Auto-generated method stub
		return paramMapper.selectByPrimaryKey(parameId);
	}

}
