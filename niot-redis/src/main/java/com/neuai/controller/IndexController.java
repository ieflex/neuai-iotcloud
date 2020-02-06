package com.neuai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

	@Autowired
	private StringRedisTemplate template;

	/**
	 * 根据key值从Redis缓存取得数据
	 * 
	 * @param key
	 * @return
	 */
	@RequestMapping("/redis/get/{key}")
	private String get(@PathVariable("key") String key) {
		// 返回缓存中的数据
		return template.opsForValue().get(key);
	}

	/**
	 * 设置数据到Redis缓存中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@RequestMapping("/redis/set/{key}/{value}")
	private Boolean set(@PathVariable("key") String key, @PathVariable("value") String value) {
		boolean flag = true;
		try {
			// 设置缓存中的数据
			template.opsForValue().set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

}
