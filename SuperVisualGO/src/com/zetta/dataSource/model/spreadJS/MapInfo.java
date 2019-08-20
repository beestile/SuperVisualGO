package com.zetta.dataSource.model.spreadJS;

public class MapInfo {
	int row;
	int col;
	String keyValue = "--";
	String value;
	String realValue = "--";
	String type;
	String tableName;
	String columnName;
	String mappingName;
	String saveInfo;
	String connectInfo;
	String figure;
	String key;
	String useYn;
	
	
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getSaveInfo() {
		return saveInfo;
	}
	public void setSaveInfo(String saveInfo) {
		this.saveInfo = saveInfo;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getRealValue() {
		return realValue;
	}
	public void setRealValue(String realValue) {
		this.realValue = realValue;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getMappingName() {
		return mappingName;
	}
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}
	public String getConnectInfo() {
		return connectInfo;
	}
	public void setConnectInfo(String connectInfo) {
		this.connectInfo = connectInfo;
	}
	public String getFigure() {
		return figure;
	}
	public void setFigure(String figure) {
		this.figure = figure;
	}
	public MapInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
