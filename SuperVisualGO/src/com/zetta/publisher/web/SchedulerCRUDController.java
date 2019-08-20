package com.zetta.publisher.web;

import java.io.IOException;
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
import com.zetta.publisher.model.ScheduleTarget;
import com.zetta.publisher.model.TargetList;
import com.zetta.scheduler.service.CreateSchedule;
import com.zetta.scheduler.service.DeleteSchedule;
import com.zetta.scheduler.service.LoadSchedule;
import com.zetta.scheduler.service.SelectSchedule;
import com.zetta.scheduler.service.StopSchedule;
import com.zetta.scheduler.service.UpdateSchedule;

@Controller
public class SchedulerCRUDController {

	@Autowired
	CreateSchedule createSchedule;
	
	@Autowired
	SelectSchedule selectSchedule;

	@Autowired
	DeleteSchedule deleteSchedule;
	
	@Autowired
	StopSchedule stopSchedule;

	@Autowired
	UpdateSchedule updateSchedule;
	
	@Autowired
	LoadSchedule loadSchedule;
		
	@RequestMapping(value = "/schedule/load.do", method = RequestMethod.POST)
	@ResponseBody
	public void load(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String result = null;

		try {
			List<TargetList> list = selectSchedule.selectAllTargetList();
			Gson gson = new GsonBuilder().create();
			result = gson.toJson(list);
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/schedule/reLoad.do", method = RequestMethod.POST)
	@ResponseBody
	public void reLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		deleteSchedule.deleteAllSchedule();
		loadSchedule.initialLoad();
		
		response.getWriter().print(true);
	} 
	
	/*
	 * 개발중... 스케줄 전체삭제 기능인데 개발할 필요가 있는지 검토.
	 */
	@RequestMapping(value = "/delete/all.do", method = RequestMethod.GET)
	public void deleteALL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.getWriter().print(true);
	}
	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄을 Delete HSQL에서도 삭제, 파일에서도
	 * 삭제 --> 동시 삭제
	 */
	@RequestMapping(value = "/delete/selectedList.do", method = RequestMethod.GET)
	public void deleteSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {

		for (String uniqueId : uniqueIds) {
			TargetList targetList = selectSchedule.selectTargetList(uniqueId);
			
			// run상태이면 Stop
			if(targetList.getStatus().equals("run")){
				stopSchedule.stop(targetList);
			}
			// 1. file에서 삭제
			deleteSchedule.deleteScheduleInfoFile(targetList);
			// 2. DB에서 삭제
			deleteSchedule.deleteScheduleInfoMemory(targetList);
		}
		response.getWriter().print(true);
	}
	
	/*
	 * View에서 선택된 한개의 스케줄아이디(uniqueId)를 받아서 한개의 스케줄을 Delete HSQL에서도 삭제, 파일에서도 삭제
	 * --> 동시 삭제
	 */
	@RequestMapping(value = "/delete/selectedOne.do", method = RequestMethod.POST)
	public void deleteSchedule(@RequestParam(value = "uniqueId") String uniqueId, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TargetList targetList = selectSchedule.selectTargetList(uniqueId);
		// 1. file에서 삭제
		deleteSchedule.deleteScheduleInfoFile(targetList);
		// 2. DB에서 삭제
		deleteSchedule.deleteScheduleInfoMemory(targetList);
		response.getWriter().print(true);
	}
	
	// 2016.03.17. Add...
	@RequestMapping(value = "/deleteStep/selectedOne.do", method = RequestMethod.POST)
	public void deleteStep(
			@RequestParam(value = "targetListId") String targetListId,
			@RequestParam(value = "stepId") String stepId,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception{

		
		ScheduleTarget scheduleTarget = new ScheduleTarget();
		scheduleTarget.setTargetListId(targetListId);
		scheduleTarget.setStepId(stepId);
		
		deleteSchedule.deleteScheduleTarget(scheduleTarget);
		
		TargetList targetList = selectSchedule.selectTargetList(targetListId);	
		fileUpdate(targetList);
		
		response.getWriter().print(true);
	}
	
	@RequestMapping(value = "/schedule/addGroup.do", method = RequestMethod.POST)
	public void addGroup(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean result = true;
		
		createSchedule.insertScheduleGroup(new Gson().fromJson(content, TargetList.class));
		
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
		
		boolean result = true;
		
		Gson gson = new Gson();
		TargetList targetList = gson.fromJson(content, TargetList.class);
		
		updateSchedule.updateTargetListInfo(targetList);
		
		targetList = selectSchedule.selectTargetList(targetList.getUniqueId());		
		fileUpdate(targetList);
		
		response.getWriter().print(result);		
	}
	
	@RequestMapping(value = "/schedule/addTarget.do", method = RequestMethod.POST)
	public void addTarget(
			@RequestParam(value = "content", required = true) String content,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean result = true;
	
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		ScheduleTarget target = gson.fromJson(content, ScheduleTarget.class);
		createSchedule.insertScheduleTarget(target);
		
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
		System.out.println(content);
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		ScheduleTarget scheduleTarget = gson.fromJson(content, ScheduleTarget.class);
		
		updateSchedule.updateScheduleTargetInfo(scheduleTarget);
		
		TargetList targetList = selectSchedule.selectTargetList(scheduleTarget.getTargetListId());
		fileUpdate(targetList);
		
		response.getWriter().println(true);
	}
	
	
	
	private void fileUpdate(TargetList targetList){		
		// 기존파일 삭제
		deleteSchedule.deleteScheduleInfoFile(targetList);
		// 새로운파일 생성
		createSchedule.createScheduleInfoFile(targetList);
	}
}
