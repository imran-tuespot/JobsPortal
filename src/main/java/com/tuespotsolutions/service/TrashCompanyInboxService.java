package com.tuespotsolutions.service;

import java.util.List;

import com.tuespotsolutions.models.TrashCompanyInboxDetail;

public interface TrashCompanyInboxService {

	public List<TrashCompanyInboxDetail> trashListCompanyNotifications(List<Long> notificationId);
	
	public  List<TrashCompanyInboxDetail> trashShortedListCompanyNotifications(List<Long> notificationId);

	public List<TrashCompanyInboxDetail> getTrashListCompanyNotificationListByCompanyId(long companyId);

	public void setSeenStatusOnNotifiation(long notificationId);

}
