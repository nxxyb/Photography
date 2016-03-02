package com.photography.service;

import java.util.List;

import com.photography.dao.query.Pager;
import com.photography.dao.query.Sort;
import com.photography.mapping.User;

/**
 * 索引查询
 * @author Administrator
 *
 */
public interface IIndexSearchService extends IBaseService {
	
	/**
	 * 创建索引
	 */
	public void createIndex();

	/**
	 * 通过索引查询
	 * @param <T>
	 * @param clazz 实体类
	 * @param pager 分页
	 * @param keys  查询关键字
	 * @param searchString 查询内容
	 * @param sort  排序
	 * @param user  用户
	 * @return
	 */
	public <T> List<T> getIndexPojoList(Class<T> clazz, Pager pager,String[] keys, String searchString, Sort sort, User user);
}
