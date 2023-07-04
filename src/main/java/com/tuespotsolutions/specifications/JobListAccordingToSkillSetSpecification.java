package com.tuespotsolutions.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.tuespotsolutions.entity.Job;

public class JobListAccordingToSkillSetSpecification {
	
	public static Specification<Job> containSkills(String skillSet){
		return ((root, crieteriaQuery, crieteriaBuilder)->{
			return crieteriaBuilder.like(root.get("skills"), "%"+skillSet+"%");
		});
	}
}
