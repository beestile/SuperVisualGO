package com.zetta.dataSource.model.dataSource;

import java.util.List;
import java.util.Map;

public class DataSetMap {
	private String tableName;
	private List<Map[]> data;

	

	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public List<Map[]> getData() {
		return data;
	}



	public void setData(List<Map[]> data) {
		this.data = data;
	}



	public DataSetMap() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
}
