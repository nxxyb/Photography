package com.photography.cache;

import java.io.Serializable;

import com.photography.exception.SystemException;

/**
 * Interface for caches.
 *
 * @since 2011-12-1
 * @author Zhao Zhihong
 * 
 * @Copyright (C) 2012 天大求实电力新技术股份有限公司 版权所有
 */
public interface Cache {


	/**
	 * Gets a value from the cache.
	 * @param key
	 * @return the object
	 * @throws SystemException
	 */
	public Object get(Serializable key) throws SystemException;

	/**
	 * Puts a value in the cache.
	 * @param key the key (must rewrite the equals and hashCode method inherit from java.lang.Object)
	 * @param value the value
	 */
	public void put(Serializable key, Serializable value);

	/**
	 * Removes a value from the cache.
	 * @param key the key
	 */
	public void remove(Serializable key);

	/**
	 * Removes all values from the cache.
	 */
	public void removeAll();

	/**
	 * Checks if a key is in the cache.
	 * @param key the key
	 * @return true, if the key is in the cache
	 */
	public boolean containsKey(Serializable key);

	/**
	 * get the number of elements in memory
	 * @return  the number of elements in memory
	 *
	 * @author Zhao Zhihong
	 */
	public long getElementCountInMemory();

	/**
	 *  get the number of elements on disk
	 * @return the number of elements on disk
	 *
	 * @author Zhao Zhihong
	 */
	public long getElementCountOnDisk();

	/**
	 * get the memory size
	 * @return the memory size
	 *
	 * @author Zhao Zhihong
	 */
	public long getSizeInMemory();

	/**
	 * get the cache name
	 * @return the cache name
	 *
	 * @author Zhao Zhihong
	 */
	public String getName();

	/**
	 * destroy this cache and remove from the ehcache manager
	 *
	 * @author Zhao Zhihong
	 */
	public void destroy();
}
