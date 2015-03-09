package com.photography.mapping;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

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
	
	/**
	8、QQ
	9、个性签名
	*/
	
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
	 * 是否审核
	 */
	@Column(name="verify")
	private String verify = Constants.NO;
	
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

}
