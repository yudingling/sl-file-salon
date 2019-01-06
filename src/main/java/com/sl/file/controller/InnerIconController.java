package com.sl.file.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sl.common.model.db.SlFile;
import com.sl.file.service.IconService;

@RestController
@RequestMapping("/inner/icon")
public class InnerIconController extends FileLoader {
	@Autowired
	private IconService iconService;
	
	@RequestMapping("/{fileId}")
	public String get(@PathVariable Long fileId) throws IOException{
		Assert.notNull(fileId, "fileId should not be null or empty");
		
		SlFile file = this.iconService.getFile(fileId);
		if(file == null){
			throw new IllegalArgumentException("file not found");
		}
		
		return this.getFileContextUrl(file);
	}
}
