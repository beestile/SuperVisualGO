package com.zetta.zScheduler.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.scheduler.service.ScheduleManager;
import com.zetta.zScheduler.utils.CommonConfig;
import com.zetta.zScheduler.utils.DataUtils;
import com.zetta.zScheduler.utils.zLogger;

@Controller
public class SchedulerCRUDController {
	public zLogger logger = new zLogger(getClass());
	public ScheduleManager scheduleManager= new ScheduleManager();
	public CommonConfig config = new CommonConfig();
	public DataUtils dataUtils = new DataUtils(config.getProperties("ZWORKINGROOT"), config.getProperties("QVXROOT"));
	
	@RequestMapping(value = "/schedule/load.do", method = RequestMethod.POST)
	@ResponseBody
	public void load(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/schedule/load.do");
		String result = null;

		try {
			List<TargetList> list = dataUtils.getTargetLists();
			Gson gson = new GsonBuilder().create();
			result = gson.toJson(list);
			response.getWriter().print(result);
			
			logger.info("result::" + result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/schedule/reLoad.do", method = RequestMethod.POST)
	@ResponseBody
	public void reLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("/schedule/reLoad.do");
		boolean isOK = dataUtils.initialLoad();
		response.getWriter().print(isOK);
	} 
	
	/*
	 * 개발중... 스케줄 전체삭제 기능인데 개발할 필요가 있는지 검토.
	 */
	@RequestMapping(value = "/delete/all.do", method = RequestMethod.GET)
	public void deleteALL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/delete/all.do");
		response.getWriter().print(true);
	}
	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄을 Delete HSQL에서도 삭제, 파일에서도
	 * 삭제 --> 동시 삭제
	 */
	@RequestMapping(value = "/delete/selectedList.do", method = RequestMethod.GET)
	public void deleteSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/delete/selectedList.do");
		for (String uniqueId : uniqueIds) {
			TargetList targetList = dataUtils.selectTargetList(uniqueId);
			
			// run상태이면 Stop
			if(targetList.getStatus().equals("run")){
				dataUtils.stop(targetList);
			}
			// 1. file에서 삭제
			dataUtils.deleteScheduleInfoFile(targetList.getUniqueId());
		}
		response.getWriter().print(true);
	}
	
	/*
	 * View에서 선택된 한개의 스케줄아이디(uniqueId)를 받아서 한개의 스케줄을 Delete HSQL에서도 삭제, 파일에서도 삭제
	 * --> 동시 삭제
	 */
	@RequestMapping(value = "/delete/selectedOne.do", method = RequestMethod.POST)
	public void deleteSchedule(@RequestParam(value = "uniqueId") String uniqueId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/delete/selectedOne.do");
		
		TargetList targetList = dataUtils.selectTargetList(uniqueId);
		// 1. file에서 삭제
		dataUtils.deleteScheduleInfoFile(targetList.getUniqueId());
		response.getWriter().print(true);
	}
	
	// 2016.03.17. Add...
	@RequestMapping(value = "/deleteStep/selectedOne.do", method = RequestMethod.POST)
	public void deleteStep(
			@RequestParam(value = "targetListId") String targetListId,
			@RequestParam(value = "stepId") String stepId,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception{

		logger.info("/deleteStep/selectedOne.do");		
		TargetList targetList = dataUtils.selectTargetList(targetListId);
		List<ScheduleTarget> target = targetList.getTargets();
		target = dataUtils.deleteStep(target, stepId);
		targetList.setTargets(target);
		
		fileUpdate(targetList);
		
		response.getWriter().print(true);
	}
	
	@RequestMapping(value = "/schedule/addGroup.do", method = RequestMethod.POST)
	public void addGroup(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("/schedule/addGroup.do");
		boolean result = true;
		
		dataUtils.insertScheduleGroup(new Gson().fromJson(content, TargetList.class));
		
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@RequestMapping(value = "/schedule/updateGroup.do", method = RequestMethod.POST)
	public void updateGroup(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		logger.info("/schedule/updateGroup.do");
		boolean result = true;
		
		Gson gson = new Gson();
		TargetList targetList = gson.fromJson(content, TargetList.class);
		
		targetList = dataUtils.updateGroup(targetList);
		fileUpdate(targetList);
		
		response.getWriter().print(result);		
	}
	
	@RequestMapping(value = "/schedule/addTarget.do", method = RequestMethod.POST)
	public void addTarget(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("response");
		boolean result = true;
	
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		ScheduleTarget target = gson.fromJson(content, ScheduleTarget.class);
		dataUtils.insertScheduleTarget(target);
		
		System.out.println(target.getParameter());
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/schedule/updateTarget.do", method = RequestMethod.POST)
	public void updateTarget(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		logger.info("/schedule/updateTarget.do");
		logger.info("content :  " + content );
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		ScheduleTarget scheduleTarget = gson.fromJson(content, ScheduleTarget.class);
				
		List<TargetList> targetLists = dataUtils.getTargetLists();
		for(TargetList targetList : targetLists){
			logger.info("targetList.getGroupId() equals scheduleTarget.getGroupId() -->" + targetList.getGroupId() + "::" + scheduleTarget.getGroupId());
			if(targetList.getGroupId().equals(scheduleTarget.getGroupId())){
				targetList = dataUtils.updateScheduleTargetInfo(targetList, scheduleTarget);
				fileUpdate(targetList);
				break;
			}
		}
		
		response.getWriter().println(true);
	}
	
	
	
	private void fileUpdate(TargetList targetList){		
		logger.info("function fileUpdate");
		// 기존파일 삭제
		dataUtils.deleteScheduleInfoFile(targetList.getUniqueId());
		// 새로운파일 생성
		dataUtils.createScheduleInfoFile(targetList);
	}
}
