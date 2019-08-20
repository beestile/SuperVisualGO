package com.zetta.dataSource.model.dataSource;

import java.util.List;
import java.util.Map;

public class ExtractInfo {
	private String tableName;
	private List<Field> fields;
	private List<String[]> data;

	
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


	public List<String[]> getData() {
		return data;
	}


	public void setData(List<String[]> data) {
		this.data = data;
	}

	public ExtractInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
}
