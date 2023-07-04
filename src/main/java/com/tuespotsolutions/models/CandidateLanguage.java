package com.tuespotsolutions.models;

import lombok.Data;

@Data
public class CandidateLanguage {

	private Long id;
	private String name;
	private String level;
	private boolean isRead;
	private boolean isWrite;
	private boolean isSpeak;
	private Long candidateId;
	
}
