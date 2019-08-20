package com.zetta.publisher.schedule;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class ZScheduler {

	private static ZScheduler zScheduler;
	
	private Scheduler scheduler;
	
	private ZScheduler(){
		SchedulerFactory sf = new StdSchedulerFactory();
		
		try {
			this.scheduler = sf.getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized ZScheduler getInstance(){
	
		if(zScheduler == null){
			zScheduler = new ZScheduler();
		}
		
		return zScheduler;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}
	
}
