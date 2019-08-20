package com.zetta.dataSource.model.menu;

import java.util.List;

import com.zetta.dataSource.model.diagram.zLinkDataArray;

public class zMNMain {
	private String classes;
	private List<zMNNodeDataArray> nodeDataArray;
	private List<zLinkDataArray> linkDataArray;
	public String getClasses() {
		return classes;
	}
	
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public List<zMNNodeDataArray> getNodeDataArray() {
		return nodeDataArray;
	}
	public void setNodeDataArray(List<zMNNodeDataArray> nodeDataArray) {
		this.nodeDataArray = nodeDataArray;
	}
	public List<zLinkDataArray> getLinkDataArray() {
		return linkDataArray;
	}
	public void setLinkDataArray(List<zLinkDataArray> linkDataArray) {
		this.linkDataArray = linkDataArray;
	}
	
	public zMNMain() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public zMNMenu getMNNode(String id) {
		for (int i = 0; i < nodeDataArray.size(); i++){
			if(id.equals(nodeDataArray.get(i).getKey())){
				return nodeDataArray.get(i).getData();
			}
		}
		return null;
	}	
	
}


