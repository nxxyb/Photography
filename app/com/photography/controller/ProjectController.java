package com.photography.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.User;
import com.photography.service.IProjectService;
import com.photography.utils.Constants;
import com.photography.utils.CustomizedPropertyPlaceholderConfigurer;
import com.photography.utils.FileUtil;

/**
 * 活动
 * @author 徐雁斌
 * @since 2015-3-13
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
	
	private final static Logger log = Logger.getLogger(ProjectController.class);
	
	public final static String PROJECT_FILE = "user.project.file";
	
	@Autowired
	private IProjectService projectService;
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}
	
	/**
	 * 进入新建页面
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/toCreate")
	public ModelAndView toCreate(HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_create");
		return mav;
	}

	/**
	 * 新建活动
	 * @param project
	 * @param imgFiles
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 */
	@RequestMapping(value="/create")
	public ModelAndView create(Project project,@RequestParam MultipartFile[] photoPics,@RequestParam MultipartFile[] modelPics,HttpServletRequest request, Model model){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("project/project_create");
		try{
			User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
			if(user == null){
				mav.addObject("project", project);
				mav.addObject("errorMessage", ErrorMessage.get(ErrorCode.SESSION_TIMEOUT));
				return mav;
			}
			
			//绝对路径
			String filePath = request.getSession().getServletContext().getRealPath((String)
        			CustomizedPropertyPlaceholderConfigurer.getContextProperty(PROJECT_FILE))  + File.separator + user.getEmail();       	
        	//相对路径
        	String relativePath = CustomizedPropertyPlaceholderConfigurer.getContextProperty(PROJECT_FILE) + user.getEmail();
        	
        	String photoPicStrs = saveAndReturnFile(filePath, relativePath,photoPics);
        	project.setPhotos(photoPicStrs);
        	
        	String modelPicStrs = saveAndReturnFile(filePath, relativePath,modelPics);
        	project.setModelPhotos(modelPicStrs);
        	
        	project.setCreateUser(user);
        	
        	projectService.savePojo(project, user);
        	mav.addObject("project", project);
			
		}catch(Exception e){
			if(e instanceof ServiceException){
				ServiceException se = (ServiceException) e;
				mav.addObject("errorMessage", se.getErrorMessage()); 
				mav.setViewName("error/error");
			}else{
				log.error("error",e);
				mav.addObject("error_message", ErrorMessage.get(ErrorCode.UNKNOWN_ERROR));
				mav.setViewName("error/error");
			}
		}
		return mav;
	}

	/**
	 * @param filePath     文件保存路径
	 * @param relativePath 文件相对路径
	 * @param files        文件对象
	 * @return
	 * @throws Exception
	 * @author 徐雁斌
	 */
	private String saveAndReturnFile(String filePath, String relativePath,MultipartFile[] files) throws Exception {
		StringBuffer fileStrs = new StringBuffer();
		for(int i=0;i<files.length;i++){
			MultipartFile file =  files[i];
			if(file.isEmpty()){
				continue;
			}
			FileUtil.saveFile(filePath, file);
			fileStrs.append(relativePath + "/" + file.getOriginalFilename());
			if(i != files.length-1){
				fileStrs.append(",");
			}
		}
		return fileStrs.toString();
	}
	
	@RequestMapping(value="/test")
	public ModelAndView test(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Project project = (Project) projectService.loadPojo("40288f814d124d52014d12602dbf0001");
		mv.addObject("project", project);
		mv.setViewName("project/project_create");
		return mv;
	}
	
	
}
