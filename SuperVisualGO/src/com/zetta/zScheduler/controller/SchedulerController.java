package com.zetta.zScheduler.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.zetta.publisher.model.TargetList;
import com.zetta.zScheduler.utils.CommonConfig;
import com.zetta.zScheduler.utils.DataUtils;
import com.zetta.zScheduler.utils.zLogger;

@Controller
public class SchedulerController {
	public zLogger logger = new zLogger(getClass());
	public CommonConfig config = new CommonConfig();
	public DataUtils dataUtils = new DataUtils(config.getProperties("ZWORKINGROOT"), config.getProperties("QVXROOT"));
	
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
		logger.info("/inputForm.do");
		
		List<TargetList> list = dataUtils.getTargetLists();

		for (TargetList targetList : list) {
			dataUtils.start(targetList);
			targetList.setStatus("run");
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
		logger.info("/inputForm.do");
		
		for (String uniqueId : uniqueIds) {
			TargetList targetList = dataUtils.selectTargetList(uniqueId);

			dataUtils.start(targetList);
			targetList.setStatus("run");
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
		logger.info("/start/selectedOne.do");
		TargetList targets = dataUtils.selectTargetList(uniqueId);

		targets.setStatus("run");
		dataUtils.start(targets);		
		fileUpdate(targets);
		
		response.getWriter().print(true);
	}

	/*
	 * 모든 스케줄을 Start 스케줄의 상태값을 stop으로 변경
	 */
	@RequestMapping(value = "/stop/all.do", method = RequestMethod.GET)
	public void stopALL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/stop/all.do");
		
		List<TargetList> list = dataUtils.getTargetLists();

		for (TargetList targetList : list) {
			dataUtils.stop(targetList);
			targetList.setStatus("stop");
			fileUpdate(targetList);
		}

		response.getWriter().print(true);
	}

	/*
	 * View에서 선택된 복수개의 스케줄아이디(uniqueId)를 받아서 복수개의 스케줄을 Stop
	 */
	@RequestMapping(value = "/stop/selectedList.do", method = RequestMethod.GET)
	public void stopSelectedList(@RequestParam(value = "listUniqueID") List<String> uniqueIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("/stop/selectedList.do");
		
		for (String uniqueId : uniqueIds) {
			TargetList targetList = dataUtils.selectTargetList(uniqueId);
			dataUtils.stop(targetList);
			targetList.setStatus("stop");
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
		logger.info("/stop/selectedOne.do");
		TargetList targetList = dataUtils.selectTargetList(uniqueId);
		dataUtils.stop(targetList);
		targetList.setStatus("stop");
		fileUpdate(targetList);

		response.getWriter().print(true);
	}
	
	private void fileUpdate(TargetList targetList){		
		logger.info("function fileUpdate");
		// 기존파일 삭제
		dataUtils.deleteScheduleInfoFile(targetList.getUniqueId());
		// 새로운파일 생성
		dataUtils.createScheduleInfoFile(targetList);
	}
	
	@RequestMapping(value = "/data/doExport.do", method = RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response) throws IOException{
		logger.info("/data/doExport.do");
		List<TargetList> targetLists = dataUtils.getTargetLists();
		 
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