package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class CandidateInboxShortListNotification {

	private List<CandidateInboxReponseShortlisted> candidateInboxReponseShortlisteds;
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
	
}
