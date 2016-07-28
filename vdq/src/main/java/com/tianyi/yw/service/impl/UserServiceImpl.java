package com.tianyi.yw.service.impl;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.tianyi.yw.common.ReturnResult;
import com.tianyi.yw.common.shiro.ShiroUsernamePasswordToken;
import com.tianyi.yw.dao.UserMapper;
import com.tianyi.yw.model.User;
import com.tianyi.yw.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	@Override
	public ReturnResult<User> login(String account, String password) {
		// TODO Auto-generated method stub

		Subject subject = SecurityUtils.getSubject();
		ReturnResult<User> res = new ReturnResult<User>();
		try {
			User u = userMapper.getUserByName(account);
			if (null == u) {
				res.setCode(ReturnResult.FAILURE);
				res.setMessage("用户[" + account + "]不存在！");
			} else {
				ShiroUsernamePasswordToken token = new ShiroUsernamePasswordToken(
						u.getAccount(), password, u.getPassword(), u.getSalt(),
						null); 
				subject.login(token);
				if (subject.isAuthenticated()) {
					res.setCode(ReturnResult.SUCCESS);
					res.setMessage("登录成功！");
					res.setResultObject(u);
				} else {
					res.setCode(ReturnResult.FAILURE);
					res.setMessage("登录失败，密码错误。");
				} 
			}
		} catch (ExcessiveAttemptsException ex) {
			res.setCode(ReturnResult.FAILURE);
			res.setMessage("登录失败，未知错误。");
		} catch (AuthenticationException ex) {
			res.setCode(ReturnResult.FAILURE);
			res.setMessage("登录失败，密码错误。");
		}
		return res;
	}

}
