package com.zetta.zScheduler.utils;

import static org.quartz.JobKey.jobKey;

import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.schedule.ZScheduler;
import com.zetta.publisher.util.ZKeyUtil;
import com.zetta.publisher.util.ZTimeUtil;
import com.zetta.scheduler.trigger.impl.TriggerDaily;
import com.zetta.scheduler.trigger.impl.TriggerInterval;
import com.zetta.scheduler.trigger.impl.TriggerWeekly;
import com.zetta.zScheduler.model.SystemInfo;

public class SchedulerUtils {

	public zLogger logger = new zLogger(getClass());

	public SchedulerUtils() {
		
	}
	public boolean start(TargetList targetList) {
		logger.info("start!!!!");
		if(targetList.getStatus() == null) {
			logger.info("targetList에 status가 없습니다.");
			return false;
		}
		
		
		logger.info("targetList.getStatus() " + targetList.getStatus());
		if (targetList.getStatus().equals("run")) {
			switch (targetList.getCycleCode()) {

			case "interval":
				logger.info("case interval - " + " 실행 : " + targetList.getGroupId());
				TriggerInterval interval = new TriggerInterval();

				interval.fire(targetList);

				break;

			case "daily":
				logger.info("case daily - " + " 실행 : " + targetList.getGroupId());
				TriggerDaily daily = new TriggerDaily();

				daily.fire(targetList);

				break;

			case "weekly":
				logger.info("case weekly - " + " 실행: " + targetList.getGroupId());
				TriggerWeekly weekly = new TriggerWeekly();

				weekly.fire(targetList);

				break;

			case "monthly":
				logger.info("case monthly - 개발중." + targetList.getGroupId());
				break;

			default:
				break;
			}
		}
		
		return true;
	}

	public boolean stop(TargetList targetList) {
		Scheduler scheduler = ZScheduler.getInstance().getScheduler();
		
		boolean results = false;
		
		try {
			scheduler.unscheduleJob(new TriggerKey(targetList.getTriggerId(), targetList.getGroupId()));
			results = scheduler.deleteJob(jobKey(targetList.getJobId(), targetList.getGroupId())); // scheduler.unscheduleJob 과의 차이점 파악.
			
		} catch (SchedulerException e) {
			
			e.printStackTrace();
		}
		
		return results;
	}

	public TargetList insertScheduleGroup(TargetList targetList, List<TargetList> list) {

		targetList.setUniqueId(ZKeyUtil.uniqueKeyGenWithDate());
		targetList.setCreatedAt(ZTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
		
		String triggerId = this.maxTriggerId(list);
		targetList.setTriggerId((triggerId == null)?"TRG_1":ZKeyUtil.getNextCodeValue(triggerId));
		
		String groupId = this.maxGroupId(list);
		targetList.setGroupId((groupId == null)?"GRP_1":ZKeyUtil.getNextCodeValue(groupId));
		
		String jobId = this.maxJobId(list);
		targetList.setJobId((jobId == null)?"JOB_1":ZKeyUtil.getNextCodeValue(jobId));

	
		if(!targetList.getCycleCode().equals("interval")){			
			String cycleTime = ZTimeUtil.concatDate(targetList.getDate(), targetList.getCycleTime());			
			targetList.setCycleTime(cycleTime);
		}
		
		targetList.setStatus("stop");
		targetList.setUseYN("yes");
		
		return targetList;			
	}

	private String maxJobId(List<TargetList> list) {
		if(list == null) return null;
		int maxId = 0;
		for (TargetList targetList : list) {
			String id = targetList.getJobId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "JOB_" + maxId;
	}

	private String maxGroupId(List<TargetList> list) {
		if(list == null) return null;
		int maxId = 0;
		for (TargetList targetList : list) {
			String id = targetList.getGroupId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "GRP_" + maxId;
	}

	private String maxTriggerId(List<TargetList> list) {
		if(list == null) return null;
		int maxId = 0;
		for (TargetList targetList : list) {
			String id = targetList.getTriggerId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "TRG_" + maxId;
	}
	
	public TargetList updateGroup(TargetList targetList, TargetList sourceList) {		
//		TargetList sourceList = this.selectTargetList(targetList.getUniqueId());
		targetList.setCreatedAt(sourceList.getCreatedAt());
		targetList.setGroupId(sourceList.getGroupId());
		targetList.setJobId(sourceList.getJobId());
		targetList.setStatus(sourceList.getStatus());
		targetList.setTargets(sourceList.getTargets());
		targetList.setTriggerId(sourceList.getTriggerId());
		targetList.setUseYN(sourceList.getUseYN());
		return targetList;
	}

	public String maxStepId(TargetList targetList) {
		int maxId = 0;
		if(targetList == null) return null;
		if(targetList.getTargets() == null) return null;
		
		for (ScheduleTarget scheduleTarget : targetList.getTargets()) {
			String id = scheduleTarget.getStepId();
			if(id != null){
				int temp = Integer.valueOf(id.split("_")[1]);
				if(temp>maxId) maxId = temp;
			}
		}
		if(maxId == 0) return null;
		else  return "STP_" + maxId;
	}

	public List<ScheduleTarget> deleteStep(List<ScheduleTarget> targets, String stepId) {
		
		for (ScheduleTarget scheduleTarget : targets) {
			String id = scheduleTarget.getStepId();
			if(id.equals(stepId)){
				targets.remove(scheduleTarget);
				break;
			}
		}
		
		return targets;
	}

	public TargetList updateScheduleTargetInfo(TargetList targetList, ScheduleTarget scheduleTarget) {
		logger.info("updateScheduleTargetInfo");
		List<ScheduleTarget> scheduleTargets = targetList.getTargets();
		for(ScheduleTarget source : scheduleTargets){
			if(source.getStepId().equals(scheduleTarget.getStepId())){
				
				scheduleTarget.setGroupId(source.getGroupId());
				scheduleTarget.setJobId(source.getJobId());
				scheduleTarget.setStepId(source.getStepId());
				scheduleTarget.setTargetListId(source.getTargetListId());
				scheduleTarget.setTargetListName(source.getTargetListName());
								
				logger.info("scheduleTarget getMethod " + scheduleTarget.getMethod());
				logger.info("scheduleTarget getParameter " + scheduleTarget.getParameter());
				logger.info("scheduleTarget getProgPath " + scheduleTarget.getProgPath());
				logger.info("scheduleTarget getpType " + scheduleTarget.getpType());
				
				scheduleTargets  = this.deleteStep(scheduleTargets, source.getStepId());
				scheduleTargets.add(scheduleTarget);
				targetList.setTargets(scheduleTargets);
				return targetList;
			}
		}
		return targetList;
	}

	public SystemInfo getSystemInfo() {
		return new SystemUtils().getSystemInfo();
	}
}
