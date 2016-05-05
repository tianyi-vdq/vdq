package com.tianyi.yw.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.tianyi.yw.common.utils.Constants;
import com.tianyi.yw.model.User;

public abstract class BaseAction {
	protected Logger log = Logger.getLogger(getClass());

	private String resultCode;
	private String resultMessage;
	
	private HttpServletRequest req;
	private HttpServletResponse res;
	

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletResponse getRes() {
		return res;
	}

	public void setRes(HttpServletResponse res) {
		this.res = res;
	}

	public Object getSession(String key) {
		return SecurityUtils.getSubject().getSession().getAttribute(key);
	}

	public Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	/**
	 * 鐢ㄦ埛session
	 * 
	 * @return
	 */
	protected User getLoginUser() {
		User loginUser = (User) SecurityUtils.getSubject().getSession().getAttribute(Constants.USER_SESSION_NAME);
		return loginUser;
	}

	/**
	 * 鐢ㄦ埛session
	 * 
	 * @param loginUser
	 */
	protected void setLoginUser(User loginUser) {
		SecurityUtils.getSubject().getSession().setAttribute(Constants.USER_SESSION_NAME, loginUser);
	}

	
}
