package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.ShortListCompanyInboxDetail;

public interface ShortListCompanyInboxService {

	public List<ShortListCompanyInboxDetail> shortListCompanyNotifications(List<Long> notificationId);
	
	public List<ShortListCompanyInboxDetail> getShortListCompanyNotificationListByCompanyId(long companyId);
	
	public void setSeenStatusOnNotifiation(long notificationId);
}
