package com.zetta.zScheduler.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import com.zetta.zScheduler.model.CpuInfo;
import com.zetta.zScheduler.model.DiskInfo;
import com.zetta.zScheduler.model.MemInfo;
import com.zetta.zScheduler.model.OSInfo;
import com.zetta.zScheduler.model.SystemInfo;

public class SystemUtils {
	public zLogger logger = new zLogger(getClass());

	private Runtime runtime = Runtime.getRuntime();

	public SystemInfo getSystemInfo() {
		SystemInfo systemInfo = new SystemInfo();
		systemInfo.setDiskInfo(this.getDiskInfo());
		systemInfo.setMemInfo(this.getMemInfo());
		systemInfo.setOsInfo(this.getOsInfo());
		systemInfo.setCpuInfo(this.getCpuInfo());
		return systemInfo;
	}

	public String OSname() {
		return System.getProperty("os.name");
	}

	public String OSversion() {
		return System.getProperty("os.version");
	}

	public String OsArch() {
		return System.getProperty("os.arch");
	}

	public long totalMem() {
		return Runtime.getRuntime().totalMemory();
	}

	public long usedMem() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	public CpuInfo getCpuInfo() {

		OperatingSystemMXBean operatingSystemMXBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
		ThreadMXBean threadMXBean = java.lang.management.ManagementFactory.getThreadMXBean();
		RuntimeMXBean runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean();

		int cores = Runtime.getRuntime().availableProcessors();
		double loadAvg = operatingSystemMXBean.getSystemLoadAverage();
		
		int threadCount = threadMXBean.getThreadCount();
		long allThreadsCpuTime = 0L;

		long[] threadIds = threadMXBean.getAllThreadIds();
		for (int i = 0; i < threadIds.length; i++) {
			allThreadsCpuTime += threadMXBean.getThreadCpuTime(threadIds[i]);
		}

		long uptime = runtimeMXBean.getUptime();

		CpuInfo cpuInfo = new CpuInfo();
		cpuInfo.setAllThreadsCpuTime(allThreadsCpuTime);
		try {
			cpuInfo.setLoadAvg(getProcessCpuLoad());
		} catch (Exception e) {
			cpuInfo.setLoadAvg(0.1);
		}
		cpuInfo.setThreadCount(threadCount);
		cpuInfo.setUptime(uptime);
		cpuInfo.setCores(cores);

		return cpuInfo;
	}
	
	public static double getProcessCpuLoad() throws Exception {

	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty())     return Double.NaN;

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)      return Double.NaN;
	    // returns a percentage value with 1 decimal point precision
	    return ((int)(value * 1000) / 10.0);
	}

	public MemInfo getMemInfo() {
		MemInfo memInfo = new MemInfo();
		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		
		com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	    memInfo.setPhysicalCommitedVirtualMemorySize(Math.round(os.getCommittedVirtualMemorySize() / (1024 * 1024)));
	    memInfo.setPhysicalFreeMemory(Math.round(os.getFreePhysicalMemorySize() / (1024 * 1024)));
	    memInfo.setPhysicalFreeSwapSize(Math.round(os.getFreeSwapSpaceSize() / (1024 * 1024)));
	    memInfo.setPhysicalMemorySize(Math.round(os.getTotalPhysicalMemorySize() / (1024 * 1024)));

	    memInfo.setVmFreeMemory(Math.round(freeMemory / (1024 * 1024)));
	    memInfo.setVmAllocateMemory(Math.round(allocatedMemory / (1024 * 1024)));
	    memInfo.setVmMaxMemory(Math.round(maxMemory / (1024 * 1024)));
	    memInfo.setVmTotalFreeMeomory(Math.round((freeMemory + (maxMemory - allocatedMemory)) / (1024 * 1024)));
		return memInfo;
	}

	public OSInfo getOsInfo() {
		OSInfo osInfo = new OSInfo();
		osInfo.setArch(this.OsArch());
		osInfo.setName(this.OSname());
		osInfo.setVersion(this.OSversion());
		return osInfo;
	}

	public List<DiskInfo> getDiskInfo() {
		List<DiskInfo> diskInfos = new ArrayList<DiskInfo>();
		File[] roots = File.listRoots();

		/* For each filesystem root, print some info */
		for (File root : roots) {
			DiskInfo diskInfo = new DiskInfo();
			diskInfo.setFreeSpace(Math.round(root.getFreeSpace() / (1024 * 1024)));
			diskInfo.setRoot(root.getAbsolutePath());
			diskInfo.setTotalSpace(Math.round(root.getTotalSpace() / (1024 * 1024)));
			diskInfo.setUsableSpace(Math.round(root.getUsableSpace() / (1024 * 1024)));

			diskInfos.add(diskInfo);
		}
		return diskInfos;
	}
}
