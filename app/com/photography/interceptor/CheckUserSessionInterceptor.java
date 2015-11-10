package com.photography.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 检查用户是否登录
 * @author 徐雁斌
 * @since 2015-7-24
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public class CheckUserSessionInterceptor implements HandlerInterceptor {
	
	private final static Logger log = Logger.getLogger(CheckUserSessionInterceptor.class);

	/* 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.debug("preHandle");
		System.out.println(request.getHeader("referer"));
		System.out.println(request.getServletPath());
		
		if("/Photography/project/index".equals(request.getRequestURI())){
			String redirectURL = request.getServletPath();
			String oldURI = request.getHeader("referer");
			if(oldURI.contains("?")){
				response.sendRedirect(request.getHeader("referer") + "&redirectURL=" + URLEncoder.encode(redirectURL));
			}else{
				response.sendRedirect(request.getHeader("referer") + "?redirectURL=" + URLEncoder.encode(redirectURL));
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
	}

}
