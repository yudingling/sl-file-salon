package com.sl.file.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sl.common.model.SToken;
import com.zeasn.common.redis.ObjectRedisApi;

@Service
public class TokenService {
	@Value("${file.tokenRedisPrefix}")
	private String tokeRedisPrefix;
	
	@Value("${file.userIdFieldInToken}")
	private String userIdFieldInToken;
	
	@Autowired
	private ObjectRedisApi objRedisApi;
	
	@SuppressWarnings("rawtypes")
	public SToken getToken(String token){
		String key = String.format("%s%s", this.tokeRedisPrefix, token);
		
		Map tokenMap = this.objRedisApi.get(key);
		
		if(tokenMap != null){
			Long uId = Long.parseLong(tokenMap.get(this.userIdFieldInToken).toString());
			SToken tokenObj = new SToken();
			tokenObj.setUserId(uId);
			
			return tokenObj;
			
		}else{
			return null;
		}
	}
}
