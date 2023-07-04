package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.CompanyInboxNotificationDetails;
import com.tuespotsolutions.models.CompanyInboxNotificationAll;
import com.tuespotsolutions.models.CompanyInboxNotificationAllWithPagination;
import com.tuespotsolutions.models.CompanyInboxPagination;

public interface CompanyInboxNotificationService {
	
	public CompanyInboxNotificationAllWithPagination getNotificationList(long companyId, Integer page, Integer size);
	
	public List<CompanyInboxNotificationAll> getLatestNotification(long companyId);
	
	public  CompanyInboxNotificationDetails getNotificationDetails(long notificationId);
	
	public void setSeenStatusOnNotifiation(long notificationId);
	
	public List<CompanyInboxNotificationAll> shortListCompanyInbox(List<Long> notificationId);
	public CompanyInboxNotificationAllWithPagination getShortListCompanyInbox(long companyId, Integer page, Integer size);
	public List<CompanyInboxNotificationAll> trashListCompanyInbox(List<Long> notificationId);
	public CompanyInboxNotificationAllWithPagination getTrashListCompanyInbox(long companyId, Integer page, Integer size);
	
	public CompanyInboxPagination getAllComapanyNotifications(long companyId,  Integer page, Integer size);

}
