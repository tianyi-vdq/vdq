package com.tianyi.yw.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
 


import org.springframework.stereotype.Service;

import com.tianyi.yw.dao.FunctionsMapper; 
import com.tianyi.yw.model.Functions; 
import com.tianyi.yw.service.FunctionService;
@Service("functionService")
public class FunctionServiceImpl implements FunctionService {

	
	@Resource
	private FunctionsMapper functionMapper;
	 
	@Override
	public List<Functions> getFunctionByParentId(Integer id) {
		// TODO Auto-generated method stub
		return functionMapper.getFunctionByParentId(id);
	}

}
