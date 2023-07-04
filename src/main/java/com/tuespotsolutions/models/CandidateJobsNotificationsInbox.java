package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CandidateJobsNotificationsInbox {
	
	private Long jobId;
	private String jobName;
	private String companyName;
	private String jobLocation;
	private String notificationActiveHours;

}
