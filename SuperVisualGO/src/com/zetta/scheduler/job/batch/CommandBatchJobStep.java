package com.zetta.scheduler.job.batch;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.util.ZTimeUtil;

public class CommandBatchJobStep {
	//private static final Logger logger = LoggerFactory.getLogger(CommandBatchJobStep.class);
	// apache exec를 활용해서 외부 프로그램 실행.
	public void doJopStep(ScheduleTarget target){
			
		String line = target.getProgPath() + " " + target.getParameter();		
 
		BatchLog.stepLog(target, "", "1-실행"); // 로그
		
		CommandLine cmdLine = CommandLine.parse(line);		
		DefaultExecutor executor = new DefaultExecutor();
		
		try {
			System.out.println(ZTimeUtil.getBasicCurrentDateTime());
			executor.execute(cmdLine);
			BatchLog.stepLog(target, "", "3-정상종료"); // 로그 
		
		} catch (Exception e) {
			BatchLog.stepLog(target, e.getMessage(), "2-비정상종료"); // 로그 
		}
	}
}
