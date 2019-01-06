package com.sl.file;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sl.file.filter.TokenFilter;

@Configuration
public class WebFilterConfiguration {
	
	@Bean
	public FilterRegistrationBean<TokenFilter> filter1(){
		FilterRegistrationBean<TokenFilter> fr = new FilterRegistrationBean<>(new TokenFilter());
		fr.addUrlPatterns("/out/*");
		fr.setOrder(1);
		return fr;
	}
}
