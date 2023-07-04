package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class CompanyInboxNotificationAllWithPagination {

	public List<CompanyInboxNotificationAll> allNotifications;
	private int pageNumber;
	private int pageSize;
	private long totalElement;
	private int totalPages;
	private boolean lastPage;
	
}
