package com.zetta.scheduler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zetta.publisher.dao.ScheduleTargetDao;
import com.zetta.publisher.dao.TargetListDao;
import com.zetta.publisher.model.TargetList;
import com.zetta.scheduler.service.SelectSchedule;

@Service("selectSchedule")
public class SelectScheduleImpl implements SelectSchedule {

	@Autowired
	TargetListDao targetListDao;
	
	@Autowired
	ScheduleTargetDao scheduleTargetDao;
	
	@Override
	public List<TargetList> selectAllTargetList() {
		List<TargetList> result = targetListDao.selectAllTargetList();
		
		for(TargetList targetList : result){
			targetList.setTargets(scheduleTargetDao.selectAllScheduleTarget(targetList));
		}
		
		return result;
	}
	
	@Override
	public List<TargetList> selectRunningTargetList() {
		List<TargetList> result = targetListDao.selectRunningTargetList();
		
		for(TargetList targetList : result){
			targetList.setTargets(scheduleTargetDao.selectAllScheduleTarget(targetList));
		}
		
		return result;
	}

	@Override
	public TargetList selectTargetList(String uniqueId) {
		
		List<TargetList> list = targetListDao.selectTargetList(uniqueId);
		
		if(list.size() > 1){
			// 에러처리
			return null;
		}else{
			
			for(TargetList targetList : list){
				targetList.setTargets(scheduleTargetDao.selectAllScheduleTarget(targetList));
			}			
			return list.get(0);
		}
	}

	@Override
	public String maxGroupId() {
		return targetListDao.maxGroupId();
	}

	@Override
	public String maxTriggerId() {
		return targetListDao.maxTriggerId();
	}

	@Override
	public String maxJobId() {
		return targetListDao.maxJobId();
	}
}
