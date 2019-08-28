package com.zetta.scheduler.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zetta.common.utils.SpringApplicationContext;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.scheduler.job.batch.CommandBatchJobStep;
import com.zetta.scheduler.job.batch.HttpBatchJobStep;
import com.zetta.scheduler.service.SelectSchedule;

public class MyBatchJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		SelectSchedule selectSchedule = null;
		try{
			selectSchedule = SpringApplicationContext.getBean("selectSchedule", SelectSchedule.class);
		}catch (Exception e) {
			initialExecute(context);
			return;
		}
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		TargetList targets = (TargetList)dataMap.get("target"); 
		
		//쿼츠Job --> 외부프로그램 실행
		
		List<ScheduleTarget> list = targets.getTargets();
		
		for(ScheduleTarget target : list){
			
			// 2016.03.15. Add... 
			// TargetList의 Status를 계속 체크해서 'run' 상태이면 실행, 'stop'상태이면 return
			TargetList currentTargets = selectSchedule.selectTargetList(targets.getUniqueId());
			
			if(currentTargets.getStatus().equals("run")){
			// 2016.03.15. End... 	
				
				if(target.getpType().equals("command")){
					
					new CommandBatchJobStep().doJopStep(target);
					
				}else if(target.getpType().equals("http")){
					
					new HttpBatchJobStep().doJopStep(target);
				} 
			}else{ 
				return; 
			}
		}		
	}
	
	private void initialExecute(JobExecutionContext context){
		
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		TargetList targets = (TargetList)dataMap.get("target"); 
		
		List<ScheduleTarget> list = targets.getTargets();
		
		for(ScheduleTarget target : list){
			if(target.getpType().equals("command")){
				
				new CommandBatchJobStep().doJopStep(target);
				
			}else if(target.getpType().equals("http")){
				
				new HttpBatchJobStep().doJopStep(target);
			} 	
		}
	} 
}
