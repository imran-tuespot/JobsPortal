package com.tuespotsolutions.models;

import java.sql.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequest {
	
	
	private Long id;
	@NotEmpty(message = "Candidate Name field is required")
	private String name;
	
	@NotEmpty(message = "Username field is required")
	private String userName;
	
	@Email(message = "Email address is not valid")
	@NotEmpty(message = "Email field is required")
	private String email;
	
	
	@Size(min = 3, message = "Password must be atleast minimum of 3 characters")
	private String password;
	
	@NotEmpty(message = "Mobile Number field is required")
	@Size(min = 10, max = 10 , message = "Mobile number must contain 10 digits")
	private String mobileNumber;
	
	@NotEmpty(message = "Prifile Headline Field is required")
	private String profileHeadline;
	
	private String address;
	private Integer cityId;
	private Integer districtId;
	private Integer stateId;
	private Long pinCode;
	private String panCard;
	private String resume;
	private Date dateOfBirth;
	private Date createdOn;
	private Date modifiedOn;
	private boolean status;
	@NotEmpty(message = "Image field is required")
	private String image;
	
	@NotEmpty(message = "About Your field is required")
	private String aboutYourSelf;
	
	//education
	private String education;
	private String university;
	private String course;
	private String specialization;
	private String courseType;
	private String courseDuration;
	private String gradingSystem;
	private Double marks;
	
	// experience
	private boolean current;
	private String employmentType;
	private String companyName;
	private String designation;
	private Date joingDate;
	private Date endDate;
	private String ctc;
	private String jobProfile;
	
	//language
	private String language;
	private String level;
	private boolean isRead;
	private boolean isWrite;
	private boolean isSpeak;
	
	//project
	private String title;
	private String client;
	private boolean projectStatus;
	private Date projectStartDate;
	private Date projectEndDate;
	private String description;
	private String location;
	private String natureOfEmployement;
	private Integer teamSize;
	private String role;
	private String roleDescription;
	private String skillSet;
	
	//salary
	private String currentCtc;
	private String expectedCtc;
	private String noticePeriod;
	

}
