package com.zetta.publisher.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.zetta.publisher.conf.ZProperty;
import com.zetta.zScheduler.utils.zLogger;

/**
 * @author SeungHun
 * @since 2016. 2. 4.
 * */
public class ZFileUtil {

	public zLogger logger = new zLogger(getClass());
	
	public String fileToString(String filePathName){
		logger.info("fileToString :: " + filePathName);
		ZProperty zProperty = new ZProperty(); 
		String dir = zProperty.getProperties("dataRootPath");
		
		File file = new File(dir + filePathName);
	
		StringBuilder sb = new StringBuilder();
		
		try {
			if(file.exists()){		
				//txt = FileUtils.readFileToString(file);
				sb.append(FileUtils.readFileToString(file, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();		
	}
	
	public void stringToFile(String filePathName, String str){
		logger.info("filePathName :: " + filePathName + "," + str);

		ZProperty zProperty = new ZProperty(); 
		String dir = zProperty.getProperties("dataRootPath");
		File file = new File(dir+ filePathName);
		
		try {
			FileUtils.writeStringToFile(file, str, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> dirFilesToStringList(){
		
		ZProperty zProperty = new ZProperty(); 
		String dir = zProperty.getProperties("dataRootPath");
		
		logger.info("dirFilesToStringList :: " + dir);
		File fileDir = new File(dir);
		
		List<File> files = (List<File>) FileUtils.listFiles(fileDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		List<String> result = new ArrayList<>();
		
		for(File file : files){
			try {
				result.add(FileUtils.readFileToString(file, "UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		return result;
	}

	public void deletefile(String filePathName) {
		logger.info("deletefile :: " + filePathName);
		ZProperty zProperty = new ZProperty(); 
		String dir = zProperty.getProperties("dataRootPath");
		File file = new File(dir + filePathName);
		
		try {
			if(file.exists())
				FileUtils.forceDelete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
