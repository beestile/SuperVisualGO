package com.zetta.publisher.util;

import com.zetta.zScheduler.utils.zLogger;

public class ZDataUtils {

	public zLogger logger = new zLogger(getClass());

//	public boolean saveTextFile(String folderName, String path, String content) {
//		logger.info("saveTextFile::" + folderName + path);
//		File file = new File(folderName);
//		if (!file.exists()) {
//			if (file.mkdir()) {
//				logger.info("Directory is created!");
//			} else {
//				logger.info("Failed to create directory!");
//			}
//		}
//
//		try {
//
//			File targetFile = new File(path);
//			targetFile.createNewFile();
//
//			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile.getPath()), "UTF8"));
//
//			output.write(content);
//			output.close();
//		} catch (UnsupportedEncodingException uee) {
//			uee.printStackTrace();
//			return false;
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//			return false;
//		}
//
//		return true;
//	}


//	public String getJsonFile(String folder, String fileName, String fileType) throws FileNotFoundException, UnsupportedEncodingException, IOException {
//
//		logger.info("function getJsonFile start");
//		logger.info("C:\\zWorking\\" + folder + "\\" + fileName + "." + fileType);
//		String result = "";
//		
//		File file = new File("C:\\zWorking\\" + folder + "\\" + fileName + "." + fileType);
//		if (file.exists()) {
//			BufferedReader br = null;
//			InputStreamReader isr = null;
//			FileInputStream fis = null;
//			fis = new FileInputStream(file);
//			isr = new InputStreamReader(fis, "UTF-8");
//			br = new BufferedReader(isr);
//			String temp = "";
//			while ((temp = br.readLine()) != null) {
//				result += temp + "\n";
//			}
//			// result = result.replace("\"class\"", "\"classes\"");
//
//			isr.close();
//		} else {
//			result = "[\"notFound\"]";
//			logger.info(result);
//		}
//		return result;
//	}

//	public String getJsonFile(String filePath) throws FileNotFoundException, UnsupportedEncodingException, IOException {
//
//		logger.info("function getJsonFile start");
//		logger.info(filePath);
//		String result = "";
//		File file = new File(filePath);
//		if (file.exists()) {
//			BufferedReader br = null;
//			InputStreamReader isr = null;
//			FileInputStream fis = null;
//			fis = new FileInputStream(file);
//			isr = new InputStreamReader(fis, "UTF-8");
//			br = new BufferedReader(isr);
//			String temp = "";
//			while ((temp = br.readLine()) != null) {
//				result += temp;
//			}
//
//			isr.close();
//		} else {
//			result = "[\"notFound\"]";
//			logger.info(result);
//		}
//		return result;
//	}


	// 특정경로의 모든 디렉토리를 얻어온다.
//	public List<String> getDirectories(String directoryName) {
//		List<String> directoryNames = new ArrayList<String>();
//
//		File dir = new File(directoryName);
//		File[] fileList = dir.listFiles();
//
//		for (File file : fileList) {
//			if (file.isDirectory()) {
//				directoryNames.add(file.getPath());
//			}
//		}
//		return directoryNames;
//	}
}
