package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 轮播活动配置表
 * @author 徐雁斌
 * @since 2015-11-3
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="admin_lb")
public class AdminLb extends BaseMapping {

	private static final long serialVersionUID = 6935896212983858419L;

	//活动
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project")
	private Project project;
	
	//作品
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work")
	private Work work;
	
	//排序
	@Column(name="sort")
	private String sort;
	
	//类型 1:轮播  2：活动推荐  3：热门活动  4-热门作品
	@Column(name="type")
	private String type;

	@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}
	
	
}
