package com.zetta.dataSource.model.spreadJS;

public class SpreadColumnInfo {
	String id;
	String caption;
	String dataField;
	String dataType = null;
	boolean allowEditing = true;
	String width = "*";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public boolean isAllowEditing() {
		return allowEditing;
	}
	public void setAllowEditing(boolean allowEditing) {
		this.allowEditing = allowEditing;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public SpreadColumnInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
}
