package com.zetta.dataSource.model.diagram;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class zDMain {
	@SerializedName("class")
	private String classes;
	private String fileName;
	private boolean copiesArrays;
	private boolean copiesArrayObjects;
	private String linkFromPortIdProperty;
	private String linkToPortIdProperty;
	private zPosition modelData;  
	private List<zNodeDataArray> nodeDataArray;
	private List<zLinkDataArray> linkDataArray;
	
	public zDMain() {
		super();
		classes = "go.GraphLinksModel";
		copiesArrays = true;
		copiesArrayObjects = true;
		linkFromPortIdProperty = "fromPort";
		linkToPortIdProperty = "toPort";
		fileName = "";
	}
	public zDMain(String classes, boolean copiesArrays, boolean copiesArrayObjects, String linkFromPortIdProperty, String linkToPortIdProperty, zPosition modelData, List<zNodeDataArray> nodeDataArray, List<zLinkDataArray> linkDataArray) {
		super();
		this.classes = classes;
		this.copiesArrays = copiesArrays;
		this.copiesArrayObjects = copiesArrayObjects;
		this.linkFromPortIdProperty = linkFromPortIdProperty;
		this.linkToPortIdProperty = linkToPortIdProperty;
		this.modelData = modelData;
		this.nodeDataArray = nodeDataArray;
		this.linkDataArray = linkDataArray;
	}
	
	//부모파일이름을 보관한다.
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getclasses() {
		return classes;
	}
	public void setclasses(String classes) {
		this.classes = classes;
	}
	public boolean isCopiesArrays() {
		return copiesArrays;
	}
	public void setCopiesArrays(boolean copiesArrays) {
		this.copiesArrays = copiesArrays;
	}
	public boolean isCopiesArrayObjects() {
		return copiesArrayObjects;
	}
	public void setCopiesArrayObjects(boolean copiesArrayObjects) {
		this.copiesArrayObjects = copiesArrayObjects;
	}
	public String getLinkFromPortIdProperty() {
		return linkFromPortIdProperty;
	}
	public void setLinkFromPortIdProperty(String linkFromPortIdProperty) {
		this.linkFromPortIdProperty = linkFromPortIdProperty;
	}
	public String getLinkToPortIdProperty() {
		return linkToPortIdProperty;
	}
	public void setLinkToPortIdProperty(String linkToPortIdProperty) {
		this.linkToPortIdProperty = linkToPortIdProperty;
	}
	public zPosition getModelData() {
		return modelData;
	}
	public void setModelData(zPosition modelData) {
		this.modelData = modelData;
	}
	public List<zNodeDataArray> getNodeDataArray() {
		return nodeDataArray;
	}
	public void setNodeDataArray(List<zNodeDataArray> nodeDataArray) {
		this.nodeDataArray = nodeDataArray;
	}
	public List<zLinkDataArray> getLinkDataArray() {
		return linkDataArray;
	}
	public void setLinkDataArray(List<zLinkDataArray> linkDataArray) {
		this.linkDataArray = linkDataArray;
	}
	
	
	
}


