package com.sl.file.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sl.common.filter.FilterHttpServletRequest;
import com.sl.common.model.SToken;
import com.sl.common.model.db.SlFile;
import com.sl.file.service.IconService;
import com.zeasn.common.model.result.ApiError;
import com.zeasn.common.model.result.ApiObjectResult;
import com.zeasn.common.model.result.ApiResult;

@RestController
@RequestMapping("/outx/icon")
public class IconXController extends FileLoader {
	@Autowired
	private IconService iconService;
	
	@RequestMapping
	public void get(@RequestParam Long fileId, @RequestParam Long userId, HttpServletRequest request, HttpServletResponse response) throws IOException{
		try{
			Assert.notNull(fileId, "fileId should not be null or empty");
			
			SlFile file = this.iconService.getFile(fileId);
			
			FilterHttpServletRequest fr = new FilterHttpServletRequest(request);
			fr.setToken(new SToken(null, userId, null));
			
			if(file == null || !this.downloadContent(file, fr, response)){
				response.setStatus(HttpStatus.SC_NOT_FOUND);
			}
			
		}catch(Exception ex){
			response.sendError(HttpStatus.SC_BAD_REQUEST, ex.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ApiResult upload(@RequestParam("file") MultipartFile file, @RequestParam Long userId, HttpServletRequest request, HttpServletResponse response){
		try{
			if (file != null && !file.isEmpty() && file.getSize() > 0) {
				if(file.getSize() / 1024 > 1024){
					return ApiResult.error(ApiError.ARGUMENT_ERROR, "max file size 1024KB");
				}
				
				FilterHttpServletRequest fr = new FilterHttpServletRequest(request);
				fr.setToken(new SToken(null, userId, null));
				
				String filePrefix = this.getFilePrefix(file, fr);
				String fileNm = this.getFileName(file);
				String storagePath = this.getFileStoragePath(filePrefix, fileNm);
				
				SlFile saved = this.iconService.saveFile(file, fr.getToken(), filePrefix, fileNm, storagePath, false);
				if(saved == null){
					return ApiResult.error(ApiError.INTERNAL_ERROR);
					
				}else{
					return new ApiObjectResult<>(saved);
				}
				
	        }else{
	        	return ApiResult.error(ApiError.ARGUMENT_ERROR, "no file was found");
	        }
			
		}catch (Exception e) {
			return ApiResult.error(ApiError.INTERNAL_ERROR, e.getMessage());
        }
	}
}
