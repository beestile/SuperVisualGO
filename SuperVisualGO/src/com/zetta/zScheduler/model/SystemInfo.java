package com.zetta.zScheduler.model;

import java.util.List;

public class SystemInfo {
	OSInfo osInfo;
	CpuInfo cpuInfo;
	MemInfo memInfo;
	List<DiskInfo> diskInfo;
	
	
	public OSInfo getOsInfo() {
		return osInfo;
	}
	public void setOsInfo(OSInfo osInfo) {
		this.osInfo = osInfo;
	}
	public MemInfo getMemInfo() {
		return memInfo;
	}
	public void setMemInfo(MemInfo memInfo) {
		this.memInfo = memInfo;
	}
	public List<DiskInfo> getDiskInfo() {
		return diskInfo;
	}
	public void setDiskInfo(List<DiskInfo> diskInfo) {
		this.diskInfo = diskInfo;
	}
	public CpuInfo getCpuInfo() {
		return cpuInfo;
	}
	public void setCpuInfo(CpuInfo cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	
}
