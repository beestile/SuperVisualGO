package com.zetta.zScheduler.utils;

import static org.quartz.JobKey.jobKey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zetta.dataSource.model.dataSource.DataSet;
import com.zetta.dataSource.model.dataSource.Fields;
import com.zetta.dataSource.model.dataSource.Filter;
import com.zetta.dataSource.model.dataSource.zAPIList;
import com.zetta.dataSource.model.dataSource.zDB;
import com.zetta.dataSource.model.dataSource.zDBList;
import com.zetta.dataSource.model.dataSource.zDashboard;
import com.zetta.dataSource.model.dataSource.zDashboardPanel;
import com.zetta.dataSource.model.dataSource.zODBCList;
import com.zetta.dataSource.model.diagram.zDMain;
import com.zetta.dataSource.model.diagram.zDataSource;
import com.zetta.dataSource.model.diagram.zLinkDataArray;
import com.zetta.dataSource.model.diagram.zNodeDataArray;
import com.zetta.dataSource.model.spreadJS.Definition;
import com.zetta.dataSource.model.spreadJS.MapInfo;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.schedule.ZScheduler;
import com.zetta.publisher.util.ZFileUtil;
import com.zetta.publisher.util.ZKeyUtil;
import com.zetta.publisher.util.ZTimeUtil;
import com.zetta.scheduler.trigger.impl.TriggerDaily;
import com.zetta.scheduler.trigger.impl.TriggerInterval;
import com.zetta.scheduler.trigger.impl.TriggerWeekly;
import com.zetta.zScheduler.model.DiskInfo;
import com.zetta.zScheduler.model.SystemInfo;

public class DataUtils {

	public zLogger logger = new zLogger(getClass());

	public int dMaxSize = 60000;
	
	private String zWoringRoot = null;	
	private String zQvxRoot = null;	

	public DataUtils(String zWoringRoot, String zQvxRoot) {
		this.zWoringRoot = zWoringRoot;
		this.zQvxRoot = zQvxRoot;
	}
	
	public List<TargetList> getTargetLists() {

		logger.info("function getTargetLists");

		List<String> lists = new ZFileUtil().dirFilesToStringList();

		String jsonStr = "[";
		for (String json : lists) {
			jsonStr += json + ",";
		}

		jsonStr = jsonStr.substring(0, jsonStr.length() - 1) + "]";

		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<TargetList>>() {
		}.getType();
		List<TargetList> result = gson.fromJson(jsonStr, listType);

		return result;
	}

	public boolean initialLoad() {
		logger.info("initialLoad");
		List<String> list = new ZFileUtil().dirFilesToStringList();

		Gson gson = new Gson();

		try {
			for (String json : list) {

				TargetList targetList = gson.fromJson(json, TargetList.class);
				this.start(targetList);
			}
		} catch (JsonSyntaxException e) {
			logger.info("error: " + e.getMessage());
			return false;
		}
		
		return true;
	}

	public boolean start(TargetList targetList) {
		logger.info("start!!!!");
		if(targetList.getStatus() == null) {
			logger.info("targetList에 status가 없습니다.");
			return false;
		}
		
		
		logger.info("targetList.getStatus() " + targetList.getStatus());
		if (targetList.getStatus().equals("run")) {
			switch (targetList.getCycleCode()) {

			case "interval":
				logger.info("case interval - " + " 실행 : " + targetList.getGroupId());
				TriggerInterval interval = new TriggerInterval();

				interval.fire(targetList);

				break;

			case "daily":
				logger.info("case daily - " + " 실행 : " + targetList.getGroupId());
				TriggerDaily daily = new TriggerDaily();

				daily.fire(targetList);

				break;

			case "weekly":
				logger.info("case weekly - " + " 실행: " + targetList.getGroupId());
				TriggerWeekly weekly = new TriggerWeekly();

				weekly.fire(targetList);

				break;

			case "monthly":
				logger.info("case monthly - 개발중." + targetList.getGroupId());
				break;

			default:
				break;
			}
		}
		
		return true;
	}

	public TargetList selectTargetList(String uniqueId) {
		String json = new ZFileUtil().fileToString(uniqueId + ".json");

		Gson gson = new Gson();
		TargetList targetList = gson.fromJson(json, TargetList.class);
		return targetList;
	}

	public boolean stop(TargetList targetList) {
		Scheduler scheduler = ZScheduler.getInstance().getScheduler();
		
		boolean results = false;
		
		try {
			scheduler.unscheduleJob(new TriggerKey(targetList.getTriggerId(), targetList.getGroupId()));
			results = scheduler.deleteJob(jobKey(targetList.getJobId(), targetList.getGroupId())); // scheduler.unscheduleJob 과의 차이점 파악.
			
		} catch (SchedulerException e) {
			
			e.printStackTrace();
		}
		
		return results;
	}

	public void deleteScheduleInfoFile(String uniqueId) {
		logger.info("deleteScheduleInfoFile " + uniqueId + ".json");
		new ZFileUtil().deletefile(uniqueId + ".json");
	}

