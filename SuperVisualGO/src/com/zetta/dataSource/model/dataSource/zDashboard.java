package com.zetta.dataSource.model.dataSource;

import java.util.ArrayList;
import java.util.List;

public class zDashboard {
	private List<zDashboardPanel> dashboardPanels = new ArrayList<zDashboardPanel>();

	public List<zDashboardPanel> getDashboardPanels() {
		return dashboardPanels;
	}

	public void setDashboardPanel(List<zDashboardPanel> dashboardPanels) {
		this.dashboardPanels = dashboardPanels;
	}

	public zDashboard() {
		super();
		// TODO Auto-generated constructor stub
	}
}
