package com.tuespotsolutions.models;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPeopleDetail {

	private Long id;
	private String name;
	private String email;
	private String mobileNumber;
	private String location;
	private Long pinCode;
	private String resume;
	
	private String city;
	private String district;
	private String state;
	private Integer cityId;
	private Integer districtId;
	private Integer stateId;
	private String panCard;
	private Date dateOfBirth;
	private boolean status;
	private String image;
	private String aboutYourSelf;
	private List<CandidateEducation> candidateEducation;
	private List<CandidateExperience> candidateExperience;
	private List<CandidateLanguage> candidateLanguage;
	private List<CandidateProjectDetail> candidateProjectDetail;
	private List<CandidateSalaryDetail> candidateSalary;
	private JobResponse jobResponse;
	
}
