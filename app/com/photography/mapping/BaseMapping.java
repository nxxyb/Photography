package com.photography.mapping;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;

/**
 * 
 * @author 徐雁斌
 * @since 2015-2-6
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@MappedSuperclass
public class BaseMapping implements Serializable{

	private static final long serialVersionUID = 4967549690310825900L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name="id")
    @DocumentId
	protected String id;
	
	/**
	 * 创建时间
	 */
	@Column(name="create_time")
	protected Date createTime = new Date();
	
	/**
	 * 最后修改时间
	 */
	@Column(name="last_update_time")
	protected Date lastUpdateTime = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
