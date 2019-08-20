package com.zetta.publisher.model;

public class BatchResult extends ScheduleTarget {

	public BatchResult(ScheduleTarget target){
		this.targetListId = target.getTargetListId();
		this.targetListName = target.getTargetListName();
		this.groupId = target.getGroupId();
		this.jobId = target.getJobId();
		this.parameter = target.getParameter();
		this.progPath = target.getProgPath();
		this.stepId = target.getStepId();
	}
	
	String currentStatus;
	String currentTime;
	long ordering;
	String errorMsg;
	
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public long getOrdering() {
		return ordering;
	}
	public void setOrdering(long ordering) {
		this.ordering = ordering;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
