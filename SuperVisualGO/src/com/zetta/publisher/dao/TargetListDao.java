package com.zetta.publisher.dao;

import java.util.List;

import com.zetta.publisher.model.TargetList;

public interface TargetListDao {

	public List<TargetList> selectAllTargetList();
	
	public List<TargetList> selectTargetList(String uniqueId);
	
	public int insertTargetList(TargetList targetList);
	
	public int updateTargetListInfo(TargetList targetList);
	
	public int updateTargetListStatus(TargetList targetList);
	
	public int deleteTargetList(TargetList targetList);
	
	public String maxGroupId();
	
	public String maxTriggerId();
	
	public String maxJobId();

	public List<TargetList> selectRunningTargetList();
	
	public int deleteAllTargetList();
	
}
