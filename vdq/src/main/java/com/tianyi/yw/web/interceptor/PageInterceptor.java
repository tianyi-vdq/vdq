package com.tianyi.yw.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class PageInterceptor implements HandlerInterceptor {

	private List<String> allowAction;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod method = (HandlerMethod) handler;
		Logger log = Logger.getLogger(method.getClass());
		
		
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		log.info("PageInterceptor [" + method.getBean() +":"+url+ " Started...]");
		log.info(" ");
//		for (String s : allowAction) {
//			if (url.equals(s)) {
//				return true;
//			}
//		}
//		User user = (User) request.getSession()
//				.getAttribute(Constant.USER_INFO);
//		if (user == null) {
//			log.info("RestMethodInterceptor [Session Timeout.]");
//			return false;
//		} else {
//			BaseController b = (BaseController) method.getBean();
//			b.setRequest(request);
//			b.setResponse(response);
//			return true;
//		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerMethod h =  (HandlerMethod)handler;
		Logger log =Logger.getLogger(h.getClass());
		
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		
		request.setAttribute("requestCurrURL", url.substring(url.lastIndexOf("/")+1));
		
		log.info("PageInterceptor [" + h.getBean() +":"+url+ " Normally End.]");
	}

	public List<String> getAllowAction() {
		return allowAction;
	}

	public void setAllowAction(List<String> allowAction) {
		this.allowAction = allowAction;
	}

}
