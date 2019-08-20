package com.zetta.dataSource.model.excel;

public class zTableInfo {
	private String connName;
	private String subName;
	private String schema;
	private String infoType;
	private String colLength;
	private String colInfo;
	private String colName;
	private String colType;
	private String colNullable;
	
	public zTableInfo(){
		
	};
	
	public zTableInfo(String connName, String subName, String schema, String infoType, String colLength, String colInfo, String colName, String colType, String colNullable) {
		super();
		this.connName = connName;
		this.subName = subName;
		this.schema = schema;
		this.infoType = infoType;
		this.colLength = colLength;
		this.colInfo = colInfo;
		this.colName = colName;
		this.colType = colType;
		this.colNullable = colNullable;
	}
	public String getConnName() {
		return connName;
	}
	public void setConnName(String connName) {
		this.connName = connName;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getColLength() {
		return colLength;
	}
	public void setColLength(String colLength) {
		this.colLength = colLength;
	}
	public String getColInfo() {
		return colInfo;
	}
	public void setColInfo(String colInfo) {
		this.colInfo = colInfo;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getColNullable() {
		return colNullable;
	}
	public void setColNullable(String colNullable) {
		this.colNullable = colNullable;
	}
	
	//["portal","PTL_TB_ADVENCED_REPORT","dbo","BASE TABLE","10","","BI_REG_DT","varchar","YES"]
	
	
}