	public void insertScheduleGroup(TargetList targetList) {
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		targetList.setUniqueId(ZKeyUtil.uniqueKeyGenWithDate());
		targetList.setCreatedAt(ZTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		List<TargetList> list = this.getTargetLists();
		
		String triggerId = this.maxTriggerId(list);
		targetList.setTriggerId((triggerId == null)?"TRG_1":ZKeyUtil.getNextCodeValue(triggerId));
		
		String groupId = this.maxGroupId(list);
		targetList.setGroupId((groupId == null)?"GRP_1":ZKeyUtil.getNextCodeValue(groupId));
		
		String jobId = this.maxJobId(list);
		targetList.setJobId((jobId == null)?"JOB_1":ZKeyUtil.getNextCodeValue(jobId));

	
		if(!targetList.getCycleCode().equals("interval")){			
			String cycleTime = ZTimeUtil.concatDate(targetList.getDate(), targetList.getCycleTime());			
			targetList.setCycleTime(cycleTime);
		}
		
		targetList.setStatus("stop");
		targetList.setUseYN("yes");
		
		this.createScheduleInfoFile(targetList);			
	}

	private String maxJobId(List<TargetList> list) {
		if(list == null) return null;
		int maxId = 0;
		for (TargetList targetList : list) {
			String id = targetList.getJobId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "JOB_" + maxId;
	}

	private String maxGroupId(List<TargetList> list) {
		if(list == null) return null;
		int maxId = 0;
		for (TargetList targetList : list) {
			String id = targetList.getGroupId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "GRP_" + maxId;
	}

	private String maxTriggerId(List<TargetList> list) {
		if(list == null) return null;
		int maxId = 0;
		for (TargetList targetList : list) {
			String id = targetList.getTriggerId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "TRG_" + maxId;
	}

	public void createScheduleInfoFile(TargetList targetList) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String result = gson.toJson(targetList);
		new ZFileUtil().stringToFile(targetList.getUniqueId() + ".json", result);	
		
	}

	public TargetList updateGroup(TargetList targetList) {		
		TargetList sourceList = this.selectTargetList(targetList.getUniqueId());
		//{"uniqueId":"20160311-012af002-8e87-41e3-bf74-4a3e74def861","scheduleName":"모니터링보고서","cycleCode":"interval","cycleTime":"10s","date":"3/11/16 20:23"}
		
		targetList.setCreatedAt(sourceList.getCreatedAt());
		targetList.setGroupId(sourceList.getGroupId());
		targetList.setJobId(sourceList.getJobId());
		targetList.setStatus(sourceList.getStatus());
		targetList.setTargets(sourceList.getTargets());
		targetList.setTriggerId(sourceList.getTriggerId());
		targetList.setUseYN(sourceList.getUseYN());
		return targetList;
	}

	public void insertScheduleTarget(ScheduleTarget target) {
		logger.info("insertScheduleTarget ");
		//step 생성코드 추가
		TargetList targetList = this.selectTargetList(target.getTargetListId());
		String stepId = this.maxStepId(targetList);
		target.setStepId((stepId == null)?"STP_1":ZKeyUtil.getNextCodeValue(stepId));
		
		if(target.getpType().equals("command")){
			target.setMethod("");
		}else if(target.getpType().equals("http")){
						
		}
		
		targetList.addTargets(target);
		
		// 기존파일 삭제
		this.deleteScheduleInfoFile(targetList.getUniqueId());
		// 새로운 파일 생성
		this.createScheduleInfoFile(targetList);
		
	}

	private String maxStepId(TargetList targetList) {
		int maxId = 0;
		if(targetList == null) return null;
		if(targetList.getTargets() == null) return null;
		
		for (ScheduleTarget scheduleTarget : targetList.getTargets()) {
			String id = scheduleTarget.getStepId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "STP_" + maxId;
	}

	public List<ScheduleTarget> deleteStep(List<ScheduleTarget> targets, String stepId) {
		
		for (ScheduleTarget scheduleTarget : targets) {
			String id = scheduleTarget.getStepId();
			if(id.equals(stepId)){
				targets.remove(scheduleTarget);
				break;
			}
		}
		
		return targets;
	}

	public TargetList updateScheduleTargetInfo(TargetList targetList, ScheduleTarget scheduleTarget) {
		logger.info("updateScheduleTargetInfo");
		List<ScheduleTarget> scheduleTargets = targetList.getTargets();
		for(ScheduleTarget source : scheduleTargets){
			if(source.getStepId().equals(scheduleTarget.getStepId())){
				
				scheduleTarget.setGroupId(source.getGroupId());
				scheduleTarget.setJobId(source.getJobId());
				scheduleTarget.setStepId(source.getStepId());
				scheduleTarget.setTargetListId(source.getTargetListId());
				scheduleTarget.setTargetListName(source.getTargetListName());
								
				logger.info("scheduleTarget getMethod " + scheduleTarget.getMethod());
				logger.info("scheduleTarget getParameter " + scheduleTarget.getParameter());
				logger.info("scheduleTarget getProgPath " + scheduleTarget.getProgPath());
				logger.info("scheduleTarget getpType " + scheduleTarget.getpType());
				
				scheduleTargets  = this.deleteStep(scheduleTargets, source.getStepId());
				scheduleTargets.add(scheduleTarget);
				targetList.setTargets(scheduleTargets);
				return targetList;
			}
		}
		return targetList;
	}

	public SystemInfo getSystemInfo() {
		return new SystemUtils().getSystemInfo();
	}


	public zODBCList getODBCInfo() throws IOException {
		logger.info("function getODBCInfo");
		zODBCList list = new zODBCList();

		String result = "";
		File file = new File(zWoringRoot + "odbc.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zODBCList.class);

		return list;
	}
	
	public zDBList getDBInfo() throws IOException {
		logger.info("function getDBInfo");
		zDBList list = new zDBList();

		String result = "";
		File file = new File(zWoringRoot + "db.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zDBList.class);

		return list;
	}

	// QlikView스크립트를 만든다.
	public String makeScriptDBLoad(String fileName, String objectKey, String connStr, String subName, String schema, zDMain dataObject) {
		logger.info("function makeScriptDBLoad");
		String result = "";
		result = connStr;
		dataObject.getLinkDataArray().get(0).getFrom();
		String selectHeader = "";
		for(int i = 0 ; i < dataObject.getLinkDataArray().size(); i++){
			selectHeader = selectHeader + dataObject.getLinkDataArray().get(i).getFromPort() + ",";
		}
		selectHeader = selectHeader.substring(0, selectHeader.length() - 1);
		
		String loadTable = "\n" + "\n" + "tableInfo:\n" + "load *;\n" + "sql select " + selectHeader + " from " + subName + schema + dataObject.getLinkDataArray().get(0).getFrom() + ";\n";

		result += loadTable;

		return result;
	}
	
	//Jdbc Query
	public String makeScriptDBLoad(zDMain main) {
		logger.info("function makeScriptDBLoad");
		String result = "";
		String selectHeader = "";
		for(int i = 0 ; i < main.getLinkDataArray().size(); i++){
			selectHeader = selectHeader + main.getLinkDataArray().get(i).getFromPort() + ",";
		}
		selectHeader = selectHeader.substring(0, selectHeader.length() - 1);
		
		String loadTable = "select " + selectHeader + " from " +  main.getLinkDataArray().get(0).getFrom() + ";\n";

		result += loadTable;

		return result;
	}
	
	//스타스키마구조에서 fact를 로딩하는 스크립트를 구성한다.
	public String makeScriptFactDBLoad(zDMain main) {
		logger.info("function makeScriptFactDBLoad");
		String result = "";
		String selectHeader = "";
		String factTableName = main.getLinkDataArray().get(0).getFrom();
		for(int i = 0 ; i < main.getNodeDataArray().size(); i++){
			if(main.getNodeDataArray().get(i).getKey().equals(factTableName)){
				List<Fields> fields  = main.getNodeDataArray().get(i).getFields();
				for(int f = 0 ; f < fields.size(); f++){
					selectHeader = selectHeader + fields.get(f).getName() + ",";
				}
			}
		}
		selectHeader = selectHeader.substring(0, selectHeader.length() - 1);
		
		String loadTable = "select " + selectHeader + " from " +  factTableName + ";\n";

		result += loadTable;
		return result;
	}
	
	public String makeScriptDBLoad(String fileName, String objectKey, String connStr, String subName, String script) {
		String result = "";
		result = connStr;
		String selectHeader = "";
		
		String loadTable = "\n" + "\n" + "tableInfo:\n" + "load *;\n" + script+ "\n";

		result += loadTable;

		return result;
	}

	public boolean makeQvx(String fileName, String objectKey, String script) {
		// 1. 스크립트 파일 저장
		String folderName = zWoringRoot + fileName;
		String path = zWoringRoot + fileName + "\\" + objectKey + ".script";
		if (saveTextFileMS949(folderName, path, script)) {
			// 2. qv.exe를 실행해야한다.
			// "C:\Program Files\QlikView\Qv.exe" /r
			// "D:\QVD\QVDLOAD\초기적재\초기적재7_QVD.QVW" /vvPath="간단한 보고서\-13"
			try {
				String cmd = "\"C:\\Program Files\\QlikView\\Qv.exe\" /r /NoSecurity \""+zWoringRoot+"RuleEngine.qvw\" /vvPath=\"" + fileName.replace(" ", "%20") + "_" + objectKey + "\"";
				logger.info("cmd:" + cmd);

				Process p = Runtime.getRuntime().exec(cmd);
				int exitVal = p.waitFor();

			} catch (InterruptedException ex) {
				logger.info("error: " + ex);
			} catch (IOException ex) {
				logger.info("error: " + ex);
			}

		} else {
			logger.info("스크립트 파일을 저장하지 못했습니다. 경로:" + path);
			return false;
		}

		return true;
	}
	
	public boolean runQvx(String fileName, String objectKey) {
		logger.info("runQvx " + fileName + ":" + objectKey );
		try {
			String cmd = "\"C:\\Program Files\\QlikView\\Qv.exe\" /r  /NoSecurity  \""+zWoringRoot+"RuleEngine.qvw\" /vvPath=\"" + fileName.replace(" ", "%20") + "_" + objectKey + "\"";
			logger.info("cmd:" + cmd);

			Process p = Runtime.getRuntime().exec(cmd);
			int exitVal = p.waitFor();

		} catch (InterruptedException ex) {
			logger.info("error: " + ex);
		} catch (IOException ex) {
			logger.info("error: " + ex);
		}

		return true;
	}

	public boolean saveTextFile(String folderName, String path, String content) {
		logger.info("saveTextFile::" + folderName + path);
		File file = new File(folderName);
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		try {

			File targetFile = new File(path);
			targetFile.createNewFile();

			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile.getPath()), "UTF-8"));

			output.write(content);
			output.close();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean saveTextFileMS949(String folderName, String path, String content) {
		logger.info("saveTextFile::" + folderName + path);
		File file = new File(folderName);
		if (!file.exists()) {
			if (file.mkdir()) {
				logger.info("Directory is created!");
			} else {
				logger.info("Failed to create directory!");
			}
		}

		try {

			File targetFile = new File(path);
			targetFile.createNewFile();

			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile.getPath()), "MS949"));

			output.write(content);
			output.close();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		return true;
	}

	// 첫번째 행을 읽어서 보낸다.
	public List getColumnInfo(Workbook workbook, int sheetId) throws IOException {

		logger.info("function getColumnInfo start");

		Sheet firstSheet = workbook.getSheetAt(sheetId);
		Iterator<Row> iterator = firstSheet.iterator();

		// List list = new ArrayList();
		int count = 0;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			List row = new ArrayList();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					row.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					row.add(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					row.add(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					row.add(cell.getCachedFormulaResultType());
					break;
				}
			}
			count++;
			if (count > 0)
				return row;
			// list.add(row);
		}
		return null;
		// return list;
	}

	@SuppressWarnings("unchecked")
	public List sampleFromExcel(String xlsPath, String sheetName) throws IOException, InvalidFormatException {
		logger.info("function sampleFromExcel start");

		File myFile = new File(xlsPath);
		Workbook workbook = null;

		FileInputStream inputStream = null;

		if (xlsPath.substring(xlsPath.lastIndexOf(".") + 1).toLowerCase().equals("xls")) {
			workbook = WorkbookFactory.create(myFile);
		} else if (xlsPath.substring(xlsPath.lastIndexOf(".") + 1).toLowerCase().equals("xlsx")) {
			inputStream = new FileInputStream(myFile);
			workbook = new XSSFWorkbook(inputStream);
		} else {
			return null;
		}

		Sheet firstSheet = workbook.getSheet(sheetName);
		Iterator<Row> iterator = firstSheet.iterator();

		List list = new ArrayList();
		int count = 0;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			List row = new ArrayList();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					row.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					row.add(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					row.add(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					row.add(cell.getCachedFormulaResultType());
					break;
				}
			}
			count++;
			if (count > 21)
				break;
			list.add(row);
		}

		if (inputStream != null)
			inputStream.close();
		// inputStream.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List getFromExcel(String xlsPath, String sheetName) throws IOException, InvalidFormatException {
		logger.info("function getFromExcel start");

		File myFile = new File(xlsPath);
		Workbook workbook = null;

		FileInputStream inputStream = null;

		if (xlsPath.substring(xlsPath.lastIndexOf(".") + 1).toLowerCase().equals("xls")) {
			workbook = WorkbookFactory.create(myFile);
		} else if (xlsPath.substring(xlsPath.lastIndexOf(".") + 1).toLowerCase().equals("xlsx")) {
			inputStream = new FileInputStream(myFile);
			workbook = new XSSFWorkbook(inputStream);
		} else {
			return null;
		}

		Sheet firstSheet = workbook.getSheet(sheetName);
		Iterator<Row> iterator = firstSheet.iterator();

		List list = new ArrayList();
		int count = 0;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			List row = new ArrayList();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					row.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					row.add(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					row.add(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					row.add(cell.getCachedFormulaResultType());
					break;
				}
			}
			count++;
			if (list.size() >= dMaxSize)
				break;
			list.add(row);
		}
		if (inputStream != null)
			inputStream.close();
		// inputStream.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List sampleFromExcel(String xlsPath, int sheetId) throws IOException {
		logger.info("function sampleFromExcel start");

		FileInputStream inputStream = new FileInputStream(new File(xlsPath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(sheetId);
		Iterator<Row> iterator = firstSheet.iterator();

		List list = new ArrayList();
		int count = 0;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			List row = new ArrayList();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					row.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					row.add(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					row.add(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					row.add(cell.getCachedFormulaResultType());
					break;
				}
			}
			count++;
			if (count > 21)
				break;
			list.add(row);
		}

		inputStream.close();
		return list;
	}

	public String getJsonFile(String folder, String fileName, String fileType) throws FileNotFoundException, UnsupportedEncodingException, IOException {

		logger.info("function getJsonFile start");
		logger.info(zWoringRoot + folder + "\\" + fileName + "." + fileType);
		String result = "";
		
		File file = new File(zWoringRoot + folder + "\\" + fileName + "." + fileType);
		if (file.exists()) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			if(fileType.equals("script")){
				isr = new InputStreamReader(fis, "MS949");
			}else{
				isr = new InputStreamReader(fis, "UTF-8");
			}
			br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				result += temp + "\n";
			}
			// result = result.replace("\"class\"", "\"classes\"");

			isr.close();
		} else {
			result = "[\"notFound\"]";
			logger.info(result);
		}
		return result;
	}
	
	public String getJsonFileTop10(String folder, String fileName, String fileType) throws FileNotFoundException, UnsupportedEncodingException, IOException {

		logger.info("function getJsonFileTop10 start");
		logger.info(zWoringRoot + folder + "\\" + fileName + "." + fileType);
		String result = "";
		
		File file = new File(zWoringRoot + folder + "\\" + fileName + "." + fileType);
		if (file.exists()) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			if(fileType.equals("script")){
				isr = new InputStreamReader(fis, "MS949");
			}else{
				isr = new InputStreamReader(fis, "UTF-8");
			}
			br = new BufferedReader(isr);
			String temp = "";
			int count = 10;
			while ((temp = br.readLine()) != null) {
				result += temp + "\n";
				if(count-- < 0) break;
			}
			// result = result.replace("\"class\"", "\"classes\"");

			isr.close();
		} else {
			result = "[\"notFound\"]";
			logger.info(result);
		}
		return result;
	}


	public String getJsonFile(String filePath) throws FileNotFoundException, UnsupportedEncodingException, IOException {

		logger.info("function getJsonFile start");
		logger.info(filePath);
		String result = "";
		File file = new File(filePath);
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
			result = "[\"notFound\"]";
			logger.info(result);
		}
		return result;
	}

	public String getDataObject(zDMain dMain, String objectKey) {
		logger.info("function getDataObject start");

		List<zLinkDataArray> List = dMain.getLinkDataArray();
		for (zLinkDataArray row : List) {
			if (row.getTo().equals(objectKey))
				return row.getFrom();
		}
		return null;
	}

	public static List sortByValue(final Map map) {
		List<String> list = new ArrayList();
		list.addAll(map.keySet());

		Collections.sort(list, new Comparator() {

			public int compare(Object o1, Object o2) {
				Object v1 = map.get(o1);
				Object v2 = map.get(o2);

				return ((Comparable) v1).compareTo(v2);
			}

		});
		Collections.reverse(list); // 주석시 오름차순
		return list;
	}

	// 서버의 모든 visualization 개체 파일을 가져온다.
	public String getAllVFiles() {
		logger.info("function getAllVFiles start");
		String result = "";
		List<zDMain> results = new ArrayList<zDMain>();

		List<String> subNames = getDirectories(zWoringRoot+"process");
		for (String dir : subNames) {
			String mainJson = "";
			try {
				mainJson = getJsonFile(dir.replace(zWoringRoot, ""), "main", "json");
			} catch (IOException e2) {
				mainJson = "{\"notFound\"},";
			}
			zDMain dMain = new zDMain();
			try {

				Gson gson = new GsonBuilder().create();
				// 보고서 main.json을 가져온다.
				logger.info("parsing zDMain.class");
				dMain = gson.fromJson(mainJson, zDMain.class);
				for (zNodeDataArray arr : dMain.getNodeDataArray()) {
					if (arr.getCategory().equals("widget") || arr.getCategory().equals("object")) {
						String json = getJsonFile(dir.replace(zWoringRoot, ""), arr.getKey(), "json");
						zDMain subV = new zDMain();

						subV = gson.fromJson(json, zDMain.class);
						subV.setFileName(dir.replace(zWoringRoot +"process\\", "") + "_" + arr.getKey());
						results.add(subV);
					}
				}
				result = gson.toJson(results);
			} catch (Exception e) {
				//result = "[\"notFound\"]";
				logger.info("getAllVFiles " +  " error" + e.getMessage());
			}
		}
		return result;
	}

	// 서버의 모든 ShareFile 파일을 가져온다.
	public String getAllQVXInfo() {
		logger.info("function getAllQVXInfo start");
		List results = new ArrayList();
		String result = "";

		List<String> subNames = getDirectories(zQvxRoot);
		for (String dirName : subNames) {
			logger.info("왜 깨지는거지??? dirName result ::: " + dirName);
			File dir = new File(dirName);
			File[] fileList = dir.listFiles();
		
			for (File file : fileList) {
				if (file.getPath().toLowerCase().indexOf(".qvx") > 0) {
					results.add(file.getPath().replace(zQvxRoot, ""));
					logger.info("file.getPath().replace result ::: " + file.getPath().replace(zQvxRoot, ""));
				}
			}
		}

		Gson gson = new GsonBuilder().create();
		result = gson.toJson(results);
		logger.info("/modelManager/getAllQVXInfo.do result ::: " + result);
		return result;
	}
	
	

	// 특정경로의 모든 디렉토리를 얻어온다.
	public List<String> getDirectories(String directoryName) {
		List<String> directoryNames = new ArrayList<String>();

		File dir = new File(directoryName);
		File[] fileList = dir.listFiles();

		for (File file : fileList) {
			if (file.isDirectory()) {
				directoryNames.add(file.getPath());
			}
		}
		return directoryNames;
	}

	public String makeScriptDBLoadWithFilter(List<Filter> filterList, String fileName, String key, String connStr, String subName, String schema, zDMain dataObject) {
		String result = "";
		result = connStr;
		dataObject.getLinkDataArray().get(0).getFrom();
		String selectHeader = "";
		for(int i = 0 ; i < dataObject.getLinkDataArray().size(); i++){
			selectHeader = selectHeader + dataObject.getLinkDataArray().get(i).getFromPort() + ",";
		}
		selectHeader = selectHeader.substring(0, selectHeader.length() - 1);
		
		String loadTable = "\n" + "\n" + "tableInfo:\n" + "load *;\n" + "sql select " + selectHeader + " from " + subName + schema + dataObject.getLinkDataArray().get(0).getFrom() + ";\n";

		result += loadTable;

		return result;
	}

	public boolean makeQvxBulk(String fileName, String objectKey, String script) {
		// 1. 스크립트 파일 저장
		String folderName = zWoringRoot + fileName;
		String path = zWoringRoot + fileName + "\\" + objectKey + ".script";
		if (saveTextFileMS949(folderName, path, script)) {
			// 2. qv.exe를 실행해야한다.
			// "C:\Program Files\QlikView\Qv.exe" /r
			// "D:\QVD\QVDLOAD\초기적재\초기적재7_QVD.QVW" /vvPath="간단한 보고서\-13"
			try {
				String cmd = "\"C:\\Program Files\\QlikView\\Qv.exe\" /r /NoSecurity \""+zWoringRoot+"RuleEngineBulk.qvw\" /vvPath=\"" + fileName.replace(" ", "%20") + "_" + objectKey + "\"";
				logger.info("cmd:" + cmd);

				Process p = Runtime.getRuntime().exec(cmd);
				int exitVal = p.waitFor();

			} catch (InterruptedException ex) {
				logger.info("error: " + ex);
			} catch (IOException ex) {
				logger.info("error: " + ex);
			}

		} else {
			logger.info("스크립트 파일을 저장하지 못했습니다. 경로:" + path);
			return false;
		}

		return true;
	}
	
	public boolean makeQvxBulk(String fileName, String script) {
		// 1. 스크립트 파일 저장
		String folderName = zWoringRoot + fileName;
//		String path = zWoringRoot + fileName + "\\" + objectKey + ".script";
//		if (saveTextFileMS949(folderName, path, script)) {
//			// 2. qv.exe를 실행해야한다.
//			// "C:\Program Files\QlikView\Qv.exe" /r
//			// "D:\QVD\QVDLOAD\초기적재\초기적재7_QVD.QVW" /vvPath="간단한 보고서\-13"
//			try {
//				String cmd = "\"C:\\Program Files\\QlikView\\Qv.exe\" /r /NoSecurity \""+zWoringRoot+"RuleEngineBulk.qvw\" /vvPath=\"" + fileName.replace(" ", "%20") + "_" + objectKey + "\"";
//				logger.info("cmd:" + cmd);
//
//				Process p = Runtime.getRuntime().exec(cmd);
//				int exitVal = p.waitFor();
//
//			} catch (InterruptedException ex) {
//				logger.info("error: " + ex);
//			} catch (IOException ex) {
//				logger.info("error: " + ex);
//			}
//
//		} else {
//			logger.info("스크립트 파일을 저장하지 못했습니다. 경로:" + path);
//			return false;
//		}

		return true;
	}

	public String getUserCodes() {
		logger.info("function getUserCodes start");
		String result = "";
		List<zDMain> results = new ArrayList<zDMain>();
		String folerName = "process\\_code";
		
		try {

			result = getJsonFile(folerName, "main", "json");
			zDMain dMain = new zDMain();
			
			Gson gson = new GsonBuilder().create();
			// 보고서 main.json을 가져온다.
			logger.info("parsing zDMain.class");
			dMain = gson.fromJson(result, zDMain.class);
			for (zNodeDataArray arr : dMain.getNodeDataArray()) {
				if (arr.getChartType().equals("dataobject")) {
					String json = getJsonFile(folerName, arr.getKey(), "json");
					zDMain subV = new zDMain();

					subV = gson.fromJson(json, zDMain.class);
					subV.setFileName(arr.getKey());
					results.add(subV);
				}
			}
			result = gson.toJson(results);
		} catch (Exception e) {
			//result = "[\"notFound\"]";
			logger.info("getUserCodes " +  " error" + e.getMessage());
		}

		return result;
	}
	
	public String getDbSimpleData(String script, zDB db, int sampleSize) {
		//connection을 만든다.
		CommonUtils commonUtils = new CommonUtils();
		commonUtils.getconnection(db);
		return commonUtils.getSimpleJSonRecordWithQuery(script, sampleSize);
	}
	
	public Boolean queryExec(String query, zDB db){		
		CommonUtils commonUtils = new CommonUtils();
		commonUtils.getconnection(db);
		return commonUtils.queryExec(query);
	}

	public String getDbData(String script, zDB db, int maxSize) {
		//connection을 만든다.
		CommonUtils commonUtils = new CommonUtils();
		commonUtils.getconnection(db);
		return commonUtils.getJSonRecordWithQuery(script, maxSize);
	}
	
	public List<String[]> getDbDataArray(String script, zDB db, int maxSize) {
		//connection을 만든다.
		CommonUtils commonUtils = new CommonUtils();
		commonUtils.getconnection(db);
		return commonUtils.getSimpleArrayWithQuery(script, maxSize);
	}

	public String getDBTables(String conName) throws IOException {
		zDBList list = new zDBList();

		String result = "";
		File file = new File(zWoringRoot + "db.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
			return "[\"notFound\"]";
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zDBList.class);

		for(int i = 0 ; i < list.getData().size(); i++){
			
			if(list.getData().get(i).getConName().equals(conName)){
				CommonUtils commonUtils = new CommonUtils();
				String script = list.getData().get(i).getDbListQuery();
				logger.info("script:" + script);
				
				commonUtils.getconnection(list.getData().get(i));
				return commonUtils.getJSonRecordWithQuery(script, -1);
			}
			
		}
		
		return "[\"notFound\"]";
	}
	
	public zDB getDB(String conName) throws IOException {
		zDBList list = new zDBList();

		String result = "";
		File file = new File(zWoringRoot + "db.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
			return null;
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zDBList.class);

		for(int i = 0 ; i < list.getData().size(); i++){
			
			if(list.getData().get(i).getConName().equals(conName)){
				return list.getData().get(i);
			}
			
		}
		
		return null;
	}
	
	public List<String[]> getDBTablesArray(String conName) throws IOException {
		logger.info("function getDBTablesArray");
		zDBList list = new zDBList();

		String result = "";
		File file = new File(zWoringRoot + "db.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
			return null;
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zDBList.class);

		for(int i = 0 ; i < list.getData().size(); i++){
			logger.info("conName :: " + conName);
			if(list.getData().get(i).getConName().equals(conName)){
				CommonUtils commonUtils = new CommonUtils();
				String script = list.getData().get(i).getTableListQuery();
				logger.info("script:" + script);
				
				commonUtils.getconnection(list.getData().get(i));
				return commonUtils.getSimpleArrayWithQuery(script, -1);
			}
			
		}
		
		return null;
	}

	public String getDbTableInfo(String connName) throws IOException {
		zDBList list = new zDBList();

		String result = "";
		File file = new File(zWoringRoot + "db.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
			return "[\"notFound\"]";
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zDBList.class);

		for(int i = 0 ; i < list.getData().size(); i++){
			
			if(list.getData().get(i).getConName().equals(connName)){
				CommonUtils commonUtils = new CommonUtils();
				String script = list.getData().get(i).getTableListQuery();
				logger.info("script:" + script);
				
				commonUtils.getconnection(list.getData().get(i));
				return commonUtils.getJSonRecordWithQuery(script, -1);
			}
		}
		
		return "[\"notFound\"]";
	}
	
	//필터된 데이터는 fdata_ 로 한다.
	public boolean saveProcessJson(String fileName, String objectKey, String content){		
		logger.info("saveProcessJson start" + fileName + ":" + objectKey);
		String folderName = zWoringRoot + fileName ;
		String path = zWoringRoot + fileName + "\\data_" + objectKey +".json";
		
		logger.info("saveProcessJson " + path);
		return new DataUtils(zWoringRoot, zQvxRoot).saveTextFile(folderName, path, content);
	}

	public ArrayList<String[]> getJsonArray(String fileName, String objectKey, String string) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		logger.info("getJsonArray start" + fileName + ":" + objectKey);
		String result = getJsonFile(fileName, objectKey, "json");
		
		Gson gson = new GsonBuilder().create();
		Type listType = new TypeToken<ArrayList<String[]>>() {}.getType();
        
		ArrayList<String[]> objects = null;
		try {
			objects = gson.fromJson(result, listType);
		} catch (JsonSyntaxException e) {
			logger.info("fileName : " + fileName + " 파일파싱애러!");
		}		
		return objects;
	}
	
	//json String 을 개체로 만들어 만환한다.
	public ArrayList<String[]> getJsonArray(String json) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		
		Gson gson = new GsonBuilder().create();
		Type listType = new TypeToken<ArrayList<String[]>>() {}.getType();
        
		ArrayList<String[]> objects = null;
		try {
			objects = gson.fromJson(json, listType);
		} catch (JsonSyntaxException e) {
			logger.info("JSON 파일파싱애러!");
		}		
		return objects;
	}
	
	
	public ArrayList<DataSet> getJsonTable(String fileName, String objectKey, String string) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		logger.info("getJsonTable start" + fileName + ":" + objectKey);
		String result = getJsonFile(fileName, objectKey, "json");
		
		Gson gson = new GsonBuilder().create();        
		ArrayList<DataSet> objects = null;
		try {
			Type listType = new TypeToken<ArrayList<DataSet>>() {}.getType();
			objects = gson.fromJson(result, listType);
		} catch (JsonSyntaxException e) {
			logger.info("fileName : " + fileName + " 파일파싱애러!");
		}		
		return objects;
	}
	
	
	public ArrayList<DataSet> getJsonTableParseContent(String content) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		Gson gson = new GsonBuilder().create();        
		ArrayList<DataSet> objects = null;
		try {
			Type listType = new TypeToken<ArrayList<DataSet>>() {}.getType();
			objects = gson.fromJson(content, listType);
		} catch (JsonSyntaxException e) {
			logger.info("content 파일파싱애러!");
		}		
		return objects;
	}
	
	//code를 얻어온다.
	public List<String[]> getCodeJsonArray(String codeKey) throws IOException {
		List<String[]> result = this.getCSVFile("process\\_CSV", codeKey, "csv");	
		return result;
	}

	private List<String[]> getCSVFile(String folder, String fileName, String fileType) throws IOException {
		logger.info("function getCSVFile start");
		logger.info(zWoringRoot + folder + "\\" + fileName + "." + fileType);
		List<String[]> result = new ArrayList<String[]>();
		
		File file = new File(zWoringRoot + folder + "\\" + fileName + "." + fileType);
		if (file.exists()) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			if(fileType.equals("csv")){
				isr = new InputStreamReader(fis, "MS949");
			}else{
				isr = new InputStreamReader(fis, "UTF-8");
			}
			
			br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				result.add(temp.replace("\"", "").split(","));
			}
			// result = result.replace("\"class\"", "\"classes\"");

			isr.close();
		} else {
			result = null;
			logger.info("파일을 찾을수 없습니다.");
		}
		return result;
	}

	//left조인을 구현한다.
	//2개의 필드로 조인을 할수가 없다.
	public List<String[]> arrayJoin(List<String[]> fact, List<String[]> code, zLinkDataArray zLinkDataArray) {
		String[] factHeader = fact.get(0);
		String[] codeHeader = code.get(0);
		
		
		logger.info("factHeader > " + factHeader);
		logger.info("codeHeader > " + codeHeader);
		
		logger.info("zLinkDataArray.getFromPort() > " + zLinkDataArray.getFromPort());
		logger.info("zLinkDataArray.getToPort() > " + zLinkDataArray.getToPort());
		
		int joinFactIndex = getHeaderIndex(factHeader, zLinkDataArray.getFromPort());
		int joinCodeIndex = getHeaderIndex(codeHeader, zLinkDataArray.getToPort());
		
		
		List<String[]> result = new ArrayList<String[]>();			
		String[] joinHeader = makeJoinHeader(factHeader, codeHeader, zLinkDataArray);
		
		result.add(joinHeader);
		for(int i = 1 ; i < fact.size(); i++){
			String[] row = makeJoinData(fact.get(i), code, joinFactIndex, joinCodeIndex);
			result.add(row);
		}
		
		return result;
	}
		
	private String[] makeJoinData(String[] fact, List<String[]> code, int joinFactIndex, int joinCodeIndex) {
		String[] row = new String[fact.length + code.get(0).length -1];
		int col = 0;
		for(int i = 0 ; i < fact.length ; i++){
			row[col] = fact[i];	
			col++;
		}
		
		String[] foundedCode = null;
		for(int i  = 0 ; i < code.size(); i++){
			if(fact[joinFactIndex].equals(code.get(i)[joinCodeIndex])){
				foundedCode = code.get(i);
			}
		}
		
		if(foundedCode != null){
			for(int i = 0 ; i < foundedCode.length ; i++){
				if(joinCodeIndex != i){
					row[col] = foundedCode[i];	
					col++;
				}
			}
		}
		return row;
	}
	
	private String[] makeJoinHeader(String[] factHeader, String[] codeHeader, zLinkDataArray zLinkDataArray) {
		String[] joinHeader = new String[factHeader.length + codeHeader.length -1];
		int col = 0;
		for(int i = 0 ; i < factHeader.length ; i++){
			joinHeader[col] = factHeader[i];	
			col++;
		}
		for(int i = 0 ; i < codeHeader.length ; i++){
			if(zLinkDataArray.getToPort() != null){
				if(!zLinkDataArray.getToPort().equals(codeHeader[i])){
					joinHeader[col] = codeHeader[i];	
					col++;
				}
			}
		}
		return joinHeader;
	}

	private int getHeaderIndex(String[] header, String name){
		for(int i = 0 ; i < header.length ; i++){
			if(header[i].equals(name)) return i;
		}
		return -1;
	}

	public zDB getCurrentDBInfo(String fileName, String parentKey) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		zDataSource dataSource = new zDataSource();
		String result = getJsonFile(fileName, parentKey, "json");
		
		Gson gson = new GsonBuilder().create();
		gson = new GsonBuilder().create();
		dataSource = gson.fromJson(result, zDataSource.class);

		zDBList list = getDBInfo();
		zDB db = null;
		for(int dbIndex = 0 ; dbIndex < list.getData().size(); dbIndex++){
			if(list.getData().get(dbIndex).getConName().equals(dataSource.getConnName())){
				db = list.getData().get(dbIndex);
			}
		}
		if(db == null){
			result = "데이터 소스이름을 확인하세요!!";
			logger.info(result); 
			return null;
		}
		return db;
	}

	public boolean DBInsert(zDB db, CommonUtils commonUtils, List<DataSet> dataSets, String dbName) {

		for(int i = 0 ; i < dataSets.size(); i++){
			DataSet dataSet = dataSets.get(i);
			String query = getTableCreateStr(dataSet, dbName);	
			commonUtils.getconnection(db);
			commonUtils.queryExec("drop table " + dbName + "." + dataSet.getTableName() + ";");
			
			commonUtils.getconnection(db);
			boolean isOk = commonUtils.queryExec(query);
			
//			if(isOk){
			commonUtils.getconnection(db);
			isOk = commonUtils.bulkInsertQueryExec(dataSet.getTableName(), dataSet.getData() , dbName);
//			}else{
//				logger.info("실패한 쿼리::" + query); 
//				return false;
//			}
		}
		
		return true;
	}

	private String getTableCreateStr(DataSet dataSet, String dbName) {
		String result = "create table " + dbName + "." + dataSet.getTableName() + "(";
		String[] header = dataSet.getData().get(0);
		String type = "varchar";
		
		result += "pid int(10) not null auto_increment primary key,";
		for(int i = 0; i < header.length; i++){
			result += header[i] + " " + type + "(" + 100 + "),";
		}
		result = result.substring(0, result.length()- 1);
		result +=");";
		
		return result;
	}

	public List<DataSet> getSourceDataSet(String fileName, String objectKey) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		
		logger.info("function getSourceDataSet");
		String result = "";
		zDMain dMain = new zDMain();
		try {
			result = this.getJsonFile(fileName, "main", "json");
		} catch (IOException e1) {
			result = "데이터 소스이름을 확인하세요!!";
			logger.info(result); 
			return null;
		}
		Gson gson = new GsonBuilder().create();
		dMain = gson.fromJson(result, zDMain.class);
		
		List<zNodeDataArray>  nodeList = dMain.getNodeDataArray();
		List<zLinkDataArray>  linkList = dMain.getLinkDataArray();
		
		List<zNodeDataArray> parents = new ArrayList<zNodeDataArray>();
		for(int i = 0 ; i < linkList.size() ; i++){
			if(linkList.get(i).getTo().equals(objectKey)){
				for(int n = 0 ; n < nodeList.size() ; n++){
					if(nodeList.get(n).getKey().equals(linkList.get(i).getFrom())){
						parents.add(nodeList.get(n));
					}
				}
			}
		}
		//parent가 1개인경우와 여러개인 경우를 나눈다.
		List<DataSet> dataSets = new ArrayList<DataSet>();
		if(parents.size() == 0) return null;
		if(parents.size() == 1){
			logger.info("parents 1개");
			//JDBC인 case와 파일인 case를 나누어본다.
			zDataSource dataSource = this.getSourceType(fileName, parents.get(0).getKey());
			List<String[]> resultObj = null;
			
			//이미 프로세싱이 있는 경우 데이터소스가 존재하지 않는경우가 있다.
			if(dataSource.getSubName() == null || dataSource.getSourceType().equals("QVX")){
				resultObj = this.getJsonArray(fileName, "\\data_" + parents.get(0).getKey(), "json");
				if(resultObj != null){
					DataSet dataSet = new DataSet();
					dataSet.setTableName("결과");
					dataSet.setData(resultObj);
					dataSets.add(dataSet);
				}else{
					dataSets = this.getJsonTable(fileName, "\\data_" + parents.get(0).getKey(), "json");
				}
			}else{
			
				List<String> sourceTableNames = this.getSourceTableName(fileName, objectKey, parents.get(0).getKey());
				logger.info("sourceTableName size --> " + sourceTableNames.size());
				
				for(int tn = 0; tn < sourceTableNames.size(); tn++){
					logger.info("sourceTableNames --> "+  tn + "::"+ sourceTableNames.get(tn));
					
					if(dataSource.getSourceType().equals("JDBC")){				
						logger.info("JDBC");
						resultObj = this.getDbDataWithSource(fileName, objectKey, parents.get(0).getKey(), sourceTableNames.get(tn));
					}else{
						logger.info("FILE");
						List<DataSet> fileDataSets = new ArrayList<DataSet>();
						fileDataSets = this.getJsonTable(fileName, "\\data_" + parents.get(0).getKey(), "json");
						for(int f = 0; f < fileDataSets.size();f++){
							if(fileDataSets.get(f).getTableName().equals(sourceTableNames.get(tn))){
								resultObj = fileDataSets.get(f).getData();
							}
						}
					}
	
					if(resultObj != null){
						DataSet dataSet = new DataSet();
						dataSet.setTableName(sourceTableNames.get(tn));
						dataSet.setData(resultObj);
						dataSets.add(dataSet);
					}else{
						dataSets = this.getJsonTable(fileName, "\\data_" + parents.get(0).getKey(), "json");
					}
				}
			}
		}else{
			logger.info("parents 2개");
			for(int i = 0; i < parents.size(); i++){
				zDataSource dataSource = this.getSourceType(fileName, parents.get(i).getKey());
				List<String[]> resultObj = null;
				
				//이미 프로세싱이 있는 경우 데이터소스가 존재하지 않는경우가 있다.
				if(dataSource.getConnName() == null){
					resultObj = this.getJsonArray(fileName, "\\data_" + parents.get(0).getKey(), "json");
					if(resultObj != null){
						DataSet dataSet = new DataSet();
						dataSet.setTableName("결과");
						dataSet.setData(resultObj);
						dataSets.add(dataSet);
					}else{
						dataSets = this.getJsonTable(fileName, "\\data_" + parents.get(0).getKey(), "json");
					}
				}else{
					List<String> sourceTableNames= this.getSourceTableName(fileName, objectKey, parents.get(i).getKey());
					logger.info("sourceTableName size --> " + sourceTableNames.size());
					
					for(int tn = 0; tn < sourceTableNames.size(); tn++){
						logger.info("sourceTableNames --> "+  tn + "::"+ sourceTableNames.get(tn));
						if(dataSource.getSourceType().equals("JDBC")){
							logger.info("JDBC");
							resultObj = this.getDbDataWithSource(fileName, objectKey, parents.get(i).getKey(), sourceTableNames.get(tn));
						}else{
							logger.info("FILE");
							List<DataSet> fileDataSets = new ArrayList<DataSet>();
							fileDataSets = this.getJsonTable(fileName, "\\data_" + parents.get(i).getKey(), "json");
							for(int f = 0; f < fileDataSets.size();f++){
								if(fileDataSets.get(f).getTableName().equals(sourceTableNames.get(tn))){
									resultObj = fileDataSets.get(f).getData();
								}
							}
						}
						
						if(resultObj != null){
							DataSet dataSet = new DataSet();
							dataSet.setTableName(sourceTableNames.get(tn));
							dataSet.setData(resultObj);
							dataSets.add(dataSet);
						}else{
							dataSets = this.getJsonTable(fileName, "\\data_" + parents.get(i).getKey(), "json");
						}
					}
				}
			}
		}		
		return dataSets;
	}

	private List<String> getSourceTableName(String fileName, String objectKey, String parentKey) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		logger.info("function getSourceTableName");
		
		List<String> resultTableNames = new ArrayList<String>();
		String result = "";
		zDMain dMain = new zDMain();
		result = this.getJsonFile(fileName, objectKey, "json");
		Gson gson = new GsonBuilder().create();
		dMain = gson.fromJson(result, zDMain.class);
		//테이블명을 얻어낸다.
		String tableName = "";
		
		//연결된 개체순서가 중요함.
		for(int i = 0; i < dMain.getNodeDataArray().size(); i++){
			if(dMain.getNodeDataArray().get(i).getCategory().equals("record")){
				tableName = dMain.getNodeDataArray().get(i).getText();
				
				//source에 있는 테이블인지 확인하자.
				logger.info( i + "번째 tableName ::" + tableName);
				List<DataSet> dataSets = this.getJsonTable(fileName, "\\data_" + parentKey, "json");
				if(dataSets == null){
					//DB에서 찾는다.
					logger.info("DB에서 찾는다.");
					logger.info("fileName, parentKey ::" + fileName + "<-->" + parentKey);
					zDataSource dataSource = this.getSourceType(fileName, parentKey);
					
					zDBList list = this.getDBInfo();
					zDB db = null;
					for(int dbIndex = 0 ; dbIndex < list.getData().size(); dbIndex++){
						if(list.getData().get(dbIndex).getConName().equals(dataSource.getConnName())){
							db = list.getData().get(dbIndex);
						}
					}
					if(db == null){
						return null;
					}
					
					List<String[]> tableNames = getDBTablesArray(dataSource.getConnName());
					for(int t = 0; t < tableNames.size(); t++){
						logger.info("tableNames ::" + tableNames.get(t)[1]);
						if(tableName.equals(tableNames.get(t)[1])) {
							resultTableNames.add(tableName);
						};
					}
					
				}else{
					logger.info("dataSets에서 찾는다.");
					for(int d = 0 ; d < dataSets.size(); d++){
						if(tableName.equals(dataSets.get(d).getTableName())) {
							resultTableNames.add(tableName);
						}
					}
				}
			}
		}
		if(resultTableNames.size() == 0) return null;
		
		return resultTableNames;
	}

	private List<String[]> getDbDataWithSource(String fileName, String objectKey, String parentKey, String sourceTableName) throws FileNotFoundException, UnsupportedEncodingException, IOException {	
		logger.info("function getDbDataWithSource");
		zDataSource dataSource = this.getSourceType(fileName, parentKey);
		
		zDBList list = this.getDBInfo();
		zDB db = null;
		for(int dbIndex = 0 ; dbIndex < list.getData().size(); dbIndex++){
			if(list.getData().get(dbIndex).getConName().equals(dataSource.getConnName())){
				db = list.getData().get(dbIndex);
			}
		}
		if(db == null){
			return null;
		}
		
		String result = "";
		zDMain dMain = new zDMain();
		result = this.getJsonFile(fileName, objectKey, "json");
		Gson gson = new GsonBuilder().create();
		dMain = gson.fromJson(result, zDMain.class);
		
		logger.info("makeScriptDBLoad call");
		String script = this.makeScriptDBLoadWithSource(dMain, sourceTableName);
		
		logger.info("script --> " + script);
		return this.getDbDataArray(script, db, dMaxSize);
	}

	private String makeScriptDBLoadWithSource(zDMain main, String sourceTableName) {
		logger.info("function makeScriptDBLoad");
		String result = "";
		String selectHeader = "";
		
		logger.info("sourceTableName:" + sourceTableName);
		List<String> sourceFields = new ArrayList<String>();
		for(int i = 0 ; i < main.getNodeDataArray().size(); i++){
			if(main.getNodeDataArray().get(i).getText().equals(sourceTableName)){
				zNodeDataArray node = main.getNodeDataArray().get(i);
				
				for(int f = 0 ; f < node.getFields().size(); f++){
					sourceFields.add(node.getFields().get(f).getName());
				}
				break;
			}
		}
		
		for(int f = 0 ; f < sourceFields.size(); f++){
			for(int i = 0 ; i < main.getLinkDataArray().size(); i++){
				if(sourceFields.get(f).equals(main.getLinkDataArray().get(i).getFromPort())){
					selectHeader = selectHeader + main.getLinkDataArray().get(i).getFromPort() + ",";
				}
			}			
		}
		
		selectHeader = selectHeader.substring(0, selectHeader.length() - 1);
		
		String loadTable = "select " + selectHeader + " from " +  sourceTableName + ";\n";

		result += loadTable;

		return result;
	}

	private zDataSource getSourceType(String fileName, String key) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		zDataSource dataSource = null;
		String result = this.getJsonFile(fileName, key, "json");

		Gson gson = new GsonBuilder().create();
		gson = new GsonBuilder().create();
		dataSource = gson.fromJson(result, zDataSource.class);
		return dataSource;
	}


	public zAPIList getAPIInfo() throws IOException {
		logger.info("function getAPIInfo");
		zAPIList list = new zAPIList();

		String result = "";
		File file = new File(zWoringRoot + "api.json");
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
			fis.close();

		} else {
			result = "odbc.json 파일을 찾을수 없습니다.";
			logger.info(result);
		}

		Gson gson = new GsonBuilder().create();
		list = gson.fromJson(result, zAPIList.class);

		return list;
	}

	public List<zDashboardPanel> getDashboard(String dashboardId) throws IOException {
		logger.info("function getDashboard");
		zDashboard dashboard = new zDashboard();

		String result = "";
		File file = new File(zWoringRoot + "dashboards//" + dashboardId + ".json");
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
			fis.close();

		} else {
			result = "dashboards//" + dashboardId + ".json 파일을 찾을수 없습니다.";
			logger.info(result);
		}

		Gson gson = new GsonBuilder().create();
		Type listType = new TypeToken<ArrayList<zDashboardPanel>>() {}.getType();        
		List<zDashboardPanel> dashboards = gson.fromJson(result, listType);

		return dashboards;
	}
	
	
	public String getFilter(String filterId) throws IOException {
		
		logger.info("function getFilter");

		String result = "";
		File file = new File(zWoringRoot + "dashboards//filter//"  + filterId + ".json");
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
			fis.close();

		} else {
			result = "dashboards//filter//"  + filterId + ".json 파일을 찾을수 없습니다.";
			logger.info(result);
		}

		return result;
	}


	public String getFilterInfo() throws IOException {
		
		logger.info("function getFilterInfo");

		String result = "";
		File file = new File(zWoringRoot + "dashboards//filter//filterInfo.json");
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
			fis.close();

		} else {
			result = "dashboards//filter//filterInfo.json 파일을 찾을수 없습니다.";
			logger.info(result);
		}

		return result;
	}

	//테이블을 csv로 저장한다.
	public boolean saveSingleCSV(ArrayList<DataSet> objects) {
		logger.info("function saveSingleCSV");

		for(int i = 0; i < objects.size();i++){
			String sheetName = objects.get(i).getTableName();
			List <String[]> data = objects.get(i).getData();
			String content  = "";
			for(int rowCount = 0; rowCount < data.size(); rowCount++){
				String[] row = data.get(rowCount);
				for(int strCount = 0; strCount < row.length; strCount++){
					content += row[strCount] + ",";
				}
				content = content.substring(0, content.length() -1);
				content += "\n";
			}
			this.saveTextFileMS949(zWoringRoot +"process\\_CSV\\", zWoringRoot +"process\\_CSV\\" + sheetName +".csv", content);
		}
		

		return true;
	}

	
	//김창영부장
	//화면구성(작업지시서)와 테이블매핑정보를 연결함.
	//유형,테이블명,컬럼명,매핑명,연결정보,형태
	public ArrayList<MapInfo> getMapAllInfo(ArrayList<MapInfo> mapInfoList, ArrayList<DataSet> typeInfos) {
		
		List<String[]> rows = typeInfos.get(0).getData();
		for(int i = 0 ; i < mapInfoList.size(); i++){
			MapInfo mapInfo = mapInfoList.get(i);
			
			for(int rowCount = 0; rowCount < rows.size(); rowCount++){
				String[] row = rows.get(rowCount);
				if(mapInfo.getValue().equals(row[3])){
					mapInfo.setType(row[0]);
					mapInfo.setTableName(row[1]);
					mapInfo.setColumnName(row[2]);
					mapInfo.setMappingName(row[3]);
					mapInfo.setSaveInfo(row[4]);
					mapInfo.setConnectInfo(row[5]);
					mapInfo.setFigure(row[6]);
					mapInfo.setKey(row[7]);
				}
			}
		}
		
		
		return mapInfoList;
	}

	public String getDbSingleData(String sqlStr, zDB db) {
		CommonUtils commonUtils = new CommonUtils();
		commonUtils.getconnection(db);
		List<String[]> result = commonUtils.getSimpleArrayWithQuery(sqlStr, 1);
		
		if(result.size() < 1) return "찾지못함";		
		return result.get(1)[0];
	}

	public int getCountData(String sql, zDB db) {
		CommonUtils commonUtils = new CommonUtils();
		commonUtils.getconnection(db);
		int result = commonUtils.getCountData(sql, 1);
				
		return result;
	}

	public List<Definition> getDefFileList() {
		logger.info("function getAllVFiles start");
		List<Definition> results = new ArrayList<Definition>();

		File dir = new File(zWoringRoot+"process\\_DBTables\\def");
		File[] fileList = dir.listFiles();

		for (File file : fileList) {
			if (file.getPath().indexOf("MAP_") > 0) {
				logger.info("fileName -- >" + file.getName());
				String temp = file.getName().replace(".json","");
				String[] temps = temp.split("_");
				Definition def = new Definition();
				def.setCreator(temps[1]);
				def.setDefId(temps[2]);
				results.add(def);
			}
		}

		return results;
	}

	public boolean deleteDefFile(String userDefId) {
		logger.info("function getAllVFiles start");
		List<Definition> results = new ArrayList<Definition>();

		File dir = new File(zWoringRoot+"process\\_DBTables\\def");
		File[] fileList = dir.listFiles();

		for (File file : fileList) {
			if (file.getPath().indexOf(userDefId) > 0) {
				logger.info(" 삭제파일 -->" + file.getName());
				file.delete();
			}
		}

		return true;
	}

	public void saveSystemInfo(SystemInfo systemInfo, zDB db) {
		String insert = " insert into OSINFO(NAME,VERSION,ARCH) values('" + 
					systemInfo.getOsInfo().getName() + "','" +
					systemInfo.getOsInfo().getVersion() + "','" +
					systemInfo.getOsInfo().getArch() + "');";
		
		boolean success = this.queryExec(insert, db);
		
		insert = " insert into CPUINFO(LAODAVG,ALLTHREADSCPUTIME,UPTIME,THREADCOUNT,CORES) values('" + 
				systemInfo.getCpuInfo().getLoadAvg() + "','" +
				systemInfo.getCpuInfo().getAllThreadsCpuTime() + "','" +
				systemInfo.getCpuInfo().getUptime() + "','" +
				systemInfo.getCpuInfo().getThreadCount() + "','" +
				systemInfo.getCpuInfo().getCores() + "');";
		
		success = this.queryExec(insert, db);
		
		insert = " insert into MEMINFO(PHYSICALMEMORYSIZE,PHYSICALFREEMEMORY,PHYSICALFREESWAPSIZE,PHYSICALCOMMITEDVIRTUALMOMORYSIZE,VMMEMORYSIZE,VMFREEMEMORY,VMFREESWAPSIZE,VMCOMMITEDVIRTUALMOMORYSIZE) values('" + 
				systemInfo.getMemInfo().getPhysicalMemorySize() + "','" +
				systemInfo.getMemInfo().getPhysicalFreeMemory() + "','" +
				systemInfo.getMemInfo().getPhysicalFreeSwapSize() + "','" +
				systemInfo.getMemInfo().getPhysicalCommitedVirtualMemorySize() + "','" +
				systemInfo.getMemInfo().getVmTotalFreeMeomory() + "','" +
				systemInfo.getMemInfo().getVmFreeMemory() + "','" +
				systemInfo.getMemInfo().getVmMaxMemory() + "','" +
				systemInfo.getMemInfo().getVmAllocateMemory() + "');";
		success = this.queryExec(insert, db);
		
		for(DiskInfo diskInfo : systemInfo.getDiskInfo()){
			insert = " insert into DISKINFO(ROOT,TOTALSPACE,FREESPACE,USABLESPACE) values('" +
					diskInfo.getRoot().replace("\\", "") + "','" +
					diskInfo.getTotalSpace() + "','" +
					diskInfo.getFreeSpace() + "','" +
					diskInfo.getUsableSpace() + "');";
			
			success = this.queryExec(insert, db);
		}
		
		
		
	}
}
