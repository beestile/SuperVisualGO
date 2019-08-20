package com.zetta.scheduler.job.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.zScheduler.utils.zLogger;

public class HttpBatchJobStep{
	public zLogger logger = new zLogger(getClass());
	//private static final Logger logger = LoggerFactory.getLogger(HttpBatchJobStep.class);
	private boolean running = true;
	private final int waitMaxCount = 5;

	// apache exec를 활용해서 외부 프로그램 실행.
	public void doJopStep(ScheduleTarget target) {
		
		String myUrl = "";
		String processKey = target.getGroupId() + target.getStepId();
		String param = target.getParameter();
//			myUrl  = target.getProgPath() + "?" + URLEncoder.encode(param, "UTF-8");
//			param += "&processKey=" + processKey;
		myUrl = target.getProgPath();

		BatchLog.stepLog(target, "", "1-실행"); // 로그 
		try {				
			String result = get(myUrl, param);
			BatchLog.stepLog(target, result, "3-정상종료"); // 로그 
		} catch (Exception e) {
			BatchLog.stepLog(target, e.getMessage(), "2-비정상종료"); // 로그 
		}
	}
	

	public String get(String url, String paramStr) throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		String resultStr = "";
		String[] params = paramStr.split("&");
		
		logger.info("executing request before -->" + url);
//		logger.info("paramStr -->" + paramStr + " params.length-->" + params.length);
		try {
			// HttpGet생성
//			logger.info("get 1");
			URIBuilder builder = new URIBuilder();
//			logger.info("get 2");
			builder.setScheme("http");
//			logger.info("get 3");
			builder.setHost(url.replace("http://", ""));

//			logger.info("get 4");
		    for (String param : params) {
		    	logger.info("param -->'" + param + "'");
		    	if(param.split("=").length > 1){
			    	String name = param.split("=")[0];
			    	String value = param.split("=")[1];
			        builder.setParameter(name, value);
		    	}
		    }
		    
//		    logger.info("get 5");
			HttpGet httpget = new HttpGet(builder.build());
//			logger.info("get 6");
//			logger.info("executing request " + httpget.getURI());
//			logger.info("get 7");
			HttpResponse response = httpclient.execute(httpget);
//			logger.info("get 8");
			HttpEntity entity = response.getEntity();
//			logger.info("----------------------------------------");
			// 응답 결과
			System.out.println(response.getStatusLine());
			if (entity != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
 
				String line = "";
				while ((line = rd.readLine()) != null) {
					resultStr += line;
				}
			}
			httpget.abort();
			httpclient.getConnectionManager().shutdown();
 
		} catch (ClientProtocolException e) {
			logger.info("ClientProtocolException 1 ::" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException 2 ::" + e.getMessage());
			e.printStackTrace();
		} catch(Exception e){
			logger.info("Exception 2 ::" + e.getMessage());
			e.printStackTrace();
		}finally {
			httpclient.getConnectionManager().shutdown();
			
			logger.info("resultStr::" + resultStr);
			
			return resultStr;
		}
	}
}
