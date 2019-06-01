package com.sl.file.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sl.common.model.db.SlFile;
import com.sl.file.service.IconService;

@RestController
@RequestMapping("/inner/icon")
public class InnerIconController extends FileLoader {
	@Autowired
	private IconService iconService;
	
	@RequestMapping("/{fileId}")
	public String get(@PathVariable Long fileId) {
		Assert.notNull(fileId, "fileId should not be null or empty");
		
		SlFile file = this.iconService.getFile(fileId);
		if(file == null){
			throw new IllegalArgumentException("file not found");
		}
		
		return this.getFileContextUrl(file);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Map<Long, String> getList(@RequestParam("fileIds[]") Set<Long> fileIds) {
		Assert.notEmpty(fileIds, "fileIds should not be null or empty");
		
		List<SlFile> files = this.iconService.getFiles(fileIds);
		
		Map<Long, String> result = new HashMap<>();
		files.forEach(file -> {
			result.put(file.getFileId(), this.getFileContextUrl(file));
		});
		
		return result;
	}
}
