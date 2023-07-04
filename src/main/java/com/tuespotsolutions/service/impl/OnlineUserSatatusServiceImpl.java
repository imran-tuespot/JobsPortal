package com.tuespotsolutions.service.impl;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.OnlinePeopleStatus;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.repository.OnlinePeopleStatusRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.service.OnlineUserSatatusService;

@Service
public class OnlineUserSatatusServiceImpl implements OnlineUserSatatusService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	OnlinePeopleStatusRepository onlinePeopleStatusRepository;

	@Override
	public Map<String, String> livePeopleStatus(Long userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found with id : " + userId));

		if (user.isStatus()) {

			if (user.getUserType().contains("CANDIDATE")) {
				OnlinePeopleStatus liveUser = this.onlinePeopleStatusRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found with id : " + userId));
				java.util.Date utilDate = new java.util.Date();
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				liveUser.setLastSeen(utilDate);
				onlinePeopleStatusRepository.save(liveUser);
			}
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = new HashedMap();
		map.put("status", "User Online");
		return map;
	}
}
