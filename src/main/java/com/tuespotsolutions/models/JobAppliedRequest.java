package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class JobAppliedRequest {
	
	private Long jobId;
	private Long candidateId;
	
}
