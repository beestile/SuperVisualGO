package com.zetta.zScheduler.model;

public class CpuInfo {
	double loadAvg;
	double allThreadsCpuTime;
	double uptime;
	int threadCount;
	int cores;
	
	public int getCores() {
		return cores;
	}
	public void setCores(int cores) {
		this.cores = cores;
	}
	public double getLoadAvg() {
		return loadAvg;
	}
	public void setLoadAvg(double loadAvg) {
		this.loadAvg = loadAvg;
	}
	public double getAllThreadsCpuTime() {
		return allThreadsCpuTime;
	}
	public void setAllThreadsCpuTime(double allThreadsCpuTime) {
		this.allThreadsCpuTime = allThreadsCpuTime;
	}
	public double getUptime() {
		return uptime;
	}
	public void setUptime(double uptime) {
		this.uptime = uptime;
	}
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	
}
