package com.zetta.scheduler.service;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;

/**
 * 
 * */
public interface CreateSchedule {

	public void insertScheduleGroup(TargetList targetList);
	
	public void insertScheduleTarget(ScheduleTarget target);
	
	public void createScheduleInfoFile(TargetList targetList);
}
