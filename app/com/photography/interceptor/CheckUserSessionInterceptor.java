package com.photography.interceptor;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.photography.mapping.User;
import com.photography.utils.Constants;

/**
 * 检查用户是否登录
 * @author 徐雁斌
 * @since 2015-7-24
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public class CheckUserSessionInterceptor implements HandlerInterceptor {
	
	private final static Logger log = Logger.getLogger(CheckUserSessionInterceptor.class);
	
	private List<String> checkUrls;

	public List<String> getCheckUrls() {
		return checkUrls;
	}

	public void setCheckUrls(List<String> checkUrls) {
		this.checkUrls = checkUrls;
	}

	/* 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.debug("preHandle");
		User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		String redirectURL = request.getServletPath();
		
		//拦截非登录用户访问
		if(checkUrls.contains(redirectURL) && sessionUser == null){
			if(!StringUtils.isEmpty(request.getQueryString())){
				redirectURL = redirectURL + "?" + request.getQueryString();
			}
			request.getSession().setAttribute(Constants.SESSION_LOGIN_REDIRECTURL, URLEncoder.encode(redirectURL));
			String requestType = request.getHeader("X-Requested-With");
			if(requestType == null){
				//正常请求
				response.sendRedirect(request.getHeader("referer"));
			}else{
				//ajax请求
			}
			return false;
		}
		return true;
	}

	/* 
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
//		log.debug("postHandle");
//		
//		if(modelAndView == null){
//			return;
//		}
//		
//		 //不拦截登录界面
//		 if("/userinfo/toUserInfo?type=4".equals(modelAndView.getViewName())){
//			 User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
//				if(sessionUser == null){
//					modelAndView.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
//					modelAndView.setViewName("login");
//					log.error(ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
//				}
//		 }
		 
		 
	}

	/* 
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		request.getSession().removeAttribute(Constants.SESSION_LOGIN_REDIRECTURL);
	}

}
