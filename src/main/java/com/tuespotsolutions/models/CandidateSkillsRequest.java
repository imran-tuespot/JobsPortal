package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CandidateSkillsRequest {
	
	private Long id;
	private String skill;
	private Long candidateId;
	
}
