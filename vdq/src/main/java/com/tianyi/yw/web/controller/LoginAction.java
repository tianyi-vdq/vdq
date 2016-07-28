package com.tianyi.yw.web.controller;
 

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
 




import net.sf.json.JSONObject;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; 
  




import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.ReturnResult; 
import com.tianyi.yw.common.utils.Constants;   
import com.tianyi.yw.model.Functions;
import com.tianyi.yw.model.User; 
import com.tianyi.yw.service.FunctionService;
import com.tianyi.yw.service.UserService;

@Scope("prototype")
@Controller
public class LoginAction extends BaseAction {
	
	@Resource
	private UserService userService;
	 

	@Resource
	private FunctionService functionService; 
	
	
	@ResponseBody
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public JsonResult <User> login(User user, 
			HttpServletRequest request, HttpServletResponse response){
		JsonResult <User>json  = new JsonResult<User>();
		json.setCode(new Integer(1));
		json.setMessage("登录失败!"); 
		
		try{
			ReturnResult<User> res = userService.login(user.getAccount(), user.getPassword());
			if(res.getCode() == ReturnResult.SUCCESS){
				//List<Functions> lf = parseFunctionList(functionService.getFunctionByParentId(res.getResultObject().getId()));
				List<Functions> lf = parseFunctionList(functionService.getFunctionByParentId(0));
				request.getSession().setAttribute(Constants.USER_SESSION_FUNCTION,lf); 
				
				
				res.getResultObject().setSelectedMainMemu(lf.get(0).getId());
				res.getResultObject().setSelectedChildMenu(lf.get(0).getChildFunctionlist().get(0).getId());
				res.getResultObject().setChildMenuList(lf.get(0).getChildFunctionlist());
				this.setLoginUser(res.getResultObject());
				
				json.setCode(new Integer(0)); 
				json.setGotoUrl(lf.get(0).getUrl());
				json.setMessage("登录成功!");
			}else{				
				json.setMessage(res.getMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}

	@ResponseBody
	@RequestMapping(value="/jsonLoadSession.do")
	public JsonResult <User> jsonLoadSession(
		@RequestParam(value = "selectedMainMemu", required = false) Integer selectedMainMemu,
		@RequestParam(value = "selectedChildMenu", required = false) Integer selectedChildMenu, 
		HttpServletRequest request, HttpServletResponse response){
		JsonResult <User>json  = new JsonResult<User>();
		if (selectedMainMemu != null) {
		    this.getLoginUser().setSelectedMainMemu(selectedMainMemu);
		    List<Functions>lf = (List<Functions>) request.getSession().getAttribute(Constants.USER_SESSION_FUNCTION);
		    for (Functions function : lf) {
				if (function.getId().intValue() == selectedMainMemu.intValue()) {
					this.getLoginUser().setSelectedChildMenu(function.getChildFunctionlist().get(0).getId());
					break;
				}
			}
		}
		else if (selectedChildMenu != null) {
			this.getLoginUser().setSelectedChildMenu(selectedChildMenu);
//			this.getLoginUser().setSelectedMainMemu(Integer.parseInt(String.valueOf(selectedChildMenu).substring(0, 1)));
		}
		json.setCode(new Integer(0));
		json.setMessage("更新成功!");
		
		return json;
	}
	private List<Functions> parseFunctionList(List<Functions> src) {
		// TODO Auto-generated method stub
		for(Functions f : src){
			f.setChildFunctionlist(functionService.getFunctionByParentId(f.getId()));
		}
		return src;
	} 
	
} 
