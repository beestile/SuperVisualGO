package com.zetta.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zetta.publisher.dao.ScheduleTargetDao;
import com.zetta.publisher.dao.TargetListDao;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.scheduler.service.UpdateSchedule;

@Service
public class UpdateScheduleImpl implements UpdateSchedule {

	@Autowired
	TargetListDao targetListDao;
	
	@Autowired
	ScheduleTargetDao scheduleTargetDao;
	
	@Override
	public void updateTargetListStatus(TargetList targetList) {
		
		targetListDao.updateTargetListStatus(targetList);

	}

	@Override
	public void updateTargetListInfo(TargetList targetList) {
		
		targetListDao.updateTargetListInfo(targetList);
		
	}

	@Override
	public void updateScheduleTargetInfo(ScheduleTarget scheduleTarget) {

		scheduleTargetDao.updateScheduleTargetInfo(scheduleTarget);
		
	}

}
