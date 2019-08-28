package com.zetta.zScheduler.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zetta.common.utils.zLogger;
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.util.ZKeyUtil;
import com.zetta.zScheduler.model.schedulerVO;
import com.zetta.zScheduler.utils.SchedulerUtils;

@Controller
public class SchedulerCRUDController {
	
	@Autowired
    private schedulerSvc schedulerSvc;
	
	public zLogger logger = new zLogger(getClass());
	public SchedulerUtils schedulerUtils = new SchedulerUtils();
	
	@RequestMapping(value = "/schedule/load.do", method = RequestMethod.POST)
	@ResponseBody
	public void load(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/schedule/load.do");
		String result = null;
		
		try {
			Gson gson = new GsonBuilder().create();
			List<schedulerVO> list = schedulerSvc.selectschedulerList();
			List<TargetList> resultList = new ArrayList<TargetList>();
			
			for(schedulerVO vo : list) {
				TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
				resultList.add(targetList);
			}
			
			result = gson.toJson(resultList);
			
			logger.info("result::" + result);
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/schedule/reLoad.do", method = RequestMethod.POST)
	@ResponseBody
	public void reLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("/schedule/reLoad.do");
		
		try {
			Gson gson = new GsonBuilder().create();
			List<schedulerVO> list = schedulerSvc.selectschedulerList();
			
			for(schedulerVO vo : list) {
				TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
				schedulerUtils.start(targetList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		response.getWriter().print(true);
	} 

	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄을 Delete HSQL에서도 삭제, 파일에서도
	 * 삭제 --> 동시 삭제
	 */
	@RequestMapping(value = "/delete/selectedList.do", method = RequestMethod.GET)
	public void deleteSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/delete/selectedList.do");
		
		Gson gson = new GsonBuilder().create();
		for (String uniqueId : uniqueIds) {
			
			schedulerVO vo = schedulerSvc.selectschedulerOne(uniqueId);
			TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
			
			// run상태이면 Stop
			if(targetList.getStatus().equals("run")){
				schedulerUtils.stop(targetList);
			}
			// 1. file에서 삭제
			schedulerSvc.deleteObjectJsonOne(vo);
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
		
		Gson gson = new GsonBuilder().create();
			
		schedulerVO vo = schedulerSvc.selectschedulerOne(uniqueId);
		TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
		
		// run상태이면 Stop
		if(targetList.getStatus().equals("run")){
			schedulerUtils.stop(targetList);
		}
		// 1. file에서 삭제
		schedulerSvc.deleteObjectJsonOne(vo);
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
		
		Gson gson = new GsonBuilder().create();
		
		schedulerVO vo = schedulerSvc.selectschedulerOne(targetListId);
		TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
		
		List<ScheduleTarget> target = targetList.getTargets();
		target = schedulerUtils.deleteStep(target, stepId);
		targetList.setTargets(target);
		
		schedulerSvc.insertscheduler(targetListId, "S", gson.toJson(targetList));
		
		response.getWriter().print(true);
	}
	
	@RequestMapping(value = "/schedule/addGroup.do", method = RequestMethod.POST)
	public void addGroup(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("/schedule/addGroup.do");
		boolean result = true;
		
		Gson gson = new GsonBuilder().create();
		List<schedulerVO> list = schedulerSvc.selectschedulerList();
		List<TargetList> targetLists = new ArrayList<TargetList>();
		
		for(schedulerVO vo : list) {
			TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
			targetLists.add(targetList);
		}
		
		TargetList resultTarget = schedulerUtils.insertScheduleGroup(new Gson().fromJson(content, TargetList.class), targetLists);
		schedulerSvc.insertscheduler(resultTarget.getUniqueId(), "S", gson.toJson(resultTarget));
		
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
		
		schedulerVO vo = schedulerSvc.selectschedulerOne(targetList.getUniqueId());
		TargetList sourceList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
		
		targetList = schedulerUtils.updateGroup(targetList, sourceList);
		schedulerSvc.insertscheduler(targetList.getUniqueId(), "S", gson.toJson(targetList));
		response.getWriter().print(result);		
	}
	
	@RequestMapping(value = "/schedule/addTarget.do", method = RequestMethod.POST)
	public void addTarget(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("schedule/addTarget.do");
		
		boolean result = true;
	
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		ScheduleTarget target = gson.fromJson(content, ScheduleTarget.class);
		
		schedulerVO vo = schedulerSvc.selectschedulerOne(target.getTargetListId());
		TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
		
		String stepId = schedulerUtils.maxStepId(targetList);
		target.setStepId((stepId == null)?"STP_1":ZKeyUtil.getNextCodeValue(stepId));
		
		if(target.getpType().equals("command")){
			target.setMethod("");
		}else if(target.getpType().equals("http")){
						
		}
		
		targetList.addTargets(target);
		schedulerSvc.insertscheduler(targetList.getUniqueId(), "S", gson.toJson(targetList));
		
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
		
		List<schedulerVO> list = schedulerSvc.selectschedulerList();
		List<TargetList> targetLists = new ArrayList<TargetList>();
		
		for(schedulerVO vo : list) {
			TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
			targetLists.add(targetList);
		}
		
		for(TargetList targetList : targetLists){
			logger.info("targetList.getGroupId() equals scheduleTarget.getGroupId() -->" + targetList.getGroupId() + "::" + scheduleTarget.getGroupId());
			if(targetList.getGroupId().equals(scheduleTarget.getGroupId())){
				targetList = schedulerUtils.updateScheduleTargetInfo(targetList, scheduleTarget);
				schedulerSvc.insertscheduler(targetList.getUniqueId(), "S", gson.toJson(targetList));
				break;
			}
		}
		
		response.getWriter().println(true);
	}
}
