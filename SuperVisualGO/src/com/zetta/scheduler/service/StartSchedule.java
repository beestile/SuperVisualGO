package com.zetta.scheduler.service;

import org.springframework.stereotype.Service;

import com.zetta.publisher.model.TargetList;

/**
 * 
 * 쿼츠를 통한 작업 스케줄 실행.
 * */
@Service
public interface StartSchedule {

	public void start(TargetList targets);
}
