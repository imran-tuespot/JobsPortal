package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class PaymentToken {

    private String amount;
    private String currency;
    private String mtx;
    private String attempts;
    private String sub_accounts_id;
    private String id;
    private String entity;
    private String status;
	
}
