package com.photography.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.alipay.util.UtilDate;
import com.photography.controller.view.ProjectOrderCoupon;
import com.photography.dao.exp.Condition;
import com.photography.dao.exp.Expression;
import com.photography.exception.ErrorCode;
import com.photography.exception.ErrorMessage;
import com.photography.exception.ServiceException;
import com.photography.mapping.Project;
import com.photography.mapping.ProjectOrder;
import com.photography.mapping.User;
import com.photography.mapping.UserCoupon;
import com.photography.service.IProjectOrderService;
import com.photography.service.IUserCouponService;
import com.photography.utils.Constants;
import com.photography.utils.DoubleUtil;
import com.photography.utils.MessageConstants;

/**
 * 活动订单
 * @author 徐雁斌
 * @since 2015-5-27
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Controller
@RequestMapping("/projectorder")
public class ProjectOrderController extends BaseController {
	
	private final static Logger log = Logger.getLogger(ProjectOrderController.class);
	
	@Autowired
	private IProjectOrderService projectOrderService;
	
	@Autowired
	private IUserCouponService userCouponService;

	public void setProjectOrderService(IProjectOrderService projectOrderService) {
		this.projectOrderService = projectOrderService;
	}
	
	public void setUserCouponService(IUserCouponService userCouponService) {
		this.userCouponService = userCouponService;
	}

	/**
	 * 预定活动页面
	 * @param id
	 * @param request
	 * @param model
	 * @return
	 * @author 徐雁斌
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/toProjectCheckout")
	public ModelAndView toProjectCheckout(ProjectOrder projectOrder,HttpServletRequest request,RedirectAttributes ra){
		ModelAndView mav = new ModelAndView();
		try{
			User user = getSessionUser(request);
			if(projectOrder != null && !StringUtils.isEmpty(projectOrder.getId())){
				projectOrder = projectOrderService.loadPojo(ProjectOrder.class, projectOrder.getId());
			}else{
				if(projectOrder != null && projectOrder.getProject() != null && !StringUtils.isEmpty(projectOrder.getProject().getId())){
					
					//判断之前是否生成订单
					List<Expression> exps =  new ArrayList<Expression>(); 
					exps.add(Condition.eq("project.id", projectOrder.getProject().getId()));
					exps.add(Condition.eq("user.id", user.getId()));
					exps.add(Condition.eq("status", Constants.USER_ORDER_STATUS_WZF));
					List<ProjectOrder> projectOrderDBs = projectOrderService.loadPojoByExpression(ProjectOrder.class, Condition.and(exps), null);
					if(!projectOrderDBs.isEmpty()){
						ProjectOrder projectOrderDB = projectOrderDBs.get(0);
						projectOrderDB.setNumber(projectOrder.getNumber());
						projectOrder = projectOrderDB;
					}else{
						Project project = projectOrderService.loadPojo(Project.class, projectOrder.getProject().getId());
						if(project == null){
							throw new ServiceException(ErrorCode.PROJECT_NOT_EXIST);
						}
						projectOrder.setProject(project);
						
						//生成订单号
						projectOrder.setOrderNumber(UtilDate.getOrderNum());
					}
				}
				projectOrder.setUser(user);
				projectOrder.setStatus(Constants.USER_ORDER_STATUS_WZF);
				projectOrderService.savePojo(projectOrder, user);
			}
			
			mav.addObject("projectOrder", projectOrder);
			mav.addObject("projectOrderCoupon", getEnableUserCoupon(projectOrder));
			
			mav.setViewName("project/project_checkout");
			
			return mav;
			
		}catch(Exception e){
			mav = new ModelAndView();
			log.error("ProjectOrderController toProjectCheckout",e);
			handleError(ra, e);
			String projectId = projectOrder.getProject().getId();
			mav.setViewName("redirect:/project/toReview?id=" + projectId);
			return mav;
		}
	}
	
	//根据活计算可以使用的胶卷数量和抵扣钱数
	private ProjectOrderCoupon getEnableUserCoupon(ProjectOrder projectOrder){
		ProjectOrderCoupon projectOrderCoupon = new ProjectOrderCoupon();
		
		//胶卷
		if(projectOrder.getProject() != null && !StringUtils.isEmpty(projectOrder.getProject().getEnableCouponNum())){
			
			//如果用户胶卷数量为空，则按原始价格处理
			if(StringUtils.isEmpty(projectOrder.getUser().getCouponNum())){
				projectOrderCoupon.setEnableCouponNum("0");
				projectOrderCoupon.setUseCouponRmb("0");
			}
			
			//活动限制使用胶卷数量
			int limitCouponNum = Integer.parseInt(projectOrder.getProject().getEnableCouponNum());
			
			//1胶卷兑换人名币数量
			double oneCouponRmb = Double.parseDouble(userCouponService.getUserCouponSettingRmb());
			
			//用户胶卷数量
			int userCouponNum = Integer.parseInt(projectOrder.getUser().getCouponNum());
			
			//订购数量
			double num = Double.parseDouble(projectOrder.getNumber());
			
			//订购单价
			double cost = Double.parseDouble(projectOrder.getProject().getCost());
			
			//订购总量
			double totalCost = DoubleUtil.mul(num, cost);
			
			//免费活动不使用胶卷
			if(totalCost == 0){
				projectOrderCoupon.setIsUseCoupon(Constants.NO);
				projectOrderCoupon.setEnableCouponNum("0");
				projectOrderCoupon.setUseCouponRmb("0");
				return projectOrderCoupon;
			}
			
			//需要胶卷数量
			int enableCouponNum;
			
			//胶卷抵扣的钱数
			double useCouponRmb = 0;
			
			//活动使用胶卷无限制
			if(limitCouponNum == 0){
				
				enableCouponNum =  (int) DoubleUtil.div(totalCost, oneCouponRmb, 0);
				
				//如果用户胶卷小于需要胶卷数量，则按用户胶卷计算
				if(enableCouponNum > userCouponNum){
					enableCouponNum = userCouponNum;
				}
					
				useCouponRmb = DoubleUtil.mul(enableCouponNum, oneCouponRmb);
				
			}else{
				if(limitCouponNum > userCouponNum){
					enableCouponNum = userCouponNum;
				}else{
					enableCouponNum = limitCouponNum;
				}
				
				useCouponRmb = DoubleUtil.mul(enableCouponNum, oneCouponRmb);
				
			}
			
			projectOrderCoupon.setIsUseCoupon(Constants.YES);
			projectOrderCoupon.setEnableCouponNum(Integer.toString(enableCouponNum));
			projectOrderCoupon.setUseCouponRmb(Double.toString(useCouponRmb));
			return projectOrderCoupon;
		}else{
			projectOrderCoupon.setIsUseCoupon(Constants.NO);
			projectOrderCoupon.setEnableCouponNum("0");
			projectOrderCoupon.setUseCouponRmb("0");
			return projectOrderCoupon;
		}
	}
	
	/**
	 * 用户支付
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/orderProject",produces = "text/html;charset=UTF-8")
	public ModelAndView orderProject(ProjectOrder projectOrder,HttpServletRequest request,RedirectAttributes ra){
		log.debug("ProjectOrderControl orderProject");
		ModelAndView mav = new ModelAndView();
		
		try{
			User user = getSessionUser(request);
			
			ProjectOrder projectOrderDB = projectOrderService.loadPojo(ProjectOrder.class, projectOrder.getId());
			projectOrderDB.setCoupon(projectOrder.getCoupon());
			projectOrderDB.setOriginalPrice(projectOrder.getOriginalPrice());
			projectOrderDB.setUnitPrice(projectOrder.getUnitPrice());
			projectOrderDB.setActualPrice(projectOrder.getActualPrice());
			projectOrderService.savePojo(projectOrderDB, user);
			
			//如果订单金额为零，则直接转入成功页面
			if("0.00".equals(projectOrderDB.getActualPrice())){
				return orderCompleteProject( projectOrder, request);
			}
			
			//必填，不能修改
			//服务器异步通知页面路径
			String notify_url = "http://np1827.com/projectorder/aliPayNotify";
			//需http://格式的完整路径，不能加?id=123这类自定义参数
	
			//页面跳转同步通知页面路径
			String return_url = "http://np1827.com/projectorder/aliPayReturn";
			//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
	
			//商户订单号
			String out_trade_no = projectOrderDB.getOrderNumber();
			//商户网站订单系统中唯一订单号，必填
	
			//订单名称
			String subject = projectOrderDB.getProject().getName();
			//必填
	
			//付款金额
			String total_fee = projectOrderDB.getActualPrice();
			//必填
	
			//订单描述
	
			String body = projectOrderDB.getProject().getName();
			//商品展示地址
	//		String show_url = new String(request.getParameter("WIDshow_url").getBytes("ISO-8859-1"),"UTF-8");
			//需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
	
			//防钓鱼时间戳
			String anti_phishing_key = "";
			//若要使用请调用类文件submit中的query_timestamp函数
	
			//客户端的IP地址
			String exter_invoke_ip = "";
			//非局域网的外网IP地址，如：221.0.0.1
			
			log.debug("out_trade_no:" + out_trade_no);
			log.debug("total_fee:" + total_fee);
			log.debug("subject:" + subject);
			//////////////////////////////////////////////////////////////////////////////////
			
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "create_direct_pay_by_user");
	        sParaTemp.put("partner", AlipayConfig.partner);
	        sParaTemp.put("seller_email", AlipayConfig.seller_email);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", AlipayConfig.payment_type);
			sParaTemp.put("notify_url", notify_url);
			sParaTemp.put("return_url", return_url);
			sParaTemp.put("out_trade_no", out_trade_no);
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", total_fee);
			sParaTemp.put("body", body);
	//		sParaTemp.put("show_url", show_url);
			sParaTemp.put("anti_phishing_key", anti_phishing_key);
			sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
			
			//建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
			log.debug("orderProject sHtmlText:" + sHtmlText);
			mav.addObject("sHtmlText",sHtmlText);
			mav.setViewName("/alipay/alipay");
		}catch(Exception e){
			log.error("orderProject error",e);
			mav = new ModelAndView();
			log.error("ProjectOrderController orderProject",e);
			handleError(ra, e);
			ra.addFlashAttribute("projectOrder", projectOrder);
			mav.setViewName("redirect:/projectorder/toProjectCheckout");
		}
		return mav;
	}
	
	
	/**
	 * 支付通知
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/aliPayNotify")
	@ResponseBody
	public String aliPayNotify(HttpServletRequest request){
		log.debug("ProjectOrderControl aliPayNotify");
		try{
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				log.debug("valueStr:" + valueStr);
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			
			//交易费用
			String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
			
			//收款用户
			String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");
			
			log.debug("out_trade_no:" + out_trade_no);
			log.debug("trade_no:" + trade_no);
			log.debug("trade_status:" + trade_status);
			log.debug("total_fee:" + total_fee);
			log.debug("seller_id:" + seller_id);
	
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	
			if(AlipayNotify.verify(params)){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
	
				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					
					handlerOrder(out_trade_no, trade_no,total_fee, seller_id,"TRADE_FINISHED");
					
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
						//如果有做过处理，不执行商户的业务程序
						
					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
					handlerOrder(out_trade_no, trade_no,total_fee, seller_id,"TRADE_SUCCESS");
				}
	
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
					
				return "success";	//请不要修改或删除
	
				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
				handlerOrder(out_trade_no, trade_no,total_fee, seller_id,"TRADE_FAIL");
				return "fail";
			}
		}catch (Exception e){
			log.error("ProjectOrderController aliPayNotify",e);
			return "fail";
		}
		
	}
	
	/**
	 * 处理支付消息
	 * @param out_trade_no 订单号 
	 * @param total_fee 订单总费用
	 * @param seller_id 订单收款用户
	 * @param status
	 * 				TRADE_FINISHED (表示交易已经成功结束，并不能再对该交易做后续操作);
	 * 				TRADE_SUCCESS  (表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
	 *              TRADE_FAIL     失败
	 * 				
	 * @throws ServiceException 
	 */
	private void handlerOrder(String out_trade_no,String trade_no,String total_fee,String seller_id,String status) throws ServiceException{
		try{
			ProjectOrder projectOrderDB = projectOrderService.loadOnePojoByExpression(ProjectOrder.class, Condition.eq("orderNumber", out_trade_no),null);
			
			if(projectOrderDB == null){
				log.error("订单 out_trade_no:" + out_trade_no + " is not existed");
				throw new ServiceException(ErrorCode.ALIPAY_ORDER_NOTEXISTED_EXCEPTION);
			}
			
			if(Double.parseDouble(total_fee)  != Double.parseDouble(projectOrderDB.getActualPrice()) 
					|| !seller_id.equals(AlipayConfig.partner)){
				log.error("订单 total_fee:" + Double.parseDouble(total_fee) + ", actual total fee:" + Double.parseDouble(projectOrderDB.getActualPrice()));
				log.error("订单 seller_id:" + seller_id + ", actual seller_id:" + AlipayConfig.partner);
				throw new ServiceException(ErrorCode.ALIPAY_NOTIFY_MESSAGE_EXCEPTION);
			}
			
			if(status.equals("TRADE_SUCCESS")){
				log.debug("订单 out_trade_no:" + out_trade_no + " 支付成功");
				projectOrderDB.setStatus(Constants.USER_ORDER_STATUS_YZF);
				projectOrderDB.setTradeNo(trade_no);
				projectOrderDB.setTradeStatus("TRADE_SUCCESS");
					
				//使用胶卷,则生成胶卷使用记录
				if(!StringUtils.isEmpty(projectOrderDB.getCoupon()) && !"0".equals(projectOrderDB.getCoupon())){
						UserCoupon userCoupon = new UserCoupon();
						userCoupon.setCouponNum(projectOrderDB.getCoupon());
						userCoupon.setInOrExp(Constants.COUPON_CLASS_SPEND);
						userCoupon.setType(Constants.COUPON_TYPE_CONSUME_ORDERPROJECT);
						userCoupon.setUser(projectOrderDB.getUser());
				}
					
				//支付成功，赠送胶卷
				addUserCoupon(null,projectOrderDB.getUser());
				
			}else if(status.equals("TRADE_FINISHED")){
				log.debug("订单 out_trade_no:" + out_trade_no + " 完成成功");
				projectOrderDB.setTradeStatus("TRADE_SUCCESS");
				
			}else if(status.equals("TRADE_FAIL")){
				log.debug("订单 out_trade_no:" + out_trade_no + " 失败");
				projectOrderDB.setStatus(Constants.USER_ORDER_STATUS_ZFSB);
				projectOrderDB.setTradeStatus("TRADE_FAIL");
			}
			projectOrderDB.setStatus(Constants.USER_ORDER_STATUS_YZF);
			projectOrderService.savePojo(projectOrderDB, projectOrderDB.getUser());
	
		}catch(Exception e){
			log.error("ProjectOrderController OrderSuccess",e);
			throw new ServiceException(ErrorCode.ALIPAY_SAVE_ORDER_EXCEPTION);
		}
	}
	
	/**
	 * 支付宝页面跳转同步通知页面
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/aliPayReturn")
	public ModelAndView aliPayReturn(HttpServletRequest request) throws UnsupportedEncodingException{
		log.debug("ProjectOrderControl aliPayReturn");
		
		ModelAndView mav = new ModelAndView();
		
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			log.debug("ProjectOrderControl valueStr:" + valueStr);
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		
		log.debug("out_trade_no:" + out_trade_no);
		log.debug("trade_no:" + trade_no);
		log.debug("trade_status:" + trade_status);
		log.debug("verify_result:" + verify_result);
		
		ProjectOrder projectOrderDB = projectOrderService.loadOnePojoByExpression(ProjectOrder.class, Condition.eq("orderNumber", out_trade_no),null);
		
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
			}
			
			//自动登录
			setSessionUser(request, projectOrderDB.getUser());
			
			
			mav.addObject("projectOrder", projectOrderDB);
			mav.setViewName("project/project_checkout_complete");

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			mav.addObject("projectOrder", projectOrderDB);
			mav.setViewName("project/project_checkout_error");
		}
		
		return mav;
	}
	
	/**
	 * 不需要支付费用的订单，直接跳转到支付成功页面
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 * @throws ServiceException 
	 */
	private ModelAndView orderCompleteProject(ProjectOrder projectOrder,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		try{
			User user = getSessionUser(request);
			
			ProjectOrder projectOrderDB = projectOrderService.loadPojo(ProjectOrder.class, projectOrder.getId());
//			projectOrderDB.setCoupon(projectOrder.getCoupon());
//			projectOrderDB.setOriginalPrice(projectOrder.getOriginalPrice());
//			projectOrderDB.setUnitPrice(projectOrder.getUnitPrice());
//			projectOrderDB.setActualPrice(projectOrder.getActualPrice());
			projectOrderDB.setStatus(Constants.USER_ORDER_STATUS_YZF);
			projectOrderService.savePojo(projectOrderDB, user);
	
			//使用胶卷,则生成胶卷使用记录
			if(!StringUtils.isEmpty(projectOrder.getCoupon()) && !"0".equals(projectOrder.getCoupon())){
				UserCoupon userCoupon = new UserCoupon();
				userCoupon.setCouponNum(projectOrder.getCoupon());
				userCoupon.setInOrExp(Constants.COUPON_CLASS_SPEND);
				userCoupon.setType(Constants.COUPON_TYPE_CONSUME_ORDERPROJECT);
				userCoupon.setUser(user);
				String userCouponNum = userCouponService.addCoupon(userCoupon, user);
				user.setCouponNum(userCouponNum);
			}
			
			//支付成功，赠送胶卷
			addUserCoupon(request,user);
			
			mav.addObject("projectOrder", projectOrderDB);
			mav.setViewName("project/project_checkout_complete");
		}catch(Exception e){
			log.error("ProjectController orderProject",e);
			handleErrorModelAndView(mav, e);
			mav.addObject("projectOrder", projectOrder);
			mav.setViewName("project/project_checkout");
		}
		
		return mav;
	}
	
	/**
	 * 取消订单
	 */
	@RequestMapping(value="/cancelProjectOrder")
	public String cancelProjectOrder(String id,HttpServletRequest request,RedirectAttributes attr){
		try{
			User user = getSessionUser(request);
			if(!StringUtils.isEmpty(id)){
				ProjectOrder projectOrder = projectOrderService.loadPojo(ProjectOrder.class, id);
				if(Constants.USER_ORDER_STATUS_WZF.equals(projectOrder.getStatus())){
					projectOrder.setStatus(Constants.USER_ORDER_STATUS_YQX);
					projectOrderService.savePojo(projectOrder, user);
				}
			}
			
			attr.addFlashAttribute(Constants.SUCCESS_MESSAGE, MessageConstants.SAVE_SUCCESS);
		}catch(Exception e){
			log.error("ProjectOrderController cancelProjectOrder",e);
			attr.addFlashAttribute(Constants.ERROR_MESSAGE, ErrorMessage.getErrorMessage(e)); 
		}
		
		return "redirect:/userinfo/toUserInfo?type=4";
	}
	
	/**
	 * 预定活动，赠送胶卷
	 * @param request
	 * @param user
	 */
	private void addUserCoupon(HttpServletRequest request,User user){
		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setCouponNum(userCouponService.getUserCouponSetting(Constants.COUPON_TYPE_CONSUME_ORDERPROJECT).getNum());
		userCoupon.setInOrExp(Constants.COUPON_CLASS_INCOME);
		userCoupon.setType(Constants.COUPON_TYPE_CONSUME_ORDERPROJECT);
		userCoupon.setUser(user);
		try {
			String userCouponNum = userCouponService.addCoupon(userCoupon, user);
			user.setCouponNum(userCouponNum);
			if(request != null){
				setSessionUser(request, user);
			}
		} catch (ServiceException e) {
			log.error("ProjectOrderController addUserCoupon",e);
		}
	}
}
