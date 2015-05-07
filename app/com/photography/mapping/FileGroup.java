package com.photography.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * 文件组 
 * @author 徐雁斌
 * @since 2015-5-7
 * 
 * @copyright 2015 天大求实电力新技术股份有限公司 版权所有
 */
@Entity(name="file_group")
public class FileGroup extends BaseMapping {

	private static final long serialVersionUID = 1720098798312028455L;
	
	//文件
	@OneToMany(mappedBy="fileGroup",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value= "create_time ASC")
	private List<FileInfo> fileInfos = new ArrayList<FileInfo>();

	public List<FileInfo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}
	
}
