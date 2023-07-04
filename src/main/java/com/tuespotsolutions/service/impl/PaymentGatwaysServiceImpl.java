package com.tuespotsolutions.service.impl;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.AssignedPackages;
import com.tuespotsolutions.entity.Packages;
import com.tuespotsolutions.entity.PaymentGateways;
import com.tuespotsolutions.entity.Transcation;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.models.Payment;
import com.tuespotsolutions.models.PaymentGatewaysRequest;
import com.tuespotsolutions.models.PaymentResponse;
import com.tuespotsolutions.models.TransactionResponse;
import com.tuespotsolutions.models.Transactions;
import com.tuespotsolutions.repository.AssignedPackagesRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.repository.PaymentGatwaysRespository;
import com.tuespotsolutions.repository.TranscationRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.PaymentGatwaysService;
import com.tuespotsolutions.util.ConstantConfiguration;
import com.tuespotsolutions.util.FileUpload;

@Service
public class PaymentGatwaysServiceImpl implements PaymentGatwaysService {

	@Autowired
	private PaymentGatwaysRespository paymentGatwaysRespository;

	@Autowired
	private TranscationRepository transcationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PackagesRepository packagesRepository;

	@Autowired
	private AssignedPackagesRepository assignedPackagesRepository;

	@Value("${file.upload.path}")
	private String fileUploadUrl;

	@Value("${file.get.url}")
	private String downloadResume;

	private static final DecimalFormat df = new DecimalFormat("0.00");

