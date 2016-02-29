package com.photography.interceptor;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		String redirectURL = request.getServletPath();
		
		//拦截非登录用户访问
		if(checkUrls.contains(redirectURL) && sessionUser == null){
			log.debug("preHandle 拦截" + redirectURL);
			if(!StringUtils.isEmpty(request.getQueryString())){
				redirectURL = redirectURL + "?" + request.getQueryString();
			}
			Map<String, String> paramMap = getParameterMap(request);
			request.getSession().setAttribute(Constants.SESSION_LOGIN_PARAMETERMAP, paramMap);
			
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
//		request.getSession().removeAttribute(Constants.SESSION_LOGIN_REDIRECTURL);
//		request.getSession().removeAttribute(Constants.SESSION_LOGIN_PARAMETERMAP);
	}
	
	/**
	 * 获取request参数
	 * @param request
	 * @return
	 */
	private static Map<String,String> getParameterMap(HttpServletRequest request) {
	    // 参数Map
	    Map properties = request.getParameterMap();
	    // 返回值Map
	    Map<String,String> returnMap = new HashMap<String,String>();
	    Iterator entries = properties.entrySet().iterator();
	    Map.Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Map.Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}

}
