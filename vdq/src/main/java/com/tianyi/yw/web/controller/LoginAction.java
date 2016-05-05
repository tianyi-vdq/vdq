package com.tianyi.yw.web.controller;
 

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.ResponseBody; 
 

import com.tianyi.yw.common.JsonResult;
import com.tianyi.yw.common.ReturnResult; 
import com.tianyi.yw.model.User; 
import com.tianyi.yw.service.UserService;

@Scope("prototype")
@Controller
public class LoginAction extends BaseAction {
	
	@Resource(name="userService")
	private UserService userService;
	 
	
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
				 
				this.setLoginUser(res.getResultObject());
				
				json.setCode(new Integer(0)); 
				json.setMessage("登录成功!");
			}else{				
				json.setMessage(res.getMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	} 
	
} 
