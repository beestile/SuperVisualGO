package com.zetta.publisher.dao;

import java.util.List;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;

public interface ScheduleTargetDao {

	public List<ScheduleTarget> selectAllScheduleTarget(TargetList targetList);
	
	public List<ScheduleTarget> selectScheduleTarget(ScheduleTarget scheduleTarget);
	
	public int insertScheduleTarget(ScheduleTarget scheduleTarget);
	
	public int updateScheduleTargetInfo(ScheduleTarget scheduleTarget);
	
	public int deleteScheduleTarget(ScheduleTarget scheduleTarget);
	
	public String maxStepId(ScheduleTarget scheduleTarget);
	
	public int deleteAllScheduleTarget();
	
}
