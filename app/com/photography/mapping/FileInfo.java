package com.photography.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * 
 * @author 徐雁斌
 * @since 2015-5-7
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="file_info")
public class FileInfo extends BaseMapping {
	
	private static final long serialVersionUID = 5876582429800017156L;
	
	//文件扩展名
	@Column(name="ext")
	private String ext;
	
	//真实文件名
	@Column(name="real_name")
	private String realName;
	
	//存储文件全路径
	@Column(name="real_path")
	private String realPath;
	
	//文件组
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_group")
	private FileGroup fileGroup;

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public FileGroup getFileGroup() {
		return fileGroup;
	}

	public void setFileGroup(FileGroup fileGroup) {
		this.fileGroup = fileGroup;
	}
	
	
}
