package com.tuespotsolutions.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.entity.Roles;
import com.tuespotsolutions.repository.RolesRepository;
import com.tuespotsolutions.service.RolesService;
import com.tuespotsolutions.util.ConstantConfiguration;

@Service
public class RolesServiceImpl implements RolesService{

	@Autowired
	private RolesRepository rolesRepository;
	
	@Override
	public List<Roles> saveRoles() {
		Set<Roles> roles = new HashSet<Roles>();
		Roles roles1 = new Roles();
		roles1.setId(1L);
		roles1.setName(ConstantConfiguration.ROLE_COMPANY);
		roles.add(roles1);
		
		Roles roles2 = new Roles();
		roles2.setId(2L);
		roles2.setName(ConstantConfiguration.ROLE_CANDIDATE);
		roles.add(roles2);
		
		Roles roles3 = new Roles();
		roles3.setId(3L);
		roles3.setName(ConstantConfiguration.ROLE_ADMIN);
		roles.add(roles3);
		
		List<Roles> saveAll = this.rolesRepository.saveAll(roles);
		return saveAll;
	}

}
