package com.zetta.scheduler.trigger;

import com.zetta.publisher.model.TargetList;

/**
 * 트리거 인터페이스
 * 트리거 종류 : interval, 일간, 주간, 월간(미구현)
 * */
public interface ZTrigger {

	public void fire(TargetList targets);
}
