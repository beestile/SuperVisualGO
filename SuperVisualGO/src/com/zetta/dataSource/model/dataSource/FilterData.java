package com.zetta.dataSource.model.dataSource;

import java.util.List;

public class FilterData {
	private String fieldName;
	private String filterMode;
	private String type;
	private List<String> lookups;
	private List<String> sort;
	
	public String getFieldName() {
		return fieldName;
	}



	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}



	public String getFilterMode() {
		return filterMode;
	}



	public void setFilterMode(String filterMode) {
		this.filterMode = filterMode;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public List<String> getLookups() {
		return lookups;
	}



	public void setLookups(List<String> lookups) {
		this.lookups = lookups;
	}



	public List<String> getSort() {
		return sort;
	}



	public void setSort(List<String> sort) {
		this.sort = sort;
	}
	
	public FilterData() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
}
