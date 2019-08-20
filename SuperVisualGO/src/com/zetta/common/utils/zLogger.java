package com.zetta.common.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class zLogger {

	public Logger logger = Logger.getLogger(getClass());
	String className = "";

	public zLogger(Class<?> logClass) {
		this.className = logClass.getName();
	}

	//로그를 DB에 저장한다.
	public void save(String message, String category, String subCategory, String userId) {
//		Date date = new Date();
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String formattedDate = dateFormat.format(date);
//	
//		String msg = "zettasoft," + "save," + userId + "," + formattedDate + "," + className + "," + message;
//		
//		DBHandler dbh;
//		try {
//			dbh = new DBHandler("ls", null);
//			String insertQuery = "INSERT INTO working_log (USER_ID,CATEGORY,SUB_CATEGORY,MESSAGE) VALUES (" + 
//					"'" + userId + "'," + 
//					"'" + category + "'," + 
//					"'" + subCategory + "'," + 
//					"'" + message + "'" + 
//					")";
//			long key  = dbh.insertJson(insertQuery);		
//		} catch (IOException e) {
//			logger.info("DB 연결을 할수 없습니다.logger.save 실패 -->  " + "원본메세지:" + message);
//			e.printStackTrace();
//		}
//		
//		logger.info(msg);
	}
	
	public void info(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zettasoft" + "info," + formattedDate + "," + className + "," + message;
		logger.info(msg);
	}

	public void error(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zettasoft," + "error," + formattedDate + "," + className + "," + message;
		logger.info(msg);
	}

	public void process(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zettasoft," + "process," + formattedDate + "," + className + "," + message;
		logger.info(msg);
	}
	
	public void debug(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zettasoft," + "debug," + formattedDate + "," + className + "," + message;
		logger.debug(msg);
	}
}
