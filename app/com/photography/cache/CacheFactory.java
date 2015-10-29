package com.photography.cache;

import com.photography.exception.SystemException;

/**
 * 缓存工厂
 *
 * @since 2011-12-1
 * @author Zhao Zhihong
 *
 * @Copyright (C) 2012 天大求实电力新技术股份有限公司 版权所有
 */
public interface CacheFactory {

	/**
	 * Gets a cache if it already exists or creates a new one.
	 * @param cacheName
	 * @return the cache
	 * @throws SystemException
	 */
	public Cache getCache(String cacheName) throws SystemException;
}
