package com.photography.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

@Component("cacheHandler")
@EnableCaching
public class CacheHandler {

	@CacheEvict(value="indexProjectCache",allEntries=true)  
	public void evictIndexProjectCache(){
//		System.out.println("evictIndexProjectCache");
	}
	
	@CacheEvict(value="indexWorkCache",allEntries=true)  
	public void evictIndexWorkCache(){
//		System.out.println("evictIndexWorkCache");
	}
	
	@CacheEvict(value="userCouponSettingCache",allEntries=true)  
	public void evictUserCouponSettingCache(){
//		System.out.println("evictIndexWorkCache");
	}
	
}
