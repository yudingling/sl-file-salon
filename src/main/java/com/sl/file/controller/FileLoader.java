package com.sl.file.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import com.sl.common.filter.FilterHttpServletRequest;
import com.sl.common.model.SToken;
import com.sl.common.model.db.SlFile;
import com.zeasn.common.log.MyLog;
import com.zeasn.common.util.Common;

public class FileLoader {
	private static final MyLog log = MyLog.getLog(FileLoader.class);
	
	private boolean checkFile(String filePath){
		return (new File(filePath)).exists();
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
			log.error(String.format("download file error, fileName: %s, filePath: %s", fileName, filePath), e);
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
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        	response.setContentType("application/octet-stream; charset=utf-8");
			
			Common.writeBytesFromFile(filePath, os);
			return true;
			
		} catch (IOException e) {
			log.error(String.format("download file error, fileName: %s, filePath: %s", fileName, filePath), e);
			return false;
			
		} finally {
			Common.closeStream(os);
		}
	}
	
	protected boolean downloadStream(SlFile file, FilterHttpServletRequest request, HttpServletResponse response){
		if(this.isAuthed(file, request.getToken())){
			String filePath = request.getServletContext().getRealPath("/") + file.getFilePfx() + file.getFileNm();
			return this.responseByteStream(file.getFileNm(), filePath, response);
			
		}else{
			return false;
		}
	}
	
	protected boolean downloadFile(SlFile file, FilterHttpServletRequest request, HttpServletResponse response){
		if(this.isAuthed(file, request.getToken())){
			String filePath = request.getServletContext().getRealPath("/") + file.getFilePfx() + file.getFileNm();
			return this.responseFile(file.getFileNm(), filePath, response);
			
		}else{
			return false;
		}
	}
	
	protected String getFileContextUrl(SlFile file){
		return file.getFilePfx() + file.getFileNm();
	}
	
	private boolean isAuthed(SlFile file, SToken token){
		if(file.getFilePrivate() == 1){
			return file.getuId() == token.getUserId();
			
		}else{
			return true;
		}
	}
}
