package com.zetta.dataSource.model.diagram;

import java.util.List;

public class zLinkDataArray {
	private String from;
	private String to;
	private String fromPort;
	private String toPort;
	private String category;
	private List points;

	
	public zLinkDataArray(String from, String to, String fromPort, String toPort, String category, List points) {
		super();
		this.from = from;
		this.to = to;
		this.fromPort = fromPort;
		this.toPort = toPort;
		this.category = category;
		this.points = points;
	}
	
	
	
	public zLinkDataArray() {
		// TODO Auto-generated constructor stub
	}



	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFromPort() {
		return fromPort;
	}
	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}
	public String getToPort() {
		return toPort;
	}
	public void setToPort(String toPort) {
		this.toPort = toPort;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List getPoints() {
		return points;
	}
	public void setPoints(List points) {
		this.points = points;
	}

	
		
	
		
}
