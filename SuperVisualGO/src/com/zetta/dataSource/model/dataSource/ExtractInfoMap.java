package com.zetta.dataSource.model.dataSource;

import java.util.List;
import java.util.Map;

public class ExtractInfoMap {
	private String tableName;
	private List<Field> fields;
	private List<Map> data;
	
	

	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public List<Field> getFields() {
		return fields;
	}



	public void setFields(List<Field> fields) {
		this.fields = fields;
	}



	public List<Map> getData() {
		return data;
	}



	public void setData(List<Map> data) {
		this.data = data;
	}



	public ExtractInfoMap() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
}
