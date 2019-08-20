package com.zetta.scheduler.service;

import org.springframework.stereotype.Service;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;

/**
 * 스케줄정보 변경
 * */
@Service
public interface UpdateSchedule {
	
	public void updateTargetListStatus(TargetList targetList);
	
	public void updateTargetListInfo(TargetList targetList);
	
	public void updateScheduleTargetInfo(ScheduleTarget scheduleTarget);
}
