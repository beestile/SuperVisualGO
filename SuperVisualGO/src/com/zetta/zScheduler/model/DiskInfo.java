package com.zetta.zScheduler.model;

public class DiskInfo {
	String root;
	long totalSpace;
	long freeSpace;
	long usableSpace;
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public long getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}
	public long getFreeSpace() {
		return freeSpace;
	}
	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}
	public long getUsableSpace() {
		return usableSpace;
	}
	public void setUsableSpace(long usableSpace) {
		this.usableSpace = usableSpace;
	}
	
	
	
}
