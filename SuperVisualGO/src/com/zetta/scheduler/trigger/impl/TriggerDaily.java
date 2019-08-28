package com.zetta.scheduler.trigger.impl;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.schedule.ZScheduler;
import com.zetta.publisher.util.ZTimeUtil;
import com.zetta.scheduler.job.MyBatchJob;
import com.zetta.scheduler.trigger.ZTrigger;

public class TriggerDaily implements ZTrigger {

	@Override
	public void fire(TargetList targets) {
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("target", targets);
		
		JobDetail job = newJob(MyBatchJob.class)
				.withIdentity(targets.getJobId(), targets.getGroupId())
				.usingJobData(jobDataMap)
				.build();
				
		///////////////////////////////////////////////////////////
		int hh = Integer.parseInt(targets.getCycleTime().split(":")[0]);
		int mm = Integer.parseInt(targets.getCycleTime().split(":")[1]);
		///////////////////////////////////////////////////////////
		
		Trigger trigger = newTrigger().withIdentity(targets.getTriggerId(), targets.getGroupId())
			    .startNow()
			    .withSchedule(dailyAtHourAndMinute(hh, mm)) // fire every wednesday at 15:00
			    .build();				
		try {			
			Scheduler scheduler = ZScheduler.getInstance().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
		} catch (SchedulerException e) {
			e.printStackTrace();			
		}
		
	}

}
