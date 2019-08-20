package com.zetta.scheduler.service;

import org.springframework.stereotype.Service;

import com.zetta.publisher.model.TargetList;

/**
 * 로그남기는 코드 추가해야 함.
 * 동작중인 작업 스케줄 종료
 * jobId : 
 * groupId : 
 * */
@Service
public interface StopSchedule {
	
	/*
	 * Deleting a Job and Unscheduling All of Its Triggers
	 * */
	public boolean stop(TargetList targetList);
}
