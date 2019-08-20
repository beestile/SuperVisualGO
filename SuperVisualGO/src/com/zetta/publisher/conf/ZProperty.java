package com.zetta.publisher.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.zetta.zScheduler.utils.zLogger;

@Component
public class ZProperty {

	public static final String RESOURCE_NAME = "config";
	static JSONObject configObject = null;
	static String realPath = null;
	public zLogger logger = new zLogger(getClass());
	
	public ZProperty(){
		
		String xmlStr=null;
		String configPath = this.getClass( ).getResource( "/" ).getPath();	
		try {
			configPath = URLDecoder.decode(configPath,"UTF-8");
			this.setRealPath(configPath);
		} catch (UnsupportedEncodingException e1) {
		
		}
		
		try {
			String result = "";
			String fileName = realPath + RESOURCE_NAME + ".json";
			logger.info("환경파일을 가져옵니다.. 경로:" + fileName);
			File file = new File(fileName);
			if (file.exists()) {
				BufferedReader br = null;
				InputStreamReader isr = null;
				FileInputStream fis = null;
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis, "UTF-8");
				br = new BufferedReader(isr);
				String temp = "";
				while ((temp = br.readLine()) != null) {
					result += temp;
				}

				isr.close();
			} else {
				logger.info("환경파일을 가져올수 없습니다. 경로:" + fileName);
				return;
			}
			
			configObject = (JSONObject)new JSONParser().parse(result);

		} catch (Exception e) {
			logger.error("환경파일을 파싱할수 없습니다.");
			logger.error("에러 :" + e.getMessage());
		}

	}

	public String getProperties(String propertyName) {
		return (String)configObject.get(propertyName);
	}

	public static String getRealPath() {
		return realPath;
	}

	public static void setRealPath(String value) {
		realPath = value;
	}
}
