package com.photography.mapping;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.photography.utils.Constants;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-3
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="user")
public class User extends BaseMapping{
	
	private static final long serialVersionUID = 8854336652654077157L;

	/**
	 * 登录名称
	 */
	@Column(name="login_name")
	private String loginName;
	
	/**
	 * 密码
	 */
	@Column(name="password")
	private String password;
	
	/**
	 * 用户类型
	 * 管理员、个人活动发布用户、单位活动发布用户、普通用户
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 昵称
	 */
	@Column(name="nack_name")
	private String nackName;
	
	/**
	 * 真实姓名
	 */
	@Column(name="real_name")
	private String realName;
	
	/**
	 * 性别
	 */
	@Column(name="sex")
	private String sex;
	
	/**
	 * 手机号码
	 */
	@Column(name="mobile")
	private String mobile;
	
	/**
	 * 邮箱
	 */
	@Column(name="email")
	private String email;
	
	/**
	 * 生日
	 */
	@Column(name="birth_day")
	private Date birthDay;
	
	/**
	 * 审核状态
	 */
	@Column(name="verify")
	private String verify = Constants.VERIFY_NO;
	
	/**
	 * 审核时间
	 * 
	 */
	@Column(name="verify_time")
	private Date verifyTime;
	
	/**
	 * 审核结果信息
	 */
	@Column(name="verify_message")
	private String verifyMessage;
	
	/**
	 * 是否激活
	 */
	@Column(name="enable")
	private String enable = Constants.NO;
	
	/**
	 * 省
	 */
	@Column(name="province")
	private String province;
	
	/**
	 * 市
	 */
	@Column(name="city")
	private String city;
	
	/**
	 * 区
	 */
	@Column(name="county")
	private String county;
	
	/**
	 * 身份证号
	 */
	@Column(name="id_card")
	private String idCard;
	
	/**
	 * 单位名称
	 */
	@Column(name="company_name")
	private String companyName;
	
	/**
	 * 验证照片地址
	 */
	@Column(name="comfirm_pic")
	private String comfirmPic;
	
	/**
	 * 头像
	 */
	@Column(name="head_pic")
	private String headPic;
	
	/**
	 * qq
	 */
	@Column(name="qq_number")
	private String qqNumber;
	
	/**
	* 个性签名
	*/
	@Column(name="person_signature")
	private String personSignature;
	
	/**
	 * 胶卷数量
	 */
	@Column(name="coupon_num")
	private String couponNum = "0";
	
	/**
	 * 派文数量
	 */
	@Formula("(select count(*) from blog b where b.create_user = id)")
	private String blogNum;
	
	/**
	 * 派友数量
	 */
	@Formula("(select count(*) from blog_friend bf where bf.create_user = id or bf.friend_user = id)")
	private String blogFriendNum;
	
	/**
	 * 显示名称，如果有真实姓名就显示，否则显示手机号
	 */
	@Transient
	private String displayName;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNackName() {
		return nackName;
	}

	public void setNackName(String nackName) {
		this.nackName = nackName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getComfirmPic() {
		return comfirmPic;
	}

	public void setComfirmPic(String comfirmPic) {
		this.comfirmPic = comfirmPic;
	}

	public String getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	public String getPersonSignature() {
		return personSignature;
	}

	public void setPersonSignature(String personSignature) {
		this.personSignature = personSignature;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getVerifyMessage() {
		return verifyMessage;
	}

	public void setVerifyMessage(String verifyMessage) {
		this.verifyMessage = verifyMessage;
	}

	public String getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(String couponNum) {
		this.couponNum = couponNum;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getBlogNum() {
		return blogNum;
	}

	public void setBlogNum(String blogNum) {
		this.blogNum = blogNum;
	}

	public String getBlogFriendNum() {
		return blogFriendNum;
	}

	public void setBlogFriendNum(String blogFriendNum) {
		this.blogFriendNum = blogFriendNum;
	}

	public String getDisplayName() {
		if(this.realName == null || "".equals(this.realName)){
			displayName = mobile.substring(0, 3) + "XXXX" + mobile.substring(7, 11);
			return displayName;
		}else{
			return realName;
		}
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
