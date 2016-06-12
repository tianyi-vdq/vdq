package com.tianyi.yw.web.controller.winService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.model.Device;
import com.tianyi.yw.model.Parame;
import com.tianyi.yw.service.DeviceService;
import com.tianyi.yw.service.ParamService;

@Scope("prototype")
@Controller
@RequestMapping("/dataUtil")
public class DataUtilAction {
	
	@Resource
	private DeviceService deviceService;
	@Resource
	private ParamService parameService;
	
	@ResponseBody
	@RequestMapping(value = "/jsonLoadDeviceList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Device> deviceList(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<Device> js = new JsonResult<Device>();
		js.setCode(new Integer(1));
		js.setMessage("加载设备列表失败!");
		List<Device> ls = new ArrayList<Device>();
		Device d = new Device();
		try{
			ls = deviceService.getDeviceList(d);
			js.setList(ls);
			js.setCode(0);
			js.setMessage("加载设备列表成功！");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
	@ResponseBody
	@RequestMapping(value = "/jsonLoadParameList.do", produces = { "text/html;charset=UTF-8" })
	public JsonResult<Parame>  parameList(HttpServletRequest request,
			HttpServletResponse response) {
		JsonResult<Parame> js = new JsonResult<Parame>();
		js.setCode(new Integer(1));
		js.setMessage("加载参数列表失败!");
		List<Parame> ls = new ArrayList<Parame>();
		Parame p = new Parame();
		try{
			ls = parameService.getParameList(p);
			js.setList(ls);
			js.setCode(0);
			js.setMessage("加载参数列表成功！");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return js;
	}
}
