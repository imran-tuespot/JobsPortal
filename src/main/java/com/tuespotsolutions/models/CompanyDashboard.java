package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class CompanyDashboard {

	List<JobDetailPieChartDataForCompanyPanel> jobPieChart;
	private Long contOfJobApplier;
	private Long countNotifications;
	private Integer  pendingDays;
	private Integer pendingDaysPercentage;
	
}
