package com.zetta.zScheduler.model;

public class MemInfo {
	long physicalMemorySize;
    long physicalFreeMemory;
    long physicalFreeSwapSize;
    long physicalCommitedVirtualMemorySize;
    
	long vmFreeMemory;
	long vmAllocateMemory;
	long vmMaxMemory;
	long vmTotalFreeMeomory;
	public long getPhysicalMemorySize() {
		return physicalMemorySize;
	}
	public void setPhysicalMemorySize(long physicalMemorySize) {
		this.physicalMemorySize = physicalMemorySize;
	}
	public long getPhysicalFreeMemory() {
		return physicalFreeMemory;
	}
	public void setPhysicalFreeMemory(long physicalFreeMemory) {
		this.physicalFreeMemory = physicalFreeMemory;
	}
	public long getPhysicalFreeSwapSize() {
		return physicalFreeSwapSize;
	}
	public void setPhysicalFreeSwapSize(long physicalFreeSwapSize) {
		this.physicalFreeSwapSize = physicalFreeSwapSize;
	}
	public long getPhysicalCommitedVirtualMemorySize() {
		return physicalCommitedVirtualMemorySize;
	}
	public void setPhysicalCommitedVirtualMemorySize(long physicalCommitedVirtualMemorySize) {
		this.physicalCommitedVirtualMemorySize = physicalCommitedVirtualMemorySize;
	}
	public long getVmFreeMemory() {
		return vmFreeMemory;
	}
	public void setVmFreeMemory(long vmFreeMemory) {
		this.vmFreeMemory = vmFreeMemory;
	}
	public long getVmAllocateMemory() {
		return vmAllocateMemory;
	}
	public void setVmAllocateMemory(long vmAllocateMemory) {
		this.vmAllocateMemory = vmAllocateMemory;
	}
	public long getVmMaxMemory() {
		return vmMaxMemory;
	}
	public void setVmMaxMemory(long vmMaxMemory) {
		this.vmMaxMemory = vmMaxMemory;
	}
	public long getVmTotalFreeMeomory() {
		return vmTotalFreeMeomory;
	}
	public void setVmTotalFreeMeomory(long vmTotalFreeMeomory) {
		this.vmTotalFreeMeomory = vmTotalFreeMeomory;
	}

}
