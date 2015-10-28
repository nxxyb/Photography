package com.photography.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.photography.controller.propertyeditor.DateEditor;
import com.photography.controller.propertyeditor.DoubleEditor;
import com.photography.controller.propertyeditor.FloatEditor;
import com.photography.controller.propertyeditor.IntegerEditor;
import com.photography.controller.propertyeditor.LongEditor;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.User;
import com.photography.utils.Constants;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-11
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
public class BaseController {
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {

		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(int.class, new IntegerEditor());
		binder.registerCustomEditor(long.class, new LongEditor());
		binder.registerCustomEditor(double.class, new DoubleEditor());
		binder.registerCustomEditor(float.class, new FloatEditor());
	}
	
	/**
	 * 从session取出user，如果不存在则抛出异常
	 * @param request
	 * @param mav
	 */
	protected User getSessionUser(HttpServletRequest request) throws ServiceException{
		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if(user == null){
			throw new ServiceException(ErrorCode.SESSION_TIMEOUT);
		}
		return user;
	}
	
	/**
	 * 将user添加到session
	 * @param request
	 * @param user
	 * @author 徐雁斌
	 */
	protected void setSessionUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
	}
	
	/**
	 * 获取错误信息
	 */
	protected String getErrorMessage(Exception e) {
		String message = null;
		if(e instanceof ServiceException){
			ServiceException se = (ServiceException) e;
			message = se.getErrorMessage();
		}else{
			message = ErrorMessage.get(ErrorCode.UNKNOWN_ERROR);
		}
		return message;
	}
	
	/**
	 * 处理错误消息
	 * @param attr
	 * @param e
	 * @author 徐雁斌
	 */
	protected void handleError(RedirectAttributes attr, Exception e) {		
		attr.addFlashAttribute(Constants.ERROR_MESSAGE, getErrorMessage(e));
	}
	
	/**
	 * 处理错误消息
	 * @param attr
	 * @param e
	 * @author 徐雁斌
	 */
	protected void handleErrorModelAndView(ModelAndView mv, Exception e) {
		mv.addObject(Constants.ERROR_MESSAGE, getErrorMessage(e));
	}
	
	

}
