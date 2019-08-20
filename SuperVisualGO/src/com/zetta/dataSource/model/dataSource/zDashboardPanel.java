package com.zetta.dataSource.model.dataSource;

public class zDashboardPanel {
	private String id;
	private String text;
	private zDashboardNode node;
	private String type;
	private String url;
	private boolean hasProcess;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public zDashboardNode getNode() {
		return node;
	}
	public void setNode(zDashboardNode node) {
		this.node = node;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean getHasProcess() {
		return hasProcess;
	}
	public void setHasProcess(boolean hasProcess) {
		this.hasProcess = hasProcess;
	}
	public zDashboardPanel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
