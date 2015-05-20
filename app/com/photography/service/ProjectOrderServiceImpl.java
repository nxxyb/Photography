package com.photography.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.exception.ErrorCode;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.utils.Constants;

/**
 * 
 * @author 徐雁斌
 * @since 2015-5-19
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@SuppressWarnings("unchecked")
@Service("projectOrderService")
public class ProjectOrderServiceImpl extends BaseServiceImpl implements IProjectOrderService {

	/* 
	 * @see com.photography.service.IProjectOrder#isCanYd(java.lang.String, java.lang.String)
	 */
	@Override
	public String isCanYd(String userId, String projectId) {
		List<Expression> exps = new ArrayList<Expression>();
		exps.add(Condition.eq("user.id", userId));
		exps.add(Condition.eq("project.id", projectId));
		exps.add(Condition.or(Condition.eq("status", Constants.USER_ORDER_STATUS_WZF), Condition.eq("status", Constants.USER_ORDER_STATUS_YZF)));
		List<ProjectOrder> orders = hibernateDao.getByQuery(ProjectOrder.class, Condition.and(exps));
		if(orders == null || orders.isEmpty()){
			return Constants.YES;
		}else{
			return Constants.NO;
		}
	}
	
	@Override
	public void saveOrderProject(String userId, String projectId) throws ServiceException {
		ProjectOrder projectOrder = new ProjectOrder();
		
		User user = hibernateDao.getById(User.class, userId);
		if(user == null){
			throw new ServiceException(ErrorCode.USER_NOT_EXIST);
		}
		
		Project project = hibernateDao.getById(Project.class, projectId);
		if(project == null){
			throw new ServiceException(ErrorCode.PROJECT_NOT_EXIST);
		}
		
		projectOrder.setUser(user);
		projectOrder.setProject(project);
		projectOrder.setStatus(Constants.USER_ORDER_STATUS_WZF);
		
		//更新预定次数
		String joinNumber = project.getJoinedNumber();
		if(joinNumber == null || "".equals(joinNumber)){
			joinNumber = "0";
		}
		project.setJoinedNumber(Integer.toString(Integer.parseInt(joinNumber) + 1));
		
		hibernateDao.saveOrUpdate(projectOrder);
		hibernateDao.saveOrUpdate(project);
		hibernateDao.flushSession();
	}

}
