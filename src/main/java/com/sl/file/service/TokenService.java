package com.sl.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sl.common.model.SToken;
import com.zeasn.common.redis.ObjectRedisApi;

@Service
public class TokenService {
	@Value("${file.tokenRedisPrefix}")
	private String tokeRedisPrefix;
	
	@Autowired
	private ObjectRedisApi objRedisApi;
	
	public SToken getToken(String token){
		String key = String.format("%s%s", this.tokeRedisPrefix, token);
		return this.objRedisApi.get(key);
	}
}
