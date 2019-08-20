package com.zetta.dataSource.model.dataSource;

public class zODBC {
	private String odbcName;
	private String connStr;
	private String schemaName;
	
	
	
	public zODBC(String odbcName, String connStr, String schemaName) {
		super();
		this.odbcName = odbcName;
		this.connStr = connStr;
		this.schemaName = schemaName;
	}
	public zODBC() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getOdbcName() {
		return odbcName;
	}
	public void setOdbcName(String odbcName) {
		this.odbcName = odbcName;
	}
	public String getConnStr() {
		return connStr;
	}
	public void setConnStr(String connStr) {
		this.connStr = connStr;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
		
	
}
