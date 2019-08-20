package com.zetta.scheduler.service;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.util.ZFileUtil;
import com.zetta.zScheduler.utils.CommonConfig;
import com.zetta.zScheduler.utils.DataUtils;
import com.zetta.zScheduler.utils.zLogger;

public class ScheduleManager {
	public zLogger logger = new zLogger(getClass());
	public CommonConfig config = new CommonConfig();
	public DataUtils dataUtils = new DataUtils(config.getProperties("ZWORKINGROOT"), config.getProperties("QVXROOT"));
	public ScheduleManager(){
		logger.info("initialLoad");
		List<String> list = new ZFileUtil().dirFilesToStringList();

		Gson gson = new Gson();

		try {
			for (String json : list) {
				logger.info("json::" + json);
				TargetList targetList = gson.fromJson(json, TargetList.class);
				dataUtils.start(targetList);
			}
		} catch (JsonSyntaxException e) {
			logger.info("error: " + e.getMessage());
		}
	}

}
