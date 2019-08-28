package com.zetta.zScheduler.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zetta.common.utils.zLogger;
import com.zetta.publisher.model.TargetList;
import com.zetta.zScheduler.model.schedulerVO;
import com.zetta.zScheduler.utils.SchedulerUtils;

@Controller
public class SchedulerController {
	
	@Autowired
    private schedulerSvc schedulerSvc;
	
	public zLogger logger = new zLogger(getClass());
	public SchedulerUtils schedulerUtils = new SchedulerUtils();
	
	@RequestMapping(value = "/inputForm.do", method = RequestMethod.GET)
	public void inPutForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/inputForm.do");
		ModelAndView mav = new ModelAndView();

		mav.addObject("jsp", "views/inputForm");
		mav.setViewName("mainViewJsp");
		response.getWriter().print(true);
	}

	/*
	 * 모든 스케줄을 Start 스케줄의 상태값을 run으로 변경
	 */
	@RequestMapping(value = "/start/all.do", method = RequestMethod.GET)
	public void startALL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/start/all.do");
		
		try {
			Gson gson = new GsonBuilder().create();
			List<schedulerVO> list = schedulerSvc.selectschedulerList();
			
			for(schedulerVO vo : list) {
				TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
				targetList.setStatus("run");
				
				schedulerUtils.start(targetList);
				schedulerSvc.insertscheduler(vo.getOBJECT_ID(), "S", gson.toJson(targetList));
			}
			
			response.getWriter().print(true);
		} catch (IOException e) {
			e.printStackTrace();
			response.getWriter().print(false);
		}
	}

	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄 정보를 Start 스케줄의 상태값을 stop에서
	 * run으로 변경
	 */
	@RequestMapping(value = "/start/selectedList.do", method = RequestMethod.GET)
	public void startSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/inputForm.do");
		
		Gson gson = new GsonBuilder().create();
		for (String uniqueId : uniqueIds) {
			schedulerVO vo = schedulerSvc.selectschedulerOne(uniqueId);
			TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);

			targetList.setStatus("run");
			schedulerUtils.start(targetList);		

			schedulerSvc.insertscheduler(uniqueId, "S", gson.toJson(targetList));
		}

		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 한개의 스케줄아이디(uniqueId)를 받아서 한개의 스케줄 정보를 Start 스케줄의 상태값을 stop에서
	 * run으로 변경
	 */
	@RequestMapping(value = "/start/selectedOne.do", method = RequestMethod.GET)
	public void startSelectedOne(@RequestParam(value = "uniqueId") String uniqueId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/start/selectedOne.do");
		
		Gson gson = new GsonBuilder().create();
		schedulerVO vo = schedulerSvc.selectschedulerOne(uniqueId);
		TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);

		targetList.setStatus("run");
		schedulerUtils.start(targetList);		

		schedulerSvc.insertscheduler(uniqueId, "S", gson.toJson(targetList));
		response.getWriter().print(true);
	}

	/*
	 * 모든 스케줄을 Start 스케줄의 상태값을 stop으로 변경
	 */
	@RequestMapping(value = "/stop/all.do", method = RequestMethod.GET)
	public void stopALL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/stop/all.do");
		
		try {
			Gson gson = new GsonBuilder().create();
			List<schedulerVO> list = schedulerSvc.selectschedulerList();
			
			for(schedulerVO vo : list) {
				TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);
				targetList.setStatus("stop");
				
				schedulerUtils.stop(targetList);
				schedulerSvc.insertscheduler(vo.getOBJECT_ID(), "S", gson.toJson(targetList));
			}
			
			response.getWriter().print(true);
		} catch (IOException e) {
			e.printStackTrace();
			response.getWriter().print(false);
		}
	}

	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄을 Stop
	 */
	@RequestMapping(value = "/stop/selectedList.do", method = RequestMethod.GET)
	public void stopSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/stop/selectedList.do");
		
		Gson gson = new GsonBuilder().create();
		for (String uniqueId : uniqueIds) {
			schedulerVO vo = schedulerSvc.selectschedulerOne(uniqueId);
			TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);

			targetList.setStatus("stop");
			schedulerUtils.stop(targetList);		

			schedulerSvc.insertscheduler(uniqueId, "S", gson.toJson(targetList));
		}

		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 한개의 스케줄아이디(uniqueId)를 받아서 한개의 스케줄 정보를 stop 스케줄의 상태값을 run에서
	 * stop으로 변경
	 */
	@RequestMapping(value = "/stop/selectedOne.do", method = RequestMethod.GET)
	public void stopSelectedOne(@RequestParam(value = "uniqueId") String uniqueId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/stop/selectedOne.do");
		
		Gson gson = new GsonBuilder().create();
		schedulerVO vo = schedulerSvc.selectschedulerOne(uniqueId);
		TargetList targetList = gson.fromJson(vo.getOBJECT_JSON(), TargetList.class);

		targetList.setStatus("stop");
		schedulerUtils.stop(targetList);		

		schedulerSvc.insertscheduler(uniqueId, "S", gson.toJson(targetList));
		response.getWriter().print(true);
	}
}