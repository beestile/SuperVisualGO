package com.zetta.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zetta.publisher.conf.ZProperty;
import com.zetta.publisher.dao.ScheduleTargetDao;
import com.zetta.publisher.dao.TargetListDao;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.util.ZFileUtil;
import com.zetta.publisher.util.ZKeyUtil;
import com.zetta.publisher.util.ZTimeUtil;
import com.zetta.scheduler.service.CreateSchedule;
import com.zetta.scheduler.service.DeleteSchedule;
import com.zetta.scheduler.service.SelectSchedule;

@Service
public class CreateScheduleImpl implements CreateSchedule {

	@Autowired
	TargetListDao targetListDao;
	
	@Autowired
	ScheduleTargetDao scheduleTargetDao;
	
	@Autowired
	SelectSchedule selectSchedule;
	
	@Autowired
	DeleteSchedule deleteSchedule;
	
	@Autowired
	ZProperty zProperty;
	
	
	@Override
	public void insertScheduleGroup(TargetList targetList) {
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		targetList.setUniqueId(ZKeyUtil.uniqueKeyGenWithDate());
		targetList.setCreatedAt(ZTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		String triggerId = targetListDao.maxGroupId();
		targetList.setTriggerId((triggerId == null)?"TRG_1":ZKeyUtil.getNextCodeValue(triggerId));
		
		String groupId = targetListDao.maxGroupId();
		targetList.setGroupId((groupId == null)?"GRP_1":ZKeyUtil.getNextCodeValue(groupId));
		
		String jobId = targetListDao.maxJobId();
		targetList.setJobId((jobId == null)?"JOB_1":ZKeyUtil.getNextCodeValue(jobId));
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////			
		if(!targetList.getCycleCode().equals("interval")){
			
			String cycleTime = ZTimeUtil.concatDate(targetList.getDate(), targetList.getCycleTime());			
			targetList.setCycleTime(cycleTime);
		}		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		targetListDao.insertTargetList(targetList); // 1
		this.createScheduleInfoFile(targetList); // 2
		
	}

	@Override
	public void insertScheduleTarget(ScheduleTarget target) {
			
		//step 생성코드 추가
		String stepId = scheduleTargetDao.maxStepId(target);		
		target.setStepId((stepId == null)?"STP_1":ZKeyUtil.getNextCodeValue(stepId));
		
		// 
		if(target.getpType().equals("command")){
			target.setMethod("");
		}else if(target.getpType().equals("http")){
						
		}
		
		scheduleTargetDao.insertScheduleTarget(target);
		
		//
		TargetList targetList = selectSchedule.selectTargetList(target.getTargetListId());
		
		// 기존파일 삭제
		deleteSchedule.deleteScheduleInfoFile(targetList);
		// 새로운 파일 생성
		this.createScheduleInfoFile(targetList);	
	}

	@Override
	public void createScheduleInfoFile(TargetList targetList) {	
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String result = gson.toJson(targetList);
		new ZFileUtil().stringToFile(targetList.getUniqueId() + ".json", result);		
	}
}
