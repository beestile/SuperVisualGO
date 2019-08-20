package com.zetta.dataSource.model.excel;

import java.util.ArrayList;
import java.util.List;

public class zSheet {
	private int sheetId;
	private String sheetName;
	private List rows;
	
	public zSheet() {
		this.sheetId = -1;
		this.sheetName = "";
		this.rows = new ArrayList();
	}
	
	public zSheet(int sheetId, String sheetName, List rows) {
		this.sheetId = sheetId;
		this.sheetName = sheetName;
		this.rows = rows;
	}
	public int getSheetId() {
		return sheetId;
	}
	public void setSheetId(int sheetId) {
		this.sheetId = sheetId;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	
}
