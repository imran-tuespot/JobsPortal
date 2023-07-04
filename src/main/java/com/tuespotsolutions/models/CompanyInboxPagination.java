package com.tuespotsolutions.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInboxPagination {
	
	CompanyInboxNotificationAllWithPagination allNotifications;
	CompanyInboxShortListWithPagination shortList;
	CompanyInboxTrashListWithPagination trashList;
	
}
