package com.zetta.scheduler.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zetta.publisher.model.TargetList;

@Service
public interface SelectSchedule {

	public List<TargetList> selectAllTargetList();
	
	public List<TargetList> selectRunningTargetList();
	
	public TargetList selectTargetList(String uniqueId);
	
	public String maxGroupId();
	
	public String maxTriggerId();
	
	public String maxJobId();
}
