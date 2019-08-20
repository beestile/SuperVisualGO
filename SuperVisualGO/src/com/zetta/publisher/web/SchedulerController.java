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
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.zetta.publisher.conf.ZProperty;
import com.zetta.publisher.model.TargetList;
import com.zetta.publisher.util.ZFileUtil;
import com.zetta.scheduler.service.CreateSchedule;
import com.zetta.scheduler.service.DeleteSchedule;
import com.zetta.scheduler.service.SelectSchedule;
import com.zetta.scheduler.service.StartSchedule;
import com.zetta.scheduler.service.StopSchedule;
import com.zetta.scheduler.service.UpdateSchedule;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SchedulerController {

	@Autowired
	CreateSchedule createSchedule;
	
	@Autowired
	SelectSchedule selectSchedule;

	@Autowired
	DeleteSchedule deleteSchedule;

	@Autowired
	StartSchedule startSchedule;

	@Autowired
	StopSchedule stopSchedule;

	@Autowired
	UpdateSchedule updateSchedule;
	
	@Autowired
	ZProperty zProperty;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public void test(){
		
	}
	

	@RequestMapping(value = "/inputForm.do", method = RequestMethod.GET)
	public void inPutForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

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

		List<TargetList> list = selectSchedule.selectAllTargetList();

		for (TargetList targetList : list) {
			startSchedule.start(targetList);
			targetList.setStatus("run");
			updateSchedule.updateTargetListStatus(targetList);
			fileUpdate(targetList);
		}

		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄 정보를 Start 스케줄의 상태값을 stop에서
	 * run으로 변경
	 */
	@RequestMapping(value = "/start/selectedList.do", method = RequestMethod.GET)
	public void startSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {

		for (String uniqueId : uniqueIds) {
			TargetList targetList = selectSchedule.selectTargetList(uniqueId);

			startSchedule.start(targetList);
			targetList.setStatus("run");
			updateSchedule.updateTargetListStatus(targetList);
			fileUpdate(targetList);
		}

		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 한개의 스케줄아이디(uniqueId)를 받아서 한개의 스케줄 정보를 Start 스케줄의 상태값을 stop에서
	 * run으로 변경
	 */
	@RequestMapping(value = "/start/selectedOne.do", method = RequestMethod.GET)
	public void startSelectedOne(@RequestParam(value = "uniqueId") String uniqueId, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TargetList targets = selectSchedule.selectTargetList(uniqueId);

		startSchedule.start(targets);
		targets.setStatus("run");
		updateSchedule.updateTargetListStatus(targets);
		fileUpdate(targets);
		
		response.getWriter().print(true);
	}

	/*
	 * 모든 스케줄을 Start 스케줄의 상태값을 stop으로 변경
	 */
	@RequestMapping(value = "/stop/all.do", method = RequestMethod.GET)
	public void stopALL(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<TargetList> list = selectSchedule.selectAllTargetList();

		for (TargetList targetList : list) {
			stopSchedule.stop(targetList);
			targetList.setStatus("stop");
			updateSchedule.updateTargetListStatus(targetList);
			fileUpdate(targetList);
		}

		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄을 Stop
	 */
	@RequestMapping(value = "/stop/selectedList.do", method = RequestMethod.GET)
	public void stopSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {

		for (String uniqueId : uniqueIds) {

			TargetList targetList = selectSchedule.selectTargetList(uniqueId);
			stopSchedule.stop(targetList);
			targetList.setStatus("stop");
			updateSchedule.updateTargetListStatus(targetList);
			fileUpdate(targetList);
		}
		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 한개의 스케줄아이디(uniqueId)를 받아서 한개의 스케줄 정보를 stop 스케줄의 상태값을 run에서
	 * stop으로 변경
	 */
	@RequestMapping(value = "/stop/selectedOne.do", method = RequestMethod.GET)
	public void stopSelectedOne(@RequestParam(value = "uniqueId") String uniqueId, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TargetList targetList = selectSchedule.selectTargetList(uniqueId);
		stopSchedule.stop(targetList);
		targetList.setStatus("stop");
		updateSchedule.updateTargetListStatus(targetList);
		fileUpdate(targetList);

		response.getWriter().print(true);
	}
	
	private void fileUpdate(TargetList targetList){		
		// 기존파일 삭제
		deleteSchedule.deleteScheduleInfoFile(targetList);
		// 새로운파일 생성
		createSchedule.createScheduleInfoFile(targetList);
	}
	
	@RequestMapping(value = "/data/doExport.do", method = RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		List<TargetList> targetLists = selectSchedule.selectAllTargetList();
		 
		Gson gson = new Gson();
		
		// 전체 데이터를 한개의 JSON 형태의 데이터로 저장.. 엑셀데이터 형태로
		// 이렇게 한꺼번에 하면 양이 너무 많아질경우, java 'String'크기를 넘어갈 경우, 문제가 생길 수 있음.
		// Import형태가 어떻게 돼야 하는지 모르므로 일단 이렇게 개발하고 멈춤
		// Import형태때문에 데이터 구조가 복잡해저서 소스코드 양이 늘어 난다면 별도의 '서비스'클래스를 만들어서 로직 이관해야 함 
		String result = gson.toJson(targetLists);
		
		//ZFileUtil.stringToFile(zProperty.getExportRootPath() + "exportResult", result);		
		
		// 데이터가 제대로 파일로 저장 되었을 때만 true가 반환되도록 수정 해야함
		response.getWriter().print(result);
	}
}