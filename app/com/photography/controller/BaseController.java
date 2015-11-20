package com.photography.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
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
import com.photography.mapping.FileGroup;
import com.photography.mapping.FileInfo;
import com.photography.mapping.User;
import com.photography.service.IBaseService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;
import com.photography.utils.FileUtil;

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
	protected User getSessionUser(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
//		if(user == null){
//			throw new ServiceException(ErrorCode.SESSION_TIMEOUT);
//		}
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
	
	/**
	 * @param filePath     文件保存路径
	 * @param relativePath 文件相对路径
	 * @param files        文件对象
	 * @return
	 * @throws Exception
	 * @author 徐雁斌
	 */
	protected FileGroup saveAndReturnFile(MultipartFile[] files,HttpServletRequest request,User user,FileGroup fileGroup,String basePath,IBaseService baseService) throws Exception {
		
		if(fileGroup == null){
			fileGroup = new FileGroup();
		}
		//绝对路径
		String filePath = request.getSession().getServletContext().getRealPath((String)
    			CustomizedPropertyPlaceholderConfigurer.getContextProperty(basePath))  + File.separator + user.getMobile();       	
    	//相对路径
    	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty(basePath) + user.getMobile();
  
    	
    	List<FileInfo> fileinfos = new ArrayList<FileInfo>();
		for(int i=0;i<files.length;i++){
			MultipartFile file = files[i];
			if(file.isEmpty()){
				continue;
			}
			
			FileInfo fileInfo = new FileInfo();
			fileInfo.setExt(FileUtil.getFileExtension(file.getOriginalFilename()));
			fileInfo.setRealName(file.getOriginalFilename());
			String fileName = UUID.randomUUID() + "." + fileInfo.getExt();
			fileInfo.setRealPath(relativePath + "/" + fileName);
			fileInfo.setFileGroup(fileGroup);
			
			fileinfos.add(fileInfo);
			
			
			FileUtil.saveFileByName(filePath, file, fileName);
		}
		
		if(!fileinfos.isEmpty()){
			List<FileInfo> oldFileinfos = fileGroup.getFileInfos();
			fileGroup.setFileInfos(null);
			for(FileInfo fileInfo:oldFileinfos){
				baseService.deletePojo(fileInfo, user);
			}
			
			fileGroup.setFileInfos(fileinfos);
			baseService.savePojo(fileGroup, user);
		}
		return fileGroup;
	}
	
	/**
	 * 判断是否上传文件
	 * @param files
	 * @param fileGroup
	 * @return
	 */
	protected boolean checkFiles(MultipartFile[] files,FileGroup fileGroup){
		boolean fileFlag = false;
		for(MultipartFile file : files){
			if(!file.isEmpty()){
				fileFlag = true;
				break;
			}
		}
		if(!fileFlag && (fileGroup == null || fileGroup.getFileInfos() == null || fileGroup.getFileInfos().isEmpty())){
			return false;
		}
		return true;
	}
	
	

}
