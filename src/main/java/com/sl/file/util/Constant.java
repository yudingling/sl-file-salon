package com.sl.file.util;

import java.io.File;

import org.springframework.boot.system.ApplicationHome;

public class Constant {
	private Constant(){}
	
	static{
		File file = new ApplicationHome(Constant.class).getSource();
		UPLOAD_STORAGEPATH = (file.isDirectory() ? file.getPath() : file.getParent()) + "/";
	}
	
	/**
	 * file upload path
	 */
	public static String UPLOAD_STORAGEPATH;  //jar path in spring boot
	public static final String UPLOAD_PATH_PREFIX = "files";
}
