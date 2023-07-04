package com.tuespotsolutions.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tuespotsolutions.entity.Job;

@Component
public class JobSpecification {
	
	
	public static Specification<Job> containsTitle(String jobTitle){
		return ((root, crieteriaQuery,crieteriaBuilder)->{
			return crieteriaBuilder.like(root.get("title"), "%"+jobTitle+"%");
		});
	}
	
	public static Specification<Job> containsLocation(String jobLocation){
		return ((root, crieteriaQuery,crieteriaBuilder)->{
			return crieteriaBuilder.like(root.get("location"),  "%"+jobLocation+"%");
		});
	}
	
	public static Specification<Job>  isStatus(boolean isStatus){
		return ((root, crieteriaQuery,crieteriaBuilder)->{
			return crieteriaBuilder.equal(root.get("status"), isStatus);
		});
	}

}
