package com.zetta.dataSource.model.dataSource;

import java.util.List;

public class HeaderInfo {
	private String tableName;
	private String[] header;

	
	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String[] getHeader() {
		return header;
	}


	public void setHeader(String[] header) {
		this.header = header;
	}


	public HeaderInfo() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
}
