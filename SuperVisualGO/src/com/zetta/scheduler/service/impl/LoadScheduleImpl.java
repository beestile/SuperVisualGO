package com.zetta.scheduler.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.zetta.publisher.conf.ZProperty;
import com.zetta.publisher.dao.ScheduleTargetDao;
import com.zetta.publisher.dao.TargetListDao;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.util.ZFileUtil;
import com.zetta.scheduler.service.LoadSchedule;
import com.zetta.scheduler.service.StartSchedule;

@Service
@Component
public class LoadScheduleImpl implements LoadSchedule{

	//private static final Logger logger = LoggerFactory.getLogger(LoadScheduleImpl.class);
	
	public static boolean inital = true;
	
//	@Autowired
	TargetListDao targetListDao;
	
//	@Autowired
	ScheduleTargetDao scheduleTargetDao;
	
//	@Autowired
	StartSchedule startSchedule;
	
//	@Autowired
	ZProperty zProperty;
	
/*	@Value("#{zConf['zconf.dataRootPath']}")
	private String dataRootPath;*/
	
	/*
	 * Component 어노테이션과
	 * PostConstruct 어노테이션을 통해
	 * WAS가 재기동 될시 본 함수는 반드시 자동으로 실행되게 됨.
	 * 
	 * - 파일로 저장되어 있는 스케줄 정보들을 HSQL에 적재하는 작업
	 * */
	@PostConstruct
	public void initialLoad(){
		zProperty = new ZProperty();
		
		// 1. 파일로 저장된 스케줄 정보 로드
		List<String> list = new ZFileUtil().dirFilesToStringList();
		
		Gson gson = new Gson();
		
		for(String json : list){
			
			TargetList targetList = gson.fromJson(json, TargetList.class);
//			targetListDao.insertTargetList(targetList);
//			
//			if(targetList.getTargets() != null){
//				for(ScheduleTarget target : targetList.getTargets()){
//					
//					scheduleTargetDao.insertScheduleTarget(target);				
//				}	
//			}
			
			if(targetList.getStatus().equals("run")){
				startSchedule.start(targetList);
			}
		}
	}
}
