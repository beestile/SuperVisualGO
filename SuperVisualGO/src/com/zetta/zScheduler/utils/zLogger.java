package com.zetta.zScheduler.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class zLogger {

	public Logger logger = Logger.getLogger(getClass());
	String className = "";

	public zLogger(Class<?> logClass) {
		this.className = logClass.getName();
	}

	public void info(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zInfo," + formattedDate + "," + className + "," + message;
		logger.info(msg);
	}

	public void error(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zError," + formattedDate + "," + className + "," + message;
		logger.info(msg);
	}

	public void process(String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateFormat.format(date);
		String msg = "zProcess," + formattedDate + "," + className + "," + message;
		logger.info(msg);
	}
}
