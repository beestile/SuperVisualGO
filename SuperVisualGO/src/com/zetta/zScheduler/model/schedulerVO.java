package com.zetta.zScheduler.model;

import java.util.Date;

public class schedulerVO {
    private String OBJECT_ID;
    private String OBJECT_CATEGORY;
    private String OBJECT_JSON;
    private String OBJECT_CHILD_ID;
    private String OBJECT_TYPE;
    private Date REGDTTM;
    
    
	public String getOBJECT_CHILD_ID() {
		return OBJECT_CHILD_ID;
	}
	public void setOBJECT_CHILD_ID(String oBJECT_CHILD_ID) {
		OBJECT_CHILD_ID = oBJECT_CHILD_ID;
	}
	public String getOBJECT_TYPE() {
		return OBJECT_TYPE;
	}
	public void setOBJECT_TYPE(String oBJECT_TYPE) {
		OBJECT_TYPE = oBJECT_TYPE;
	}
	public String getOBJECT_ID() {
		return OBJECT_ID;
	}
	public void setOBJECT_ID(String oBJECT_ID) {
		OBJECT_ID = oBJECT_ID;
	}
	public String getOBJECT_CATEGORY() {
		return OBJECT_CATEGORY;
	}
	public void setOBJECT_CATEGORY(String oBJECT_CATEGORY) {
		OBJECT_CATEGORY = oBJECT_CATEGORY;
	}
	public String getOBJECT_JSON() {
		return OBJECT_JSON;
	}
	public void setOBJECT_JSON(String oBJECT_JSON) {
		OBJECT_JSON = oBJECT_JSON;
	}
	public Date getREGDTTM() {
		return REGDTTM;
	}
	public void setREGDTTM(Date rEGDTTM) {
		REGDTTM = rEGDTTM;
	}
    
  

	
}
