package com.zetta.scheduler.job.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.zetta.publisher.model.BatchResult;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.util.ZTimeUtil;

public class BatchLog {

	private static final Logger logger = LoggerFactory.getLogger(BatchLog.class);
	
	public static void stepLog(ScheduleTarget target, String msg, String status){
		BatchResult batchResult = new BatchResult(target);
		batchResult.setCurrentTime(ZTimeUtil.getBasicCurrentDateTime());
		batchResult.setOrdering(ZTimeUtil.getMillisTime());
		batchResult.setErrorMsg(msg);
		batchResult.setCurrentStatus(status);
		logger.info(new Gson().toJson(batchResult));
	}
}
