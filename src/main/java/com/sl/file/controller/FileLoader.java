package com.sl.file.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.sl.common.filter.FilterHttpServletRequest;
import com.sl.common.model.SToken;
import com.sl.common.model.db.SlFile;
import com.sl.file.util.Constant;
import com.zeasn.common.log.MyLog;
import com.zeasn.common.util.Common;

public class FileLoader {
	private static final MyLog log = MyLog.getLog(FileLoader.class);
	
	private boolean checkFile(String filePath){
		return (new File(filePath)).exists();
	}
	
	public String getContentType(String filePath) {
        Path path = Paths.get(filePath);
        String ctType = null;
        try {
        	ctType = Files.probeContentType(path);
        } catch (IOException e) {
        	//
        }
        
        if (ctType == null) {
        	ctType = new MimetypesFileTypeMap().getContentType(new File(filePath));
        }
        
        return ctType;
    }
	
	/**
	 * download content
	 */
	private boolean responseContent(String fileName, String filePath, HttpServletResponse response){
		if(!this.checkFile(filePath)){
			return false;
		}
		
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			
			response.reset();
			response.setContentType(this.getContentType(filePath));
			
			Common.writeBytesFromFile(filePath, os);
			return true;
			
		} catch (IOException e) {
			log.error(String.format("get file error, fileName: %s, filePath: %s", fileName, filePath), e);
			return false;
			
		} finally {
			Common.closeStream(os);
		}
	}
	
	/**
	 * download file
	 */
	private boolean responseFile(String fileName, String filePath, HttpServletResponse response){
		if(!this.checkFile(filePath)){
			return false;
		}
		
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			
			response.reset();
			response.setHeader("Content-Disposition", "attachment; fileName=" + fileName);
			response.setContentType("multipart/form-data");
			
			Common.writeBytesFromFile(filePath, os);
			return true;
			
		} catch (IOException e) {
			log.error(String.format("get file error, fileName: %s, filePath: %s", fileName, filePath), e);
			return false;
			
		} finally {
			Common.closeStream(os);
		}
	}
	
	/**
	 * download file(stream)
	 */
	private boolean responseByteStream(String fileName, String filePath, HttpServletResponse response){
		if(!this.checkFile(filePath)){
			return false;
		}
		
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			
			response.reset();
			response.setHeader("Content-Disposition", "attachment; fileName=" + fileName);
        	response.setContentType("application/octet-stream; charset=utf-8");
			
			Common.writeBytesFromFile(filePath, os);
			return true;
			
		} catch (IOException e) {
			log.error(String.format("get file error, fileName: %s, filePath: %s", fileName, filePath), e);
			return false;
			
		} finally {
			Common.closeStream(os);
		}
	}
	
	protected boolean downloadStream(SlFile file, FilterHttpServletRequest request, HttpServletResponse response){
		if(this.isAuthed(file, request.getToken())){
			String filePath = Constant.UPLOAD_STORAGEPATH + file.getFilePfx() + file.getFileNm();
			return this.responseByteStream(file.getFileNm(), filePath, response);
			
		}else{
			return false;
		}
	}
	
	protected boolean downloadFile(SlFile file, FilterHttpServletRequest request, HttpServletResponse response){
		if(this.isAuthed(file, request.getToken())){
			String filePath = Constant.UPLOAD_STORAGEPATH + file.getFilePfx() + file.getFileNm();
			return this.responseFile(file.getFileNm(), filePath, response);
			
		}else{
			return false;
		}
	}
	
	protected boolean downloadContent(SlFile file, FilterHttpServletRequest request, HttpServletResponse response){
		if(this.isAuthed(file, request.getToken())){
			String filePath = Constant.UPLOAD_STORAGEPATH + file.getFilePfx() + file.getFileNm();
			return this.responseContent(file.getFileNm(), filePath, response);
			
		}else{
			return false;
		}
	}
	
	protected String getFileContextUrl(SlFile file){
		return String.format("%s/%s%s", Constant.UPLOAD_PATH_PREFIX, file.getFilePfx(), file.getFileNm());
	}
	
	protected String getFilePrefix(MultipartFile file, FilterHttpServletRequest request){
		return String.format("upload/icon/%s/%d/", (new SimpleDateFormat("yyyyMMdd")).format(new Date()), request.getToken().getUserId());
	}
	
	protected String getFileName(MultipartFile file){
		String orgName = file.getOriginalFilename();
		
		int index = orgName.lastIndexOf(".");
		String endFix = index >= 0 ? orgName.substring(index) : "";
		
		return Common.uuid32() + endFix;
	}
	
	protected String getFileStoragePath(String filePrefix, String fileNm){
		return Constant.UPLOAD_STORAGEPATH + filePrefix + fileNm;
	}
	
	private boolean isAuthed(SlFile file, SToken token){
		if(file.getFilePrivate() == 1){
			return file.getuId() == token.getUserId();
			
		}else{
			return true;
		}
	}
}
