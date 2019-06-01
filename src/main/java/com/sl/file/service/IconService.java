package com.sl.file.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tk.mybatis.mapper.entity.Example;

import com.sl.common.model.SToken;
import com.sl.common.model.db.SlFile;
import com.sl.file.mapper.SlFileMapper;
import com.zeasn.common.feign.api.SnowFlakeApi;

@Service
public class IconService {
	@Autowired
	private SnowFlakeApi snowFlakeApi;
	@Autowired
	private SlFileMapper fileMapper;
	
	public SlFile getFile(Long fileId){
		return this.fileMapper.selectByPrimaryKey(fileId);
	}
	
	public List<SlFile> getFiles(Set<Long> fileIds){
		Example example = new Example(SlFile.class);
		example.createCriteria().andIn("fileId", fileIds);
		
		return this.fileMapper.selectByExample(example);
	}
	
	public SlFile saveFile(MultipartFile file, SToken token, String filePfx, String fileNm, String storagePath, boolean isPrivate) throws IOException{
    	File sysFile = new File(storagePath);
    	
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
}
