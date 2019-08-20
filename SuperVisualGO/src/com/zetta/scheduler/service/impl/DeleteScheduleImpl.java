package com.zetta.scheduler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zetta.publisher.conf.ZProperty;
import com.zetta.publisher.dao.ScheduleTargetDao;
import com.zetta.publisher.dao.TargetListDao;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.util.ZFileUtil;
import com.zetta.scheduler.service.DeleteSchedule;

@Service
public class DeleteScheduleImpl implements DeleteSchedule {

	@Autowired
	TargetListDao targetListDao;
	
	@Autowired
	ScheduleTargetDao scheduleTargetDao;
	
	@Autowired
	ZProperty zProperty;
	
	
	@Override
	public void deleteScheduleInfoMemory(TargetList targetList) {

		// 트랜잭션 처리를 고려해야 함...
		targetListDao.deleteTargetList(targetList);
		
		// 트랜잭션 처리를 고려해야 함...
		List<ScheduleTarget> list = targetList.getTargets();
		
		for(ScheduleTarget scheduleTarget : list){
			scheduleTargetDao.deleteScheduleTarget(scheduleTarget);
		}
	}

	@Override
	public void deleteScheduleInfoFile(TargetList targetList) {

		new ZFileUtil().deletefile(targetList.getUniqueId() + ".json"); // 확장자 고려...
		
	}

	@Override
	public void deleteScheduleTarget(ScheduleTarget scheduleTarget) {
		scheduleTargetDao.deleteScheduleTarget(scheduleTarget);
	}

	@Override
	public void deleteAllSchedule() {
		scheduleTargetDao.deleteAllScheduleTarget();
		targetListDao.deleteAllTargetList();
	}
}
