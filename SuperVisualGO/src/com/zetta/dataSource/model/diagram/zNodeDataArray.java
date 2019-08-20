package com.zetta.dataSource.model.diagram;

import java.util.List;

import com.zetta.dataSource.model.dataSource.Fields;

public class zNodeDataArray {
	private String key;
	private String category;
	private String text;
	private String eventType;
	private String eventDimension;
	private String item;
	private String loc;
	private boolean isGroup;
	private boolean isSubProcess;
	private int taskType;
	private String size;
	private String group;
	private String chartType;
	private String leftGroup;
	private String imgPath;
	private String parentKey;
	private String url;
	private List<NodeData> data;
	private List<Fields> fields;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventDimension() {
		return eventDimension;
	}
	public void setEventDimension(String eventDimension) {
		this.eventDimension = eventDimension;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public boolean isSubProcess() {
		return isSubProcess;
	}
	public void setSubProcess(boolean isSubProcess) {
		this.isSubProcess = isSubProcess;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public String getLeftGroup() {
		return leftGroup;
	}
	public void setLeftGroup(String leftGroup) {
		this.leftGroup = leftGroup;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	public List<NodeData> getData() {
		return data;
	}
	public void setData(List<NodeData> data) {
		this.data = data;
	}
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Fields> getFields() {
		return fields;
	}
	public void setFields(List<Fields> fields) {
		this.fields = fields;
	}
	public zNodeDataArray() {
		super();
		// TODO Auto-generated constructor stub
	}

}
