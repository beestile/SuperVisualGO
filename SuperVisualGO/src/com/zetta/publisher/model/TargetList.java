package com.zetta.publisher.model;

import java.util.ArrayList;
import java.util.List;

public class TargetList {
	
	private List<ScheduleTarget> targets;
	private String uniqueId;
	private String createdAt;
	private String scheduleName;
	
	private String triggerId;
	private String groupId;
	private String jobId;
	
	private String cycleCode;
	private String cycleTime;
	private String date; // view에서 넘겨 받는 데이터. view에서 넘겨받는 cycleTime과 date를 조합해서 cycleTime을 다시 만듦.
	
	private String useYN;
	
	private String status;

	public List<ScheduleTarget> getTargets() {
		if(targets == null) return new ArrayList<ScheduleTarget>();
		return targets;
	}

	public void setTargets(List<ScheduleTarget> targets) {
		this.targets = targets;
	}
	
	public void addTargets(ScheduleTarget target) {
		if(targets == null) targets = new ArrayList<ScheduleTarget>();
		targets.add(target);		
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getCycleCode() {
		return cycleCode;
	}

	public void setCycleCode(String cycleCode) {
		this.cycleCode = cycleCode;
	}

	public String getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(String cycleTime) {
		this.cycleTime = cycleTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUseYN() {
		return useYN;
	}

	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
