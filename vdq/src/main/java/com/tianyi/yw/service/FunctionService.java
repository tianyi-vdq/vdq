package com.tianyi.yw.service;

import java.util.List; 

import com.tianyi.yw.model.Functions;
public interface FunctionService { 
	
	List<Functions> getFunctionByParentId(Integer id);

}
