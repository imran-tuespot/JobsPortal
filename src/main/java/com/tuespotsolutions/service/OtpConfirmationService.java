package com.tuespotsolutions.service;

import com.tuespotsolutions.models.CandidateRegistrationResponse;
import com.tuespotsolutions.models.CompanyResponse;
import com.tuespotsolutions.models.OtpConfirmedRequest;

public interface OtpConfirmationService {

	public CompanyResponse otpConfirmation(OtpConfirmedRequest confirmedRequest);
	
	public CandidateRegistrationResponse otpConfirmationForCandidate(OtpConfirmedRequest confirmedRequest);
	
}
