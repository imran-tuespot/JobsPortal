package com.tuespotsolutions.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuespotsolutions.models.Payment;
import com.tuespotsolutions.models.PaymentGatewaysRequest;
import com.tuespotsolutions.models.TransactionResponse;
import com.tuespotsolutions.service.PaymentGatwaysService;

@RestController
@CrossOrigin("*")
@RequestMapping("paymentgateway")
public class PaymentGatwaysController {

	@Autowired
	private PaymentGatwaysService paymentGatwaysService;

	@PostMapping("/post")
	public ResponseEntity<?> postPaymentGateway(@RequestBody PaymentGatewaysRequest paymentGatewaysRequest) {
		PaymentGatewaysRequest postPaymentGatway = this.paymentGatwaysService.postPaymentGatway(paymentGatewaysRequest);
		return new ResponseEntity<PaymentGatewaysRequest>(postPaymentGatway, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<?> putPaymentGateway(@RequestBody PaymentGatewaysRequest paymentGatewaysRequest) {
		PaymentGatewaysRequest postPaymentGatway = this.paymentGatwaysService
				.updatePaymentGatway(paymentGatewaysRequest);
		return new ResponseEntity<PaymentGatewaysRequest>(postPaymentGatway, HttpStatus.OK);
	}

	@GetMapping("/get/all")
	public ResponseEntity<?> getAllPaymentGatewayList() {
		List<PaymentGatewaysRequest> findAllPaymentGatewayList = this.paymentGatwaysService.findAllPaymentGatewayList();
		return new ResponseEntity<List<PaymentGatewaysRequest>>(findAllPaymentGatewayList, HttpStatus.OK);
	}

	@GetMapping("/get/by")
	public ResponseEntity<?> getPaymentGateway(@RequestParam("paymentGatewayId") Long paymentGatewayId) {
		PaymentGatewaysRequest findByIdPaymentGateway = this.paymentGatwaysService
				.findByIdPaymentGateway(paymentGatewayId);
		return new ResponseEntity<PaymentGatewaysRequest>(findByIdPaymentGateway, HttpStatus.OK);
	}

	@DeleteMapping("/delete/by")
	public ResponseEntity<?> deletePaymentGateway(@RequestParam("paymentGatewayId") Long paymentGatewayId) {
		this.paymentGatwaysService.deletePaymentGateway(paymentGatewayId);
		Map<String, String> response = new HashMap<String, String>();
		response.put("status", "Deleted Successfully");
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
	}

	@PostMapping("/payment")
	public ResponseEntity<?> buyPackage(@RequestBody Payment payment) {
		Map<String, String> paymentPacakage = this.paymentGatwaysService.paymentPacakage(payment);
		return new ResponseEntity<Map<String, String>>(paymentPacakage, HttpStatus.OK);
	}

	@GetMapping("/enabled/paymentgateway/list")
	public ResponseEntity<?> findEnabledPaymentGateways() {
		List<PaymentGatewaysRequest> findEnabledPaymentGateways = this.paymentGatwaysService
				.findEnabledPaymentGateways();
		return new ResponseEntity<List<PaymentGatewaysRequest>>(findEnabledPaymentGateways, HttpStatus.OK);
	}

	@PutMapping("/update/payment/status")
	public void updatePaymentStatus(@RequestBody String body) {

		@SuppressWarnings("deprecation")
		String result = java.net.URLDecoder.decode(body);
		System.out.println("transcation_response : " + result);
		this.paymentGatwaysService.updateStatusOfPayment(result);
	}

	@GetMapping("/transcactions/of/candidate/by")
	public ResponseEntity<?> findByTransactionsByPlanIdOfCandidate(@RequestParam("planId") Long planId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		TransactionResponse findTransactionByPlanId = this.paymentGatwaysService.findTransactionByPlanIdForCandidate(planId,
				pageNumber, pageSize);
		return new ResponseEntity<TransactionResponse>(findTransactionByPlanId, HttpStatus.OK);
	}
	
	@GetMapping("/transcactions/of/company/by")
	public ResponseEntity<?> findByTransactionsByPlanIdForCompany(@RequestParam("planId") Long planId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		TransactionResponse findTransactionByPlanId = this.paymentGatwaysService.findTransactionByPlanIdForCompany(planId,
				pageNumber, pageSize);
		return new ResponseEntity<TransactionResponse>(findTransactionByPlanId, HttpStatus.OK);
	}
}
