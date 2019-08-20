package com.zetta.dataSource.model.dataSource;

import java.util.List;

public class DataSet {
	private String tableName;
	private List<String[]> data;

	

	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public List<String[]> getData() {
		return data;
	}



	public void setData(List<String[]> data) {
		this.data = data;
	}



	public DataSet() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
}
