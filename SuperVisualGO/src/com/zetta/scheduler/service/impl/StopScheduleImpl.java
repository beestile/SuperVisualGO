package com.zetta.scheduler.service.impl;

import static org.quartz.JobKey.jobKey;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.schedule.ZScheduler;
import com.zetta.scheduler.service.StopSchedule;

/**
 * 로그남기는 코드 추가해야 함.
 * 동작중인 작업 스케줄 종료
 * jobId : 
 * groupId : 
 * */
@Service
public class StopScheduleImpl implements StopSchedule {
	/*
	 * Deleting a Job and Unscheduling All of Its Triggers
	 * */
	public boolean stop(TargetList targetList){
		
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
}
