package com.zetta.zScheduler.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zetta.zScheduler.model.SystemInfo;
import com.zetta.zScheduler.utils.CommonConfig;
import com.zetta.zScheduler.utils.DBHandler;
import com.zetta.zScheduler.utils.DataUtils;
import com.zetta.zScheduler.utils.zLogger;

@Controller
public class DataController {
	public zLogger logger = new zLogger(getClass());

	public CommonConfig config = new CommonConfig();
	public DataUtils dataUtils = new DataUtils(config.getProperties("ZWORKINGROOT"), config.getProperties("QVXROOT"));
	
	
	@RequestMapping(value = "/data/generateSampleData.do", method = RequestMethod.GET)
	@ResponseBody
	public void generateSampleData(
			@RequestParam(value = "targetTable", required = true) String targetTable,
			@RequestParam(value = "schema", required = true) String schema,
			@RequestParam(value = "genCnt", required = true) int genCnt,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
	
		logger.info("/data/generateSampleData.do start"); 
	
		DBHandler dbh = new DBHandler(schema, null);
		boolean success  = dbh.generateSampleData(targetTable, genCnt);
		
		try {
	        response.getWriter().print(success);
	    } catch (IOException e) {
	        e.printStackTrace();
	        response.getWriter().print(false);
	    }
	}
	
	
	@RequestMapping(value = "/data/getSystemInfo.do", method = RequestMethod.GET)
	@ResponseBody
	public void getSystemInfo(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		logger.info("/data/getSystemInfo.do start");
		SystemInfo result = dataUtils.getSystemInfo();
		Gson gson = new GsonBuilder().create();
		String resultStr = gson.toJson(result);
		try {
	        response.getWriter().print(resultStr);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }  
	}
	
	
	@RequestMapping(value = "/data/saveSystemInfo.do", method = RequestMethod.GET)
	@ResponseBody
	public void saveSystemInfo(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		logger.info("/data/saveSystemInfo.do start");
		SystemInfo systemInfo = dataUtils.getSystemInfo();
		
		DBHandler dbh = new DBHandler("ls", null);
		boolean success  = dbh.getSystemInfoSave(systemInfo);
		try {
	        response.getWriter().print(true);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }  
	}
}