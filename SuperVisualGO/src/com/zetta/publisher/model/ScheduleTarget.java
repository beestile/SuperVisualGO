package com.zetta.publisher.model;

/**
 * 데이터의 의미와 용도를 정의 해야 함.
 * */
public class ScheduleTarget {
	
	String targetListId;
	String targetListName;
	
	String groupId;
	String jobId;
	
	String stepId;

	String progPath;
	String parameter;
	
	String pType;
	String method;
	
	
	public String getTargetListName() {
		return targetListName;
	}
	public void setTargetListName(String targetListName) {
		this.targetListName = targetListName;
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
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getProgPath() {
		return progPath;
	}
	public void setProgPath(String progPath) {
		this.progPath = progPath;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getTargetListId() {
		return targetListId;
	}
	public void setTargetListId(String targetListId) {
		this.targetListId = targetListId;
	}
	public String getpType() {
		return pType;
	}
	public void setpType(String pType) {
		this.pType = pType;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
