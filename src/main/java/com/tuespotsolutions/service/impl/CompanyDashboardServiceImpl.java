package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.AssignedPackages;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobApplied;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.CompanyDashboard;
import com.tuespotsolutions.models.JobDetailPieChartDataForCompanyPanel;
import com.tuespotsolutions.repository.AssignedPackagesRepository;
import com.tuespotsolutions.repository.CandidateInboxNotificationRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.JobAppliedRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.CompanyDashboardService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class CompanyDashboardServiceImpl implements CompanyDashboardService {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private JobAppliedRepository appliedRepository;
	
	@Autowired
	private CandidateInboxNotificationRepository candidateInboxNotificationRepository;
	
	@Autowired
	private AssignedPackagesRepository assignedPackagesRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public CompanyDashboard getDashboardDataOfCompany(Long companyId) {
		Company company = this.companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company Not Exist"));
		long activeJobs = this.jobRepository.countByCompanyAndStatus(company, true);
		long inActiveJobs = this.jobRepository.countByCompanyAndStatus(company, false);
		long countOfAppliersOnJobs = this.appliedRepository.countByCompanyId(company.getId());
		long shairedLinksByCompany = this.candidateInboxNotificationRepository.countByCompanyId(company.getId());
		User user = this.userRepository.findByUserTypeAndTypeId(ConstantConfiguration.COMPANY,company.getId())
		.orElseThrow(() -> new ResourceNotFoundException("User Not Exist"));
		AssignedPackages findByUserId = assignedPackagesRepository.findByUserId(user.getId());
		
		CompanyDashboard companyDashboard = new CompanyDashboard();
		companyDashboard.setCountNotifications(shairedLinksByCompany);
		companyDashboard.setContOfJobApplier(countOfAppliersOnJobs);
		List<JobDetailPieChartDataForCompanyPanel> chartDataForCompanyPanels = new ArrayList<JobDetailPieChartDataForCompanyPanel>();
		JobDetailPieChartDataForCompanyPanel activeJobsData =  new JobDetailPieChartDataForCompanyPanel();
		activeJobsData.setName("Active Jobs");
		activeJobsData.setValue(activeJobs);
		activeJobsData.setColor("green");
		chartDataForCompanyPanels.add(activeJobsData);
		JobDetailPieChartDataForCompanyPanel inActiveJobData =  new JobDetailPieChartDataForCompanyPanel();
		inActiveJobData.setName("In Active Jobs");
		inActiveJobData.setValue(inActiveJobs);
		inActiveJobData.setColor("red");
		chartDataForCompanyPanels.add(inActiveJobData);
		companyDashboard.setJobPieChart(chartDataForCompanyPanels);;
//		companyDashboard.setActiveJob(activeJobs);
//		companyDashboard.setInActiveJob(inActiveJobs);
		companyDashboard.setPendingDays(findByUserId.getPendingDays());
		Integer pendingDaysPers = (findByUserId.getPendingDays() * 100)/findByUserId.getAssignedDays();
		companyDashboard.setPendingDaysPercentage(pendingDaysPers);
		return companyDashboard;
	}

}
