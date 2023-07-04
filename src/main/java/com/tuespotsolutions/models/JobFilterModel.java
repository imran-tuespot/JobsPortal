package com.tuespotsolutions.models;

import java.util.List;

import lombok.Data;

@Data
public class JobFilterModel {

   long filterId;
   String filterName;
   List<JobFilterModelValue> filesValue;
   
	
}
