package com.zetta.dataSource.model.dataSource;

public class zDB {
	private String conName;
	private String driverName;
	private String connectionURL;
	private String userId;
	private String passWd;
	private String dbListQuery;
	private String tableListQuery;
	
	
	public String getDbListQuery() {
		return dbListQuery;
	}
	public void setDbListQuery(String dbListQuery) {
		this.dbListQuery = dbListQuery;
	}
	public String getTableListQuery() {
		return tableListQuery;
	}
	public void setTableListQuery(String tableListQuery) {
		this.tableListQuery = tableListQuery;
	}
	public String getConName() {
		return conName;
	}
	public void setConName(String conName) {
		this.conName = conName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getConnectionURL() {
		return connectionURL;
	}
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassWd() {
		return passWd;
	}
	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}
	public zDB() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
