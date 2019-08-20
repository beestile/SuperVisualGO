package com.zetta.scheduler.service;

import org.springframework.stereotype.Service;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;

/**
 * 스케줄 정보 삭제...
 * */
@Service
public interface DeleteSchedule {

	// - TargetList 메모리에서 삭제
	public void deleteScheduleInfoMemory(TargetList targetList);
	
	public void deleteAllSchedule();
	
	// - 파일에서 삭제
	public void deleteScheduleInfoFile(TargetList targetList);
	
	// - ScheduleTarget 메모리에서 삭제
	public void deleteScheduleTarget(ScheduleTarget scheduleTarget);
	
	
}
