package com.sl.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.common.model.SToken;
import com.sl.common.util.Constant;
import com.zeasn.common.redis.ObjectRedisApi;

@Service
public class TokenService {
	@Autowired
	private ObjectRedisApi objRedisApi;
	
	public SToken getToken(String token){
		String key = String.format("%s%s", Constant.REDIS_PREFIX_TOKEN, token);
		return this.objRedisApi.get(key);
	}
}
