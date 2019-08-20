package com.zetta.dataSource.model.menu;

import java.sql.Timestamp;

public class zMNMenu {
	private String menuId;
	private String menuTp; //폴더 페이지 구분 --f 폴더 , p 페이지 
	private String menuNm; //메뉴명
	private int sortSn;
	private String dc; //설명
	private String searchKeyword; //검색
	private String regDt;
	private String updtDt;
	public zMNMenu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getmenuTp() {
		return menuTp;
	}
	public void setmenuTp(String menuTp) {
		this.menuTp = menuTp;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public int getSortSn() {
		return sortSn;
	}
	public void setSortSn(int sortSn) {
		this.sortSn = sortSn;
	}
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUpdtDt() {
		return updtDt;
	}
	public void setUpdtDt(String updtDt) {
		this.updtDt = updtDt;
	}
	
}
