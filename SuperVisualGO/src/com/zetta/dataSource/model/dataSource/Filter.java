package com.zetta.dataSource.model.dataSource;
import java.util.List;

public class Filter {
	private String name;
	private String pysicalName;
	private String type;
	private List<String> values;
	private String where;
	
	
	
	public String getPysicalName() {
		return pysicalName;
	}
	public void setPysicalName(String pysicalName) {
		this.pysicalName = pysicalName;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public Filter() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
