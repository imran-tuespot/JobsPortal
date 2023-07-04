package com.tuespotsolutions.models;

import java.sql.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRegistrationResponse {

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
	private CandidateEducation candidateEducation;
	private CandidateExperience candidateExperience;
	private CandidateLanguage candidateLanguage;
	private CandidateProjectDetail candidateProjectDetail;
	private CandidateSalaryDetail candidateSalary;
	
}
