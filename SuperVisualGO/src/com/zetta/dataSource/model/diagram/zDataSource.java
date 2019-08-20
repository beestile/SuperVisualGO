package com.zetta.dataSource.model.diagram;

public class zDataSource {
	private String sourceType;
	private String connName;
	private String subName;
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getConnName() {
		return connName;
	}
	public void setConnName(String connName) {
		this.connName = connName;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public zDataSource(String sourceType, String connName, String subName) {
		super();
		this.sourceType = sourceType;
		this.connName = connName;
		this.subName = subName;
	}
	public zDataSource() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	
}
