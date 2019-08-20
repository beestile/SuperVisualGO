package com.zetta.publisher.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ZTimeUtil {

	public static String getBasicCurrentDateTime(){
		
		Date date = new Date();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 
		
		return format.format(date);
	}
	
	public static String getCurrentDateTime(String sFormat){
		
		Date date = new Date();
		
		SimpleDateFormat format = new SimpleDateFormat(sFormat); // 
		
		return format.format(date);		
	}
	
	public static Calendar getCalendar(String str){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm"); //
		
		Date date = null;
		
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		return cal;
	}
	
	public static Date getDate(String str){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm"); // 
		
		Date date = null;
		
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static Long getMillisTime(){
		return System.currentTimeMillis();
	}
	
	
	// 하드코딩... UI쪽을 수정하는게 차라에 나을 수도...
	public static String concatDate(String date, String time){
		
		String[] dateArr = date.split("/");
		
		String[] timeArr = time.split(":");
		
		String result = dateArr[2] + dateArr[1] + dateArr[0] + timeArr[0] + timeArr[1];
		
		return result;
	}
}