	@Override
	public PaymentGatewaysRequest postPaymentGatway(PaymentGatewaysRequest gatewaysRequest) {

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		String pattern = "MM/dd/yyyy HH:mm:ss";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date according to the chosen pattern
		DateFormat df = new SimpleDateFormat(pattern);
		String format = df.format(utilDate);
		Date nowDate = new Date();
		try {
			nowDate = timeStamp.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PaymentGateways paymentGateways = new PaymentGateways();
		paymentGateways.setPaymentGatewayUrl(gatewaysRequest.getPaymentGatewayUrl());
		paymentGateways.setCreatedOn(nowDate);
		paymentGateways.setModifiedOn(nowDate);
		paymentGateways.setPaymentGatwayName(gatewaysRequest.getPaymentGatwayName());
		if (gatewaysRequest.getPaymentGatwayLogo().contains("data:image")) {
			String uploadFile = FileUpload.uploadFile(gatewaysRequest.getPaymentGatwayLogo(), fileUploadUrl);
			paymentGateways.setPaymentGatwayLogo(uploadFile);
		} else {
			paymentGateways.setPaymentGatwayLogo("");
		}

		paymentGateways.setStatus(false);
		PaymentGateways save = this.paymentGatwaysRespository.save(paymentGateways);

		PaymentGatewaysRequest response = new PaymentGatewaysRequest();
		response.setId(save.getId());
		paymentGateways.setPaymentGatewayUrl(save.getPaymentGatewayUrl());
		response.setCreatedOn(save.getCreatedOn());
		response.setModifiedOn(save.getModifiedOn());
		response.setPaymentGatwayName(save.getPaymentGatwayName());
		response.setPaymentGatwayLogo(save.getPaymentGatwayLogo());
		response.setStatus(save.getStatus());
		return response;
	}

	@Override
	public PaymentGatewaysRequest updatePaymentGatway(PaymentGatewaysRequest gatewaysRequest) {

		PaymentGateways paymentGateways = this.paymentGatwaysRespository.findById(gatewaysRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Payment Gateway Not Foud !!"));

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		String pattern = "MM/dd/yyyy HH:mm:ss";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date according to the chosen pattern
		DateFormat df = new SimpleDateFormat(pattern);
		String format = df.format(utilDate);
		Date nowDate = new Date();
		try {
			nowDate = timeStamp.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		paymentGateways.setPaymentGatewayUrl(gatewaysRequest.getPaymentGatewayUrl());
		paymentGateways.setModifiedOn(nowDate);
		paymentGateways.setPaymentGatwayName(gatewaysRequest.getPaymentGatwayName());
		if (gatewaysRequest.getPaymentGatwayLogo().contains("data:image")) {
			String uploadFile = FileUpload.uploadFile(gatewaysRequest.getPaymentGatwayLogo(), fileUploadUrl);
			paymentGateways.setPaymentGatwayLogo(uploadFile);
		}
		paymentGateways.setStatus(gatewaysRequest.getStatus());
		PaymentGateways save = this.paymentGatwaysRespository.save(paymentGateways);

		PaymentGatewaysRequest response = new PaymentGatewaysRequest();
		response.setId(save.getId());
		response.setCreatedOn(save.getCreatedOn());
		response.setModifiedOn(save.getModifiedOn());
		paymentGateways.setPaymentGatewayUrl(save.getPaymentGatewayUrl());
		response.setPaymentGatwayName(save.getPaymentGatwayName());
		response.setPaymentGatwayLogo(save.getPaymentGatwayLogo());
		response.setStatus(save.getStatus());
		return response;
	}

	@Override
	public List<PaymentGatewaysRequest> findAllPaymentGatewayList() {
		List<PaymentGateways> findAll = this.paymentGatwaysRespository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		List<PaymentGatewaysRequest> responseList = new ArrayList<PaymentGatewaysRequest>();
		findAll.forEach(data -> {
			PaymentGatewaysRequest response = new PaymentGatewaysRequest();
			response.setId(data.getId());
			response.setCreatedOn(data.getCreatedOn());
			response.setModifiedOn(data.getModifiedOn());
			response.setPaymentGatewayUrl(data.getPaymentGatewayUrl());
			response.setPaymentGatwayName(data.getPaymentGatwayName());
			response.setPaymentGatwayLogo(downloadResume + data.getPaymentGatwayLogo());
			response.setStatus(data.getStatus());
			responseList.add(response);
		});
		return responseList;
	}

	@Override
	public PaymentGatewaysRequest findByIdPaymentGateway(Long id) {
		PaymentGateways gateways = this.paymentGatwaysRespository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment Gateway Not Foud !!"));
		PaymentGatewaysRequest response = new PaymentGatewaysRequest();
		response.setId(gateways.getId());
		response.setCreatedOn(gateways.getCreatedOn());
		response.setModifiedOn(gateways.getModifiedOn());
		response.setPaymentGatewayUrl(gateways.getPaymentGatewayUrl());
		response.setPaymentGatwayName(gateways.getPaymentGatwayName());
		response.setPaymentGatwayLogo(downloadResume + gateways.getPaymentGatwayLogo());
		response.setStatus(gateways.getStatus());
		return response;
	}

	@Override
	public void deletePaymentGateway(Long id) {
		PaymentGateways gateways = this.paymentGatwaysRespository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment Gateway Not Foud !!"));
		this.paymentGatwaysRespository.delete(gateways);
	}

	@Override
	public Map<String, String> paymentPacakage(Payment payment) {

		df.setRoundingMode(RoundingMode.DOWN);
		String format = df.format(payment.getAmount());

		String transctionId = "instantjob" + System.currentTimeMillis();
		User user = userRepository.findById(payment.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found !!"));

		Packages packages = packagesRepository.findById(payment.getPlaneId())
				.orElseThrow(() -> new ResourceNotFoundException("Package Not Found"));

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		String pattern = "MM/dd/yyyy HH:mm:ss";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date according to the chosen pattern
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		String currentDate = dateFormat.format(utilDate);
		Date nowDate = new Date();
		try {
			nowDate = timeStamp.parse(currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Transcation transcation = new Transcation();
		transcation.setModifiedOn(nowDate);
		transcation.setPlaneId(packages.getId());
		transcation.setTransactionId(transctionId);
		transcation.setType(user.getUserType());
		transcation.setUserId(user.getId());
		transcation.setCreatedOn(nowDate);
		Transcation save = this.transcationRepository.save(transcation);

		Map<String, String> response = new HashMap<String, String>();

		response.put("url",
				"http://localhost:1704/hello?amount=" + format + "&currency=INR&name=" + payment.getName()
						+ "&email_id=" + payment.getEmail() + "&contact_number=" + payment.getMobileNumber()
						+ "&transcation_id=" + transctionId + "&id=" + save.getId());
		return response;
	}

	@Override
	public List<PaymentGatewaysRequest> findEnabledPaymentGateways() {
		List<PaymentGateways> findByStatus = this.paymentGatwaysRespository.findByStatus(true);
		List<PaymentGatewaysRequest> responseList = new ArrayList<PaymentGatewaysRequest>();
		findByStatus.forEach(data -> {
			PaymentGatewaysRequest response = new PaymentGatewaysRequest();
			response.setId(data.getId());
			response.setCreatedOn(data.getCreatedOn());
			response.setModifiedOn(data.getModifiedOn());
			response.setPaymentGatewayUrl(data.getPaymentGatewayUrl());
			response.setPaymentGatwayName(data.getPaymentGatwayName());
			response.setPaymentGatwayLogo(downloadResume + data.getPaymentGatwayLogo());
			response.setStatus(data.getStatus());
			responseList.add(response);
		});
		return responseList;
	}

	@Override
	public void updateStatusOfPayment(String response) {
		Gson gson = new Gson();
		String substring = response.substring(5);
		System.out.println("substring : " + substring);
//		 String json = gson.toJson(substring);
		System.out.println("json : " + substring);
		PaymentResponse paymentResponse = gson.fromJson(substring, PaymentResponse.class);

		Transcation transcation = this.transcationRepository
				.findByTransactionId(paymentResponse.getPayment_token().getMtx())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Transaction Not Found with transcationId : " + paymentResponse.getPayment_token().getMtx()));

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		String pattern = "MM/dd/yyyy HH:mm:ss";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date according to the chosen pattern
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		String currentDate = dateFormat.format(utilDate);
		Date nowDate = new Date();
		try {
			nowDate = timeStamp.parse(currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		transcation.setModifiedOn(nowDate);
		transcation.setCustomerEmail(paymentResponse.getCustomer().getEmail_id());
		transcation.setTokenTranscationId(paymentResponse.getPayment_token().getId());
		transcation.setCustomerMobileNumber(paymentResponse.getCustomer().getContact_number());
		transcation.setPlaneAmount(paymentResponse.getPayment_token().getAmount());
		transcation.setStatus(paymentResponse.getPayment_token().getStatus());
		transcation.setTransactionResponse(substring);
		Transcation save = this.transcationRepository.save(transcation);


		Packages packages = this.packagesRepository.findById(save.getPlaneId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Package is not exist with packageId : " + save.getPlaneId()));
		User user = this.userRepository.findById(save.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"User is not exist with userId : " + save.getUserId()));
		
		
		 AssignedPackages findByUserId = this.assignedPackagesRepository.findByUserId(user.getId());
		
		 if(findByUserId != null) {
			 	findByUserId.setAssignDate(currentDate);
				Calendar c = Calendar.getInstance();
				c.setTime(new Date()); // Using today's date
				c.add(Calendar.DATE, packages.getDays());
				String endDate = timeStamp.format(c.getTime());
				findByUserId.setEndDate(endDate);
				findByUserId.setAssignedDays(packages.getDays());
				findByUserId.setPendingDays(packages.getDays());
				findByUserId.setPackageId(packages.getId());
				findByUserId.setStatus(true);
				findByUserId.setUserId(user.getId());
				findByUserId.setUserType(user.getUserType());
			    this.assignedPackagesRepository.save(findByUserId);
				
		 }else {
			 
				AssignedPackages assignedPackages = new AssignedPackages();
				assignedPackages.setAssignDate(currentDate);

				Calendar c = Calendar.getInstance();
				c.setTime(new Date()); // Using today's date
				c.add(Calendar.DATE, packages.getDays());
				String endDate = timeStamp.format(c.getTime());

				assignedPackages.setEndDate(endDate);
				assignedPackages.setAssignedDays(packages.getDays());
				assignedPackages.setPendingDays(packages.getDays());
				assignedPackages.setPackageId(packages.getId());
				assignedPackages.setStatus(true);
				assignedPackages.setUserId(user.getId());
				assignedPackages.setUserType(user.getUserType());
				this.assignedPackagesRepository.save(assignedPackages);
	
		 }

	}

	@Override
	public TransactionResponse findTransactionByPlanIdForCandidate(Long planId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transcation> findByPlaneId = this.transcationRepository.findByPlaneIdAndType(planId,ConstantConfiguration.CANDIDATE, pageable);
		List<Transcation> content = findByPlaneId.getContent();
		TransactionResponse  response = new TransactionResponse();
		List<Transactions> txtResponse = new ArrayList<Transactions>();
		content.forEach(data->{
			Transactions transactionResponse = new Transactions();
			transactionResponse.setId(data.getId());
			transactionResponse.setCreatedOn(data.getCreatedOn());
			transactionResponse.setCustomerEmail(data.getCustomerEmail());
			transactionResponse.setCustomerMobileNumber(data.getCustomerMobileNumber());
			transactionResponse.setModifiedOn(data.getModifiedOn());
			transactionResponse.setStatus(data.getStatus());
			transactionResponse.setTokenTranscationId(data.getTokenTranscationId());
			transactionResponse.setTransactionId(data.getTransactionId());
			txtResponse.add(transactionResponse);
		});
		
//		Collections.sort(txtResponse, new Comparator<Transactions>() {
//
//			@Override
//			public int compare(Transactions o1, Transactions o2) {
//
//				if (o1.getId() < o2.getId()) {
//					return 1;
//				} else if (o1.getId() == o2.getId()) {
//					return 0;
//				} else {
//					return -1;
//				}
//
//			}
//
//		});
		
		response.setTransactions(txtResponse);
		response.setLastPage(findByPlaneId.isLast());
		response.setPageNumber(findByPlaneId.getNumber());
		response.setPageSize(findByPlaneId.getSize());
		response.setTotalElement(findByPlaneId.getTotalElements());
		response.setTotalPages(findByPlaneId.getTotalPages());
		return response;
	}

	@Override
	public TransactionResponse findTransactionByPlanIdForCompany(Long planId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transcation> findByPlaneId = this.transcationRepository.findByPlaneIdAndType(planId,ConstantConfiguration.COMPANY, pageable);
		List<Transcation> content = findByPlaneId.getContent();
		TransactionResponse  response = new TransactionResponse();
		List<Transactions> txtResponse = new ArrayList<Transactions>();
		content.forEach(data->{
			Transactions transactionResponse = new Transactions();
			transactionResponse.setId(data.getId());
			transactionResponse.setCreatedOn(data.getCreatedOn());
			transactionResponse.setCustomerEmail(data.getCustomerEmail());
			transactionResponse.setCustomerMobileNumber(data.getCustomerMobileNumber());
			transactionResponse.setModifiedOn(data.getModifiedOn());
			transactionResponse.setStatus(data.getStatus());
			transactionResponse.setTokenTranscationId(data.getTokenTranscationId());
			transactionResponse.setTransactionId(data.getTransactionId());
			txtResponse.add(transactionResponse);
		});
		
//		Collections.sort(txtResponse, new Comparator<Transactions>() {
//
//			@Override
//			public int compare(Transactions o1, Transactions o2) {
//
//				if (o1.getId() < o2.getId()) {
//					return 1;
//				} else if (o1.getId() == o2.getId()) {
//					return 0;
//				} else {
//					return -1;
//				}
//
//			}
//
//		});
		
		response.setTransactions(txtResponse);
		response.setLastPage(findByPlaneId.isLast());
		response.setPageNumber(findByPlaneId.getNumber());
		response.setPageSize(findByPlaneId.getSize());
		response.setTotalElement(findByPlaneId.getTotalElements());
		response.setTotalPages(findByPlaneId.getTotalPages());
		return response;
	}



}
