package com.photography.cache;

import java.io.Serializable;

import com.photography.exception.ErrorCode;
import com.photography.exception.SystemException;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

/**
 * Cache implementation with EhCache.<br>http://ehcache.sourceforge.net/
 *
 * @since 2011-12-1
 * @author Zhao Zhihong
 *
 * @Copyright (C) 2012 天大求实电力新技术股份有限公司 版权所有
 */
public class EhCache implements Cache {
	private net.sf.ehcache.Cache cache;

	public EhCache(net.sf.ehcache.Cache cache){
		this.cache = cache;
	}

	public Object get(Serializable key) throws SystemException {
		if (key == null) {
			return null;
		}

		try {
			Element element = cache.get(key);
			return element != null ? element.getObjectValue() : null;
		} catch (CacheException e) {
			throw new SystemException(ErrorCode.CACHE_EXCEPTION, e);
		}
	}

	public boolean containsKey(Serializable key) {
		try {
			return cache.isKeyInCache(key);
		} catch (CacheException e) {
			return false;
		}
	}

	public void put(Serializable key, Serializable value) {
		cache.put(new Element(key, value));
	}

	public void remove(Serializable key) {
		if (containsKey(key)) {
			cache.remove(key);
		}
	}

	public void removeAll() {
		cache.removeAll();
	}

	public long getElementCountInMemory() {
		return cache.getMemoryStoreSize();
	}

	public long getElementCountOnDisk() {
		return cache.getDiskStoreSize();
	}

	public long getSizeInMemory() {
		try {
			return this.cache.calculateInMemorySize();
		} catch (Throwable t) {
			return -1L;
		}
	}

	public String getName() {
		return this.cache.getName();
	}

	public void destroy() {
		try {
			cache.getCacheManager().removeCache(cache.getName());
		} catch (Throwable t) {
		}
	}
}
