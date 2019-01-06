package com.sl.file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlController {
	
	@RequestMapping("/{pageName}")
	public String getPage(@PathVariable String pageName){
		return pageName;
	}
}
