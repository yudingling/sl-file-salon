package com.sl.file;

import java.io.File;

import org.junit.Test;

public class Test2 {
	@Test
	public void testYt(){
		File fi = new File("D:\\seeklon\\salon\\code\\sl-file-salon\\target\\sl-file-salon-0.0.1-SNAPSHOT.jar");
		File f2i = new File("D:\\seeklon\\salon\\code\\sl-file-salon\\target\\");
		
		System.out.println(fi.isDirectory() ? fi.getPath() : fi.getParent());
		System.out.println(f2i.isDirectory() ? f2i.getPath() : f2i.getParent());
	}
}
