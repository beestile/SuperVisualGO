package com.zetta.scheduler.service.impl;

import org.springframework.stereotype.Service;

import com.zetta.publisher.model.TargetList;
import com.zetta.scheduler.service.StartSchedule;
import com.zetta.scheduler.trigger.impl.TriggerDaily;
import com.zetta.scheduler.trigger.impl.TriggerInterval;
import com.zetta.scheduler.trigger.impl.TriggerWeekly;

/**
 * 
 * 쿼츠를 통한 작업 스케줄 실행.
 * */
@Service
public class StartScheduleImpl implements StartSchedule {
	
	public void start(TargetList targets){
		
		switch (targets.getCycleCode()) {
		
		case "interval":
			System.out.println("case interval - " + " 실행");
			TriggerInterval interval = new TriggerInterval();
			
			interval.fire(targets);
			
			break;
			
		case "daily":
			System.out.println("case daily - " + " 실행");
			TriggerDaily daily = new TriggerDaily();
		
			daily.fire(targets);
			
			break;
		
		case "weekly":
			System.out.println("case weekly - " + " 실행");
			TriggerWeekly weekly = new TriggerWeekly();
			
			weekly.fire(targets);
			
			break;
			
		case "monthly":
			// 개발중...
			break;
		
		default:
			break;
		}
	}
	
}
