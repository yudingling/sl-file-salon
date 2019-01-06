package com.sl.file.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sl.common.model.SToken;
import com.sl.common.model.db.SlFile;
import com.sl.file.mapper.SlFileMapper;
import com.zeasn.common.feign.api.SnowFlakeApi;
import com.zeasn.common.util.Common;

@Service
public class IconService {
	@Autowired
	private SnowFlakeApi snowFlakeApi;
	@Autowired
	private SlFileMapper fileMapper;
	
	public SlFile getFile(Long fileId){
		return this.fileMapper.selectByPrimaryKey(fileId);
	}
	
	public SlFile saveFile(MultipartFile file, SToken token, String contextPath, boolean isPrivate) throws IOException{
    	String filePfx = String.format("upload/icon/%s/%d/", (new SimpleDateFormat("yyyyMMdd")).format(new Date()), token.getUserId());
    	String fileNm = this.getUniqueFileName(file.getOriginalFilename());
    	
    	File sysFile = new File(contextPath + filePfx + fileNm);
    	
        if(sysFile.getParent() != null && !new File(sysFile.getParent()).exists()){
		    new File(sysFile.getParent()).mkdirs();
		}
        
        file.transferTo(sysFile);
        
        SlFile saved = this.saveFileToDB(filePfx, fileNm, token.getUserId(), isPrivate);
        
        if(saved == null){
        	sysFile.delete();
        	return null;
        	
        }else{
        	return saved;
        }
	}
	
	private SlFile saveFileToDB(String filePfx, String fileNm, Long uId, boolean isPrivate){
		Long ts = System.currentTimeMillis();
		
		SlFile file = new SlFile(this.snowFlakeApi.nextId(), filePfx, fileNm, isPrivate ? 1 : 0, uId, ts, ts);
		return this.fileMapper.insert(file) == 1 ? file : null;
	}
	
	protected final String getUniqueFileName(String orgName){
		int index = orgName.lastIndexOf(".");
		String endFix = index >= 0 ? orgName.substring(index) : "";
		
		return Common.uuid32() + endFix;
	}
}
