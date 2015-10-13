package com.photography.mapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author 徐雁斌
 * @since 2015-10-12
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="project_trip")
public class ProjectTrip extends BaseMapping {
	
	private static final long serialVersionUID = -4980740609816448217L;

	/**
	 * 行程标题
	 */
	@Column(name="title")
	private String title;
	
	/**
	 * 行程介绍
	 */
	@Column(name="des")
	private String des;
	
	/**
	 * 行程介绍图片
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="des_photos")
	private FileGroup desPhotos;
	
	@Transient
	private MultipartFile[] desPhotoPics;
	
	/**
	 * 所属活动
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project")
	private Project project;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public FileGroup getDesPhotos() {
		return desPhotos;
	}

	public void setDesPhotos(FileGroup desPhotos) {
		this.desPhotos = desPhotos;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public MultipartFile[] getDesPhotoPics() {
		return desPhotoPics;
	}

	public void setDesPhotoPics(MultipartFile[] desPhotoPics) {
		this.desPhotoPics = desPhotoPics;
	}

	
}
