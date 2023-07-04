package com.tuespotsolutions.service.impl;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.customexception.WrongOtpException;
import com.tuespotsolutions.email.service.EmailServiceImpl;
import com.tuespotsolutions.entity.AssignedPackages;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.Packages;
import com.tuespotsolutions.entity.RegistrationOtp;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.CandidateRegistrationResponse;
import com.tuespotsolutions.models.CompanyResponse;
import com.tuespotsolutions.models.OtpConfirmedRequest;
import com.tuespotsolutions.repository.AssignedPackagesRepository;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.repository.RegistrationOtpRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.OtpConfirmationService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class OtpConfirmationServiceImpl implements OtpConfirmationService {

	@Autowired
	private RegistrationOtpRepository registrationOtpRepository;
	
	@Autowired 
	private CompanyRepository companyRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Autowired
	private PackagesRepository packagesRepository;
	
	@Autowired
	private AssignedPackagesRepository assignedPackagesRepository;

	@Override
	public CompanyResponse otpConfirmation(OtpConfirmedRequest confirmedRequest) {
		RegistrationOtp otp = this.registrationOtpRepository.findById(confirmedRequest.getOtpId()).orElseThrow(
				() -> new ResourceNotFoundException("Resource Not Found with id : " + confirmedRequest.getOtpId()));
		CompanyResponse companyResponse = new CompanyResponse();
		if(otp.getOtp().equalsIgnoreCase(confirmedRequest.getOtp())){		
			Company company = this.companyRepository.findById(otp.getUserId()).orElseThrow(
					() -> new ResourceNotFoundException("Company Not Found with id : " +otp.getUserId()));
			
			company.setStatus(true);	
			Company save = this.companyRepository.save(company);	
			if(save.isStatus()) {
				
				User existUser = this.userRepository.findByUserTypeAndTypeId(ConstantConfiguration.COMPANY, save.getId()).orElseThrow(
						() -> new ResourceNotFoundException("User Not Found with UserId : " + save.getId()+" and User Type : "+ ConstantConfiguration.COMPANY));
				existUser.setStatus(true);
				
				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat  timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				String format = timeStamp.format(utilDate.getTime());
				
//				Packages packages = this.packagesRepository.findById(1L)
//						.orElseThrow(() -> new ResourceNotFoundException("Free Pacakge is not exist"));
//				
				
				 Optional<Packages> andDefaultPackage = this.packagesRepository.findByCompanyAndDefaultPackage(true, true);				 
				 if(!andDefaultPackage.isPresent()) {
					 
				 }else {
					 Packages packages = andDefaultPackage.get();
					 Calendar c = Calendar.getInstance();
						c.setTime(new Date()); // Using today's date
						c.add(Calendar.DATE, packages.getDays()); 
						String endDate = timeStamp.format(c.getTime());
						AssignedPackages assignedPackages = new AssignedPackages();
						assignedPackages.setAssignDate(format);
						assignedPackages.setAssignedDays(packages.getDays());
						assignedPackages.setEndDate(endDate);
						assignedPackages.setPackageId(packages.getId());
						assignedPackages.setPendingDays(packages.getDays());
						assignedPackages.setStatus(true);
						assignedPackages.setUserId(existUser.getId());
						assignedPackages.setUserType(ConstantConfiguration.COMPANY);
						this.assignedPackagesRepository.save(assignedPackages);
				 }
				 
				this.registrationOtpRepository.delete(otp);
				// send mail company registered.
				this.emailServiceImpl.sendSimpleMessage(existUser.getEmail(), "Regarding: Registration", "You are registered on Job Solutions as below username",existUser.getApplicantName(),existUser.getUsername(), existUser.getPassword());	
				companyResponse.setEmail(company.getEmail());
				companyResponse.setId(company.getId());
				companyResponse.setMobileNumber(company.getMobileNumber());
				companyResponse.setName(company.getName());
				
			}
		}else {
			throw new WrongOtpException("Wrong Otp : "+confirmedRequest.getOtp());
		}
		
		return companyResponse;
	}


	@Override
	public CandidateRegistrationResponse otpConfirmationForCandidate(OtpConfirmedRequest confirmedRequest) {
		RegistrationOtp otp = this.registrationOtpRepository.findById(confirmedRequest.getOtpId()).orElseThrow(
				() -> new ResourceNotFoundException("Resource Not Found with id : " + confirmedRequest.getOtpId()));
		CandidateRegistrationResponse candidateRegistrationResponse = new CandidateRegistrationResponse();
		if(otp.getOtp().equalsIgnoreCase(confirmedRequest.getOtp())){
			Candidate candidate = this.candidateRepository.findById(otp.getUserId()).orElseThrow(
					() -> new ResourceNotFoundException("Company Not Found with id : " +otp.getUserId()));
			
			candidate.setStatus(true);
			Candidate save = this.candidateRepository.save(candidate);
			if(save.isStatus()) {
				User existUser = this.userRepository.findByUserTypeAndTypeId(ConstantConfiguration.CANDIDATE, save.getId()).orElseThrow(
						() -> new ResourceNotFoundException("User Not Found with UserId : " + save.getId()+" and User Type : "+ ConstantConfiguration.COMPANY));
				existUser.setStatus(true);
				
				
				
				
				
				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat  timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				String format = timeStamp.format(utilDate.getTime());
				
			  Optional<Packages> findByCandidateAndDefaultPackage = this.packagesRepository.findByCandidateAndDefaultPackage(true, true);
					
			  if(!findByCandidateAndDefaultPackage.isPresent()) {
				 
			  }else {
				  Packages packages = findByCandidateAndDefaultPackage.get();
				  Calendar c = Calendar.getInstance();
					c.setTime(new Date()); // Using today's date
					c.add(Calendar.DATE, packages.getDays()); 
					String endDate = timeStamp.format(c.getTime());
					AssignedPackages assignedPackages = new AssignedPackages();
					assignedPackages.setAssignDate(format);
					assignedPackages.setAssignedDays(packages.getDays());
					assignedPackages.setEndDate(endDate);
					assignedPackages.setPackageId(packages.getId());
					assignedPackages.setPendingDays(packages.getDays());
					assignedPackages.setStatus(true);
					assignedPackages.setUserId(existUser.getId());
					assignedPackages.setUserType(ConstantConfiguration.CANDIDATE);
					this.assignedPackagesRepository.save(assignedPackages);
			  }
				
				
				this.registrationOtpRepository.delete(otp);
				this.emailServiceImpl.sendSimpleMessage(existUser.getEmail(), "Regarding: Registration", "You are registered on Job Solutions as below username",existUser.getApplicantName(),existUser.getUsername(), existUser.getPassword());	
				candidateRegistrationResponse.setEmail(candidate.getEmail());
				candidateRegistrationResponse.setId(candidate.getId());
				candidateRegistrationResponse.setMobileNumber(candidate.getMobileNumber());
				candidateRegistrationResponse.setName(candidate.getName());
			}
		}else {
			throw new WrongOtpException("Wrong Otp : "+confirmedRequest.getOtp());
		}
		return candidateRegistrationResponse;
	}

}
