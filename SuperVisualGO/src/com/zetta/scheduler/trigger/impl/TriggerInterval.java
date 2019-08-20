package com.zetta.scheduler.trigger.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.schedule.ZScheduler;
import com.zetta.scheduler.job.MyBatchJob;
import com.zetta.scheduler.trigger.ZTrigger;

public class TriggerInterval implements ZTrigger {

	@Override
	public void fire(TargetList targets) {
		
		JobDataMap jobDataMap = new JobDataMap(); 
		jobDataMap.put("target", targets); 
		
		JobDetail job = newJob(MyBatchJob.class)
				.withIdentity(targets.getJobId(), targets.getGroupId())
				.usingJobData(jobDataMap)
				.build();
		
		//////////////////////////////////////////////////////////////		
		String cycleTime = targets.getCycleTime().toUpperCase();
		//////////////////////////////////////////////////////////////
		
		Trigger trigger = null;
		if(cycleTime.endsWith("S")){
			
			int interval = Integer.parseInt(cycleTime.replace("S", ""));			
			trigger = newTrigger().withIdentity(targets.getTriggerId(), targets.getGroupId())
			.startNow()
			.withSchedule(simpleSchedule().withIntervalInSeconds(interval).repeatForever())
			.build();
			
		}else{
			
			int interval = Integer.parseInt(targets.getCycleTime());
			trigger = newTrigger().withIdentity(targets.getTriggerId(), targets.getGroupId())
					.startNow()
					.withSchedule(simpleSchedule().withIntervalInMinutes(interval).repeatForever())
					.build();				
		}
	
		try {			
			Scheduler scheduler = ZScheduler.getInstance().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
		} catch (SchedulerException e) {
			e.printStackTrace();
			
		}
	}
}
