package com.sl.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.sl.file.util.Constant;

@Configuration
public class UploadConfiguration {
	
	@Bean
	public WebMvcConfigurer uploadConfigurer(){
		return new UploadConfigurerAdapter();
	}
	
	class UploadConfigurerAdapter implements WebMvcConfigurer {
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler(String.format("/%s/**", Constant.UPLOAD_PATH_PREFIX)).addResourceLocations(String.format("file:%s", Constant.UPLOAD_STORAGEPATH));
		}
	}
}