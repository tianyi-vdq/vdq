package com.tianyi.yw.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.model.DeviceDiagnosis;

public interface DataUtilService {
	
	JsonResult<DeviceDiagnosis>  DiagnosisList(int count,HttpServletRequest request,HttpServletResponse response);
	
	void  DiagnosisList1(int count,HttpServletRequest request,HttpServletResponse response);

	JsonResult<DeviceDiagnosis> DeviceDiagnosis(Integer deviceId,String score,
			HttpServletRequest request,HttpServletResponse response);
}
