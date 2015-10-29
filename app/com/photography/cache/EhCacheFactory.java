package com.photography.cache;

import java.io.IOException;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.ObjectExistsException;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.photography.exception.ErrorCode;
import com.photography.exception.SystemException;

/**
 * 缓存管理器
 *
 * @since 2011-12-1
 * @author Zhao Zhihong
 *
 * @Copyright (C) 2012 天大求实电力新技术股份有限公司 版权所有
 */
public class EhCacheFactory implements CacheFactory {
	private static Logger log = Logger.getLogger(EhCacheFactory.class);

	private net.sf.ehcache.CacheManager manager;

	public EhCacheFactory(Resource config) throws SystemException{
		try {
			manager = net.sf.ehcache.CacheManager.create(config.getInputStream());
		} catch (CacheException e) {
			log.error("Can't instantiate cache manager since the error:", e);
			throw new SystemException(ErrorCode.CACHE_EXCEPTION, e);
		} catch (IOException e) {
			log.error("Can't instantiate cache manager since the error:", e);
			throw new SystemException(ErrorCode.CACHE_EXCEPTION, e);
		}
	}

	public Cache getCache(String cacheName) throws SystemException {
		if (!manager.cacheExists(cacheName)) {
			try {
				manager.addCache(cacheName);
			} catch (IllegalStateException e) {
				log.error("EhCacheManager.getCache():", e);
				throw new SystemException(ErrorCode.CACHE_EXCEPTION, e);
			} catch (ObjectExistsException e) {
				log.error("EhCacheManager.getCache():", e);
				throw new SystemException(ErrorCode.CACHE_EXCEPTION, e);
			} catch (CacheException e) {
				log.error("EhCacheManager.getCache():", e);
				throw new SystemException(ErrorCode.CACHE_EXCEPTION, e);
			}
		}

		return new EhCache(manager.getCache(cacheName));
	}
}
