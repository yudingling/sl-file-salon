package com.sl.file.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sl.common.filter.FilterHttpServletRequest;
import com.sl.common.model.db.SlFile;
import com.sl.file.service.IconService;
import com.zeasn.common.model.result.ApiError;
import com.zeasn.common.model.result.ApiObjectResult;
import com.zeasn.common.model.result.ApiResult;

@RestController
@RequestMapping("/out/icon")
public class IconController extends FileLoader {
	@Autowired
	private IconService iconService;
	
	@RequestMapping("/{fileId}")
	public void get(@PathVariable Long fileId, FilterHttpServletRequest request, HttpServletResponse response) throws IOException{
		try{
			Assert.notNull(fileId, "fileId should not be null or empty");
			
			SlFile file = this.iconService.getFile(fileId);
			if(file == null || !this.downloadContent(file, request, response)){
				response.setStatus(HttpStatus.SC_NOT_FOUND);
			}
			
		}catch(Exception ex){
			response.sendError(HttpStatus.SC_BAD_REQUEST, ex.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ApiResult upload(@RequestParam("file") MultipartFile file, FilterHttpServletRequest request, HttpServletResponse response){
		try{
			if (file != null && !file.isEmpty() && file.getSize() > 0) {
				if(file.getSize() / 1024 > 1024){
					return ApiResult.error(ApiError.ARGUMENT_ERROR, "max file size 1024KB");
				}
				
				String filePrefix = this.getFilePrefix(file, request);
				String fileNm = this.getFileName(file);
				String storagePath = this.getFileStoragePath(filePrefix, fileNm);
				
				SlFile saved = this.iconService.saveFile(file, request.getToken(), filePrefix, fileNm, storagePath, false);
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
