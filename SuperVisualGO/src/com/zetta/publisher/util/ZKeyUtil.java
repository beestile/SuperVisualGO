package com.zetta.publisher.util;

import java.util.UUID;

public class ZKeyUtil {

	
	public static String uniqueKeyGen(){
		
		return UUID.randomUUID().toString();
		
	}
	
	public static String uniqueKeyGenWithDate(){
		
		String result = ZTimeUtil.getCurrentDateTime("yyyyMMdd") +  "-" + UUID.randomUUID().toString();
		return result;
		
	}
	
	public static String getNextCodeValue(String str){
		
		String temp[] = str.split("_");
		
		String code = temp[0];
		String index = temp[1];
		
		int indexI = Integer.parseInt(index) + 1;
		
		return code + "_" + String.valueOf(indexI);
	}
	

}
