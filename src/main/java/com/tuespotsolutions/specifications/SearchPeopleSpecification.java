package com.tuespotsolutions.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tuespotsolutions.entity.Candidate;

@Component
public class SearchPeopleSpecification {
	
	public static Specification<Candidate> containsProfileHeadline(String profileHeadline){
		return ((root, crieteriaQuery,crieteriaBuilder)->{
			return crieteriaBuilder.like(root.get("profileHeadline"), "%"+profileHeadline+"%");
		});
	}

}
