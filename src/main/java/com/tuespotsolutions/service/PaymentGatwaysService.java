package com.tuespotsolutions.service;
import java.util.List;
import java.util.Map;

import com.tuespotsolutions.models.Payment;
import com.tuespotsolutions.models.PaymentGatewaysRequest;
import com.tuespotsolutions.models.TransactionResponse;

public interface PaymentGatwaysService {
	
	public PaymentGatewaysRequest postPaymentGatway(PaymentGatewaysRequest gatewaysRequest);
	public PaymentGatewaysRequest updatePaymentGatway(PaymentGatewaysRequest gatewaysRequest);
	public List<PaymentGatewaysRequest> findAllPaymentGatewayList();
	public PaymentGatewaysRequest findByIdPaymentGateway(Long id);
	public void deletePaymentGateway(Long id);
	public  Map<String, String> paymentPacakage(Payment payment);
	public List<PaymentGatewaysRequest> findEnabledPaymentGateways();
	public void updateStatusOfPayment(String response);
	public TransactionResponse findTransactionByPlanIdForCandidate(Long planId, Integer page, Integer size);
	public TransactionResponse findTransactionByPlanIdForCompany(Long planId, Integer page, Integer size);
	
}
