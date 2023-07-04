package com.tuespotsolutions.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.NullFieldException;
import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Candidate;
import com.tuespotsolutions.entity.CandidateSkills;
import com.tuespotsolutions.entity.City;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.FilterValues;
import com.tuespotsolutions.entity.Filters;
import com.tuespotsolutions.entity.JobFilter;
import com.tuespotsolutions.entity.JobFilterValues;
import com.tuespotsolutions.entity.JobWorkMode;
import com.tuespotsolutions.models.CityResponse;
import com.tuespotsolutions.models.CompanyResponse;
import com.tuespotsolutions.models.DepartmentFilter;
import com.tuespotsolutions.models.FilterValueResponseForPostJob;
import com.tuespotsolutions.models.FiltersListForJobPost;
import com.tuespotsolutions.models.Job;
import com.tuespotsolutions.models.JobCategoryFilters;
import com.tuespotsolutions.models.JobFilterModel;
import com.tuespotsolutions.models.JobFilterWithValues;
import com.tuespotsolutions.models.JobResponse;
import com.tuespotsolutions.models.JobResponseWithPagination;
import com.tuespotsolutions.models.JobSearchFilters;
import com.tuespotsolutions.models.LocationFilters;
import com.tuespotsolutions.models.WorkModeFilter;
import com.tuespotsolutions.repository.CandidateRepository;
import com.tuespotsolutions.repository.CandidateSkillsRepository;
import com.tuespotsolutions.repository.CityRepository;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.FilterRepository;
import com.tuespotsolutions.repository.FilterValueRepository;
import com.tuespotsolutions.repository.JobFilterRepository;
import com.tuespotsolutions.repository.JobFilterValuesRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.repository.JobWorkModeRepository;
import com.tuespotsolutions.service.JobService;
import com.tuespotsolutions.specifications.JobListAccordingToSkillSetSpecification;
import com.tuespotsolutions.specifications.JobSpecification;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	FilterValueRepository filterValueRepository;

	@Autowired
	FilterRepository filterRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	CandidateSkillsRepository candidateSkillsRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	JobWorkModeRepository jobWorkModeRepository;

	@Autowired
	JobFilterRepository jobFilterRepository;

	@Autowired
	JobFilterValuesRepository jobFilterValuesRepository;

	@Value("${file.get.url}")
	private String jobLogoUrl;

	@Override
	public JobResponse saveJob(Job job) {

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		String sqlDate = timeStamp.format(utilDate.getTime());
		com.tuespotsolutions.entity.Job jobs = new com.tuespotsolutions.entity.Job();
		jobs.setTitle(job.getTitle());
		if (job.getCompanyId() != null && job.getCompanyId() + "" != "") {
			Company company = this.companyRepository.findById(job.getCompanyId()).orElseThrow(
					() -> new ResourceNotFoundException("Company is not exist with companyId : " + job.getCompanyId()));
			jobs.setCompany(company);
		} else {
			throw new NullFieldException("Company field must be required");
		}

		jobs.setCreateOn(sqlDate);
		jobs.setDescription(job.getDescription());
		jobs.setExperience(job.getExperience());
		jobs.setJobType(job.getJobType());
		jobs.setLocation(job.getLocation());
		jobs.setModifiedOn(sqlDate);
		jobs.setSkills(job.getSkills());
		jobs.setStatus(true);
		jobs.setDepartment(job.getDepartment());
		com.tuespotsolutions.entity.Job save = this.jobRepository.save(jobs);

		job.getFilterWithValues().forEach(filter -> {
			JobFilter saveFilter = new JobFilter();
			saveFilter.setJob(save);
			Filters filters = this.filterRepository.findById(filter.getFilterId()).orElseThrow(
					() -> new ResourceNotFoundException("Filter not fount with Id : " + filter.getFilterId()));
			saveFilter.setFilter(filters);

			FilterValues filterValues = this.filterValueRepository.findById(filter.getFilterValueId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Filter Value not fount with Id : " + filter.getFilterValueId()));
			saveFilter.setFilterValue(filterValues);
			
			this.jobFilterRepository.save(saveFilter);
		});


		JobResponse jobResponse = new JobResponse();
		jobResponse.setTitle(save.getTitle());
		jobResponse.setId(save.getId());
		Company company2 = save.getCompany();
		CompanyResponse companyResponse = new CompanyResponse();
		companyResponse.setName(company2.getName());
		companyResponse.setId(company2.getId());
		companyResponse.setMobileNumber(company2.getMobileNumber());
		companyResponse.setEmail(company2.getMobileNumber());
		jobResponse.setCompany(companyResponse);
		jobResponse.setDescription(save.getDescription());
		jobResponse.setExperience(save.getExperience());
		jobResponse.setJobType(save.getJobType());
		jobResponse.setLocation(save.getLocation());
		jobResponse.setSkills(save.getSkills());
		jobResponse.setStatus(save.isStatus());
		jobResponse.setWorkMode(sqlDate);
		jobResponse.setDepartment(save.getDepartment());
		return jobResponse;
	}

	@Override
	public JobResponse updateJob(Job job) {
		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		String sqlDate = timeStamp.format(utilDate.getTime());

		com.tuespotsolutions.entity.Job jobs = this.jobRepository.findById(job.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Job is not exist with jobId : " + job.getId()));
		jobs.setTitle(job.getTitle());
		Company company = this.companyRepository.findById(job.getCompanyId()).orElseThrow(
				() -> new ResourceNotFoundException("Company is not exist with companyId : " + job.getCompanyId()));
		jobs.setCompany(company);
		jobs.setDescription(job.getDescription());
		jobs.setExperience(job.getExperience());
		jobs.setJobType(job.getJobType());
		jobs.setLocation(job.getLocation());
		jobs.setModifiedOn(sqlDate);
		jobs.setSkills(job.getSkills());
		jobs.setStatus(job.isStatus());
		jobs.setDepartment(job.getDepartment());
		com.tuespotsolutions.entity.Job save = this.jobRepository.save(jobs);

		JobResponse jobResponse = new JobResponse();
		jobResponse.setTitle(save.getTitle());
		jobResponse.setId(save.getId());
		Company company2 = save.getCompany();
		CompanyResponse companyResponse = new CompanyResponse();
		companyResponse.setName(company2.getName());
		companyResponse.setId(company2.getId());
		companyResponse.setMobileNumber(company2.getMobileNumber());
		companyResponse.setEmail(company2.getMobileNumber());
		jobResponse.setCompany(companyResponse);
		jobResponse.setDescription(save.getDescription());
		jobResponse.setExperience(save.getExperience());
		jobResponse.setJobType(save.getJobType());
		jobResponse.setLocation(save.getLocation());
		jobResponse.setSkills(save.getSkills());
		jobResponse.setStatus(save.isStatus());
		jobResponse.setDepartment(save.getDepartment());
		return jobResponse;
	}

	@Override
	public JobResponseWithPagination getJobsByComany(Integer page, Integer size, Long companyId) {
		Pageable pageable = PageRequest.of(page, size);

		Company company = this.companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company is not exist with companyId : " + companyId));
		Page<com.tuespotsolutions.entity.Job> findAll = this.jobRepository.findByCompany(pageable, company);

		List<com.tuespotsolutions.entity.Job> content = findAll.getContent();

		List<JobResponse> jobResponses = new ArrayList<JobResponse>();
		content.forEach(data -> {

			if (data.isStatus()) {
				JobResponse jobResponse = new JobResponse();
				jobResponse.setTitle(data.getTitle());
				jobResponse.setId(data.getId());
				Company company2 = data.getCompany();
				CompanyResponse companyResponse = new CompanyResponse();
				companyResponse.setName(company2.getName());
				companyResponse.setId(company2.getId());
				companyResponse.setMobileNumber(company2.getMobileNumber());
				companyResponse.setEmail(company2.getMobileNumber());
				if (company2.getLogo() != null) {
					companyResponse.setLogo(jobLogoUrl + company2.getLogo());
				}
				jobResponse.setCompany(companyResponse);

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreateOn());
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						jobResponse.setActiveHours(diffInMinutes + " minuts");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						jobResponse.setActiveHours(diffInHours + " hour");
					} else {
						jobResponse.setActiveHours(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jobResponse.setDescription(data.getDescription());
				jobResponse.setExperience(data.getExperience());
				jobResponse.setJobType(data.getJobType());
				jobResponse.setLocation(data.getLocation());
				jobResponse.setSkills(data.getSkills());
				jobResponse.setStatus(data.isStatus());
				jobResponse.setDepartment(data.getDepartment());
				jobResponses.add(jobResponse);
			}
		});

		JobResponseWithPagination jobResponseWithPagination = new JobResponseWithPagination();
		jobResponseWithPagination.setContent(jobResponses);
		jobResponseWithPagination.setLastPage(findAll.isLast());
		jobResponseWithPagination.setPageNumber(findAll.getNumber());
		jobResponseWithPagination.setPageSize(findAll.getSize());
		jobResponseWithPagination.setTotalElement(findAll.getTotalElements());
		jobResponseWithPagination.setTotalPages(findAll.getTotalPages());

		return jobResponseWithPagination;
	}

	@Override
	public JobResponseWithPagination getJobs(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<com.tuespotsolutions.entity.Job> findAll = this.jobRepository.findAll(pageable);
		List<com.tuespotsolutions.entity.Job> content = findAll.getContent();
		List<JobResponse> jobResponses = new ArrayList<JobResponse>();
		content.forEach(data -> {

			if (data.isStatus()) {
				JobResponse jobResponse = new JobResponse();
				jobResponse.setTitle(data.getTitle());
				jobResponse.setId(data.getId());
				Company company2 = data.getCompany();
				CompanyResponse companyResponse = new CompanyResponse();
				companyResponse.setName(company2.getName());
				companyResponse.setId(company2.getId());
				companyResponse.setMobileNumber(company2.getMobileNumber());
				companyResponse.setEmail(company2.getMobileNumber());
				if (company2.getLogo() != null) {
					companyResponse.setLogo(jobLogoUrl + company2.getLogo());
				}
				jobResponse.setCompany(companyResponse);

				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreateOn());
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						jobResponse.setActiveHours(diffInMinutes + " minuts");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						jobResponse.setActiveHours(diffInHours + " hour");
					} else {
						jobResponse.setActiveHours(diffInDays + " day");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				jobResponse.setDescription(data.getDescription());
				jobResponse.setExperience(data.getExperience());
				jobResponse.setJobType(data.getJobType());
				jobResponse.setLocation(data.getLocation());
				jobResponse.setSkills(data.getSkills());
				jobResponse.setStatus(data.isStatus());
				jobResponse.setDepartment(data.getDepartment());
				jobResponses.add(jobResponse);
			}

		});

		JobResponseWithPagination jobResponseWithPagination = new JobResponseWithPagination();
		jobResponseWithPagination.setContent(jobResponses);
		jobResponseWithPagination.setLastPage(findAll.isLast());
		jobResponseWithPagination.setPageNumber(findAll.getNumber());
		jobResponseWithPagination.setPageSize(findAll.getSize());
		jobResponseWithPagination.setTotalElement(findAll.getTotalElements());
		jobResponseWithPagination.setTotalPages(findAll.getTotalPages());

		return jobResponseWithPagination;
	}

	@Override
	public JobResponse getJobById(Long jobId) {
		com.tuespotsolutions.entity.Job save = this.jobRepository.findById(jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Job is not exist with jobId : " + jobId));
		JobResponse jobResponse = new JobResponse();
		jobResponse.setTitle(save.getTitle());
		jobResponse.setId(save.getId());
		Company company2 = save.getCompany();
		CompanyResponse companyResponse = new CompanyResponse();
		companyResponse.setName(company2.getName());
		companyResponse.setId(company2.getId());
		companyResponse.setMobileNumber(company2.getMobileNumber());
		companyResponse.setEmail(company2.getMobileNumber());
		if (company2.getLogo() != null) {
			companyResponse.setLogo(jobLogoUrl + company2.getLogo());
		}
		jobResponse.setCompany(companyResponse);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);
		try {
			Date createdOn = timeStamp.parse(save.getCreateOn());
			Date date = new Date();
			String format = timeStamp.format(date);
			Date parse = timeStamp.parse(format);
			long createdTime = createdOn.getTime();
			long nowTime = parse.getTime();
			long activeHours = nowTime - createdTime;

			long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
			long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
			long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
			long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
			if (diffInHours < 1) {
				jobResponse.setActiveHours(diffInMinutes + " minuts");
			} else if (diffInHours >= 1 && diffInHours < 24) {
				jobResponse.setActiveHours(diffInHours + " hour");
			} else {
				jobResponse.setActiveHours(diffInDays + " day");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jobResponse.setDescription(save.getDescription());
		jobResponse.setExperience(save.getExperience());
		jobResponse.setJobType(save.getJobType());
		jobResponse.setLocation(save.getLocation());
		jobResponse.setSkills(save.getSkills());
		jobResponse.setStatus(save.isStatus());
		jobResponse.setWorkMode(save.getTitle());
		jobResponse.setDepartment(save.getDepartment());
		List<FiltersListForJobPost> filterWithValues=new ArrayList<FiltersListForJobPost>();
		
		
		// find all filters from filter table
		
		
		// fild  filter value with filter id and jobid
		
		
//		List<Filters> filters = this.filterRepository.findAll();	
//		filters.forEach(data->{
//			FiltersListForJobPost filterModel=new FiltersListForJobPost();
//			filterModel.setFilterName(data.getFilterName());
//			filterModel.setId(data.getId());
//			List<JobFilter> filterJobList = this.jobFilterRepository.findByJobAndFilter(save,data);
//			filterJobList.forEach((item)->{
//				
//				FilterValues filterValue = item.getFilterValue();
//				FilterValueResponseForPostJob valueFilter=new FilterValueResponseForPostJob();
//				valueFilter.setFilterValue(filterValue.getFilterValue());
//				valueFilter.setId(filterValue.getId());	
//				filerValues.add(valueFilter);
//				filterModel.setFilterValues(filerValues);
//				filterWithValues.add(filterModel);
//				});	
//		});	
		
		List<FiltersListForJobPost> filerValues=new ArrayList<FiltersListForJobPost>();
		List<Filters> filters = this.filterRepository.findAll();
		filters.forEach(data->{
			FiltersListForJobPost filterModel=new FiltersListForJobPost();
			filterModel.setId(data.getId());
			filterModel.setFilterName(data.getFilterName());
			filterModel.setStatus(data.getStatus())	;	
			List<JobFilter> filterJobList = this.jobFilterRepository.findByJobAndFilter(save,data);
			List<FilterValueResponseForPostJob> filterValueList = new ArrayList<FilterValueResponseForPostJob>();
			filterJobList.forEach(dataValue -> {
				FilterValueResponseForPostJob valueFilter=new FilterValueResponseForPostJob();
				valueFilter.setFilterValue(dataValue.getFilterValue().getFilterValue());
				valueFilter.setId(dataValue.getFilterValue().getId());
				filterValueList.add(valueFilter);
			});
			filterModel.setFilterValues(filterValueList);
			filerValues.add(filterModel);
		});	
		
		jobResponse.setFilterWithValues(filerValues);
		return jobResponse;
	}

	@Override
	public void deleteJob(Long jobId) {
		com.tuespotsolutions.entity.Job jobs = this.jobRepository.findById(jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Job is not exist with jobId : " + jobId));
		System.err.println(jobs.toString());
		try {
			
			List<JobFilter> findByJob = this.jobFilterRepository.findByJob(jobs);
			if(findByJob.size() > 0) {
				findByJob.forEach(data->{
					System.err.println(data.toString());
					this.jobFilterRepository.delete(data);
				});
			}
			this.jobRepository.delete(jobs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// baljinder
	@Override
	public JobResponseWithPagination searchJobByTitleOrLocation(String jobTitle, String jobLocation, Integer page,
			Integer size) {
		Specification<com.tuespotsolutions.entity.Job> specification = Specification
				.where(JobSpecification.containsTitle(jobTitle).or(JobSpecification.containsLocation(jobLocation)));
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));
		Page<com.tuespotsolutions.entity.Job> findAll = this.jobRepository.findAll(specification, pageable);
		List<com.tuespotsolutions.entity.Job> content = findAll.getContent();
		JobResponseWithPagination response = new JobResponseWithPagination();
		List<JobResponse> jobResponses = new ArrayList<JobResponse>();
		content.forEach(data -> {

			if (data.isStatus()) {
				JobResponse jobResponse = new JobResponse();
				jobResponse.setTitle(data.getTitle());
				jobResponse.setId(data.getId());
				Company company2 = data.getCompany();
				CompanyResponse companyResponse = new CompanyResponse();
				companyResponse.setName(company2.getName());
				companyResponse.setId(company2.getId());
				companyResponse.setMobileNumber(company2.getMobileNumber());
				companyResponse.setEmail(company2.getMobileNumber());
				if (company2.getLogo() != null) {
					companyResponse.setLogo(jobLogoUrl + company2.getLogo());
				}
				jobResponse.setCompany(companyResponse);
				jobResponse.setDescription(data.getDescription());
				jobResponse.setExperience(data.getExperience());
				jobResponse.setJobType(data.getJobType());
				jobResponse.setLocation(data.getLocation());
				jobResponse.setSkills(data.getSkills());
				jobResponse.setStatus(data.isStatus());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
				SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStamp.setTimeZone(istTimeZone);
				try {
					Date createdOn = timeStamp.parse(data.getCreateOn());
					Date date = new Date();
					String format = timeStamp.format(date);
					Date parse = timeStamp.parse(format);
					long createdTime = createdOn.getTime();
					long nowTime = parse.getTime();
					long activeHours = nowTime - createdTime;

					long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
					long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
					long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
					long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
					if (diffInHours < 1) {
						jobResponse.setActiveHours(diffInMinutes + " minuts");
					} else if (diffInHours >= 1 && diffInHours < 24) {
						jobResponse.setActiveHours(diffInHours + " hour");
					} else {
						jobResponse.setActiveHours(diffInDays + " day");
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jobResponse.setDepartment(data.getDepartment());
				jobResponses.add(jobResponse);
			}
		});
		response.setContent(jobResponses);
		response.setLastPage(findAll.isLast());
		response.setPageNumber(findAll.getNumber());
		response.setPageSize(findAll.getSize());
		response.setTotalElement(findAll.getTotalElements());
		response.setTotalPages(findAll.getTotalPages());
		return response;
	}

	@Override
	public JobSearchFilters jobSearchingFilters() {

		JobSearchFilters jobFilters = new JobSearchFilters();

		List<String> findDepartment = this.jobRepository.findDepartment();
		Set<DepartmentFilter> department = new HashSet<DepartmentFilter>();
		findDepartment.forEach(data -> {
			DepartmentFilter departmentFilter = new DepartmentFilter();
			if (data != null) {
				departmentFilter.setTitle(data);
				department.add(departmentFilter);
			}
		});
		jobFilters.setJobDepartment(department);

		List<String> findLocation = this.jobRepository.findLocation();
		Set<LocationFilters> locationFilters = new HashSet<LocationFilters>();
		findLocation.forEach(data -> {
			LocationFilters filters = new LocationFilters();
			if (data != null) {
				filters.setTitle(data);
				locationFilters.add(filters);
			}

		});
		jobFilters.setJobLocation(locationFilters);

//		List<String> findWorkMode = this.jobRepository.findWorkMode();
//		Set<WorkModeFilter> workMode = new HashSet<WorkModeFilter>();
//		findWorkMode.forEach(data -> {
//			WorkModeFilter workModeFilter = new WorkModeFilter();
//			if (data != null) {
//				workModeFilter.setTitle(data);
//				workMode.add(workModeFilter);
//			}
//		});
		// jobFilters.setJobWorkMode(workMode);

		// List<String> findCategory = this.jobRepository.findType();
		Set<JobCategoryFilters> categoryFilters = new HashSet<JobCategoryFilters>();
//		findCategory.forEach(data -> {
//			JobCategoryFilters categoryFilter = new JobCategoryFilters();
//			if (data != null) {
//				categoryFilter.setTitle(data);
//				categoryFilters.add(categoryFilter);
//			}
//		});
		jobFilters.setJobCategory(categoryFilters);

		return jobFilters;
	}

	@Override
	public Set<JobResponse> findByFilters(JobSearchFilters jobSearchFilters) {

		Set<JobResponse> jobResponses = new HashSet<JobResponse>();

		Set<DepartmentFilter> jobDepartment = jobSearchFilters.getJobDepartment();
		jobDepartment.forEach(data -> {
			if (data.isStatus()) {
				List<com.tuespotsolutions.entity.Job> findByDepartment = this.jobRepository
						.findByDepartment(data.getTitle());
				findByDepartment.forEach(dept -> {
					JobResponse jobResponse = new JobResponse();
					jobResponse.setTitle(dept.getTitle());
					jobResponse.setId(dept.getId());
					Company company2 = dept.getCompany();
					CompanyResponse companyResponse = new CompanyResponse();
					companyResponse.setName(company2.getName());
					companyResponse.setId(company2.getId());
					companyResponse.setMobileNumber(company2.getMobileNumber());
					companyResponse.setEmail(company2.getMobileNumber());
					if (company2.getLogo() != null) {
						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
					}
					jobResponse.setCompany(companyResponse);
					jobResponse.setDescription(dept.getDescription());
					jobResponse.setExperience(dept.getExperience());
					jobResponse.setJobType(dept.getJobType());
					jobResponse.setLocation(dept.getLocation());
					jobResponse.setSkills(dept.getSkills());
					jobResponse.setStatus(dept.isStatus());

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timeStamp.setTimeZone(istTimeZone);
					try {
						Date createdOn = timeStamp.parse(dept.getCreateOn());
						Date date = new Date();
						String format = timeStamp.format(date);
						Date parse = timeStamp.parse(format);
						long createdTime = createdOn.getTime();
						long nowTime = parse.getTime();
						long activeHours = nowTime - createdTime;

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
						long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
						if (diffInHours < 1) {
							jobResponse.setActiveHours(diffInMinutes + " minuts");
						} else if (diffInHours >= 1 && diffInHours < 24) {
							jobResponse.setActiveHours(diffInHours + " hour");
						} else {
							jobResponse.setActiveHours(diffInDays + " day");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jobResponse.setDepartment(dept.getDepartment());
					jobResponses.add(jobResponse);
				});
			}
		});

		Set<JobCategoryFilters> jobCategory = jobSearchFilters.getJobCategory();
		jobCategory.forEach(data -> {
			if (data.isStatus()) {
//				List<com.tuespotsolutions.entity.Job> findByType = this.jobRepository.findByType(data.getTitle());
//				findByType.forEach(dept -> {
//					JobResponse jobResponse = new JobResponse();
//					jobResponse.setTitle(dept.getTitle());
//					jobResponse.setId(dept.getId());
//					Company company2 = dept.getCompany();
//					CompanyResponse companyResponse = new CompanyResponse();
//					companyResponse.setName(company2.getName());
//					companyResponse.setId(company2.getId());
//					companyResponse.setMobileNumber(company2.getMobileNumber());
//					companyResponse.setEmail(company2.getMobileNumber());
//					if (company2.getLogo() != null) {
//						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
//					}
//					jobResponse.setCompany(companyResponse);
//					jobResponse.setDescription(dept.getDescription());
//					jobResponse.setExperience(dept.getExperience());
//					jobResponse.setJobType(dept.getJobType());
//					jobResponse.setLocation(dept.getLocation());
//					jobResponse.setSkills(dept.getSkills());
//					jobResponse.setStatus(dept.isStatus());
//					JobWorkMode jobWorkMode = this.jobWorkModeRepository.findById(dept.getWorkModeId())
//							.orElseThrow(() -> new ResourceNotFoundException("Work Mode Not Found"));
//					jobResponse.setWorkMode(jobWorkMode.getTitle());
//
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
//					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					timeStamp.setTimeZone(istTimeZone);
//					try {
//						Date createdOn = timeStamp.parse(dept.getCreateOn());
//						Date date = new Date();
//						String format = timeStamp.format(date);
//						Date parse = timeStamp.parse(format);
//						long createdTime = createdOn.getTime();
//						long nowTime = parse.getTime();
//						long activeHours = nowTime - createdTime;
//
//						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
//						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
//						long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
//						long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
//						if (diffInHours < 1) {
//							jobResponse.setActiveHours(diffInMinutes + " minuts");
//						} else if (diffInHours >= 1 && diffInHours < 24) {
//							jobResponse.setActiveHours(diffInHours + " hour");
//						} else {
//							jobResponse.setActiveHours(diffInDays + " day");
//						}
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					jobResponse.setDepartment(dept.getDepartment());
//					jobResponses.add(jobResponse);
				// });
			}
		});
		Set<LocationFilters> jobLocation = jobSearchFilters.getJobLocation();
		jobLocation.forEach(data -> {
			if (data.isStatus()) {
				List<com.tuespotsolutions.entity.Job> findByLocation = this.jobRepository
						.findByLocation(data.getTitle());
				findByLocation.forEach(dept -> {
					JobResponse jobResponse = new JobResponse();
					jobResponse.setTitle(dept.getTitle());
					jobResponse.setId(dept.getId());
					Company company2 = dept.getCompany();
					CompanyResponse companyResponse = new CompanyResponse();
					companyResponse.setName(company2.getName());
					companyResponse.setId(company2.getId());
					companyResponse.setMobileNumber(company2.getMobileNumber());
					companyResponse.setEmail(company2.getMobileNumber());
					if (company2.getLogo() != null) {
						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
					}
					jobResponse.setCompany(companyResponse);
					jobResponse.setDescription(dept.getDescription());
					jobResponse.setExperience(dept.getExperience());
					jobResponse.setJobType(dept.getJobType());
					jobResponse.setLocation(dept.getLocation());
					jobResponse.setSkills(dept.getSkills());
					jobResponse.setStatus(dept.isStatus());

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timeStamp.setTimeZone(istTimeZone);
					try {
						Date createdOn = timeStamp.parse(dept.getCreateOn());
						Date date = new Date();
						String format = timeStamp.format(date);
						Date parse = timeStamp.parse(format);
						long createdTime = createdOn.getTime();
						long nowTime = parse.getTime();
						long activeHours = nowTime - createdTime;

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
						long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
						if (diffInHours < 1) {
							jobResponse.setActiveHours(diffInMinutes + " minuts");
						} else if (diffInHours >= 1 && diffInHours < 24) {
							jobResponse.setActiveHours(diffInHours + " hour");
						} else {
							jobResponse.setActiveHours(diffInDays + " day");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jobResponse.setDepartment(dept.getDepartment());
					jobResponses.add(jobResponse);
				});
			}
		});
		Set<WorkModeFilter> jobWorkMode = jobSearchFilters.getJobWorkMode();
		jobWorkMode.forEach(data -> {
			if (data.isStatus()) {
				List<com.tuespotsolutions.entity.Job> findByJobType = this.jobRepository.findByJobType(data.getTitle());
				findByJobType.forEach(dept -> {
					JobResponse jobResponse = new JobResponse();
					jobResponse.setTitle(dept.getTitle());
					jobResponse.setId(dept.getId());
					Company company2 = dept.getCompany();
					CompanyResponse companyResponse = new CompanyResponse();
					companyResponse.setName(company2.getName());
					companyResponse.setId(company2.getId());
					companyResponse.setMobileNumber(company2.getMobileNumber());
					companyResponse.setEmail(company2.getMobileNumber());
					if (company2.getLogo() != null) {
						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
					}
					jobResponse.setCompany(companyResponse);
					jobResponse.setDescription(dept.getDescription());
					jobResponse.setExperience(dept.getExperience());
					jobResponse.setJobType(dept.getJobType());
					jobResponse.setLocation(dept.getLocation());
					jobResponse.setSkills(dept.getSkills());
					jobResponse.setStatus(dept.isStatus());

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timeStamp.setTimeZone(istTimeZone);
					try {
						Date createdOn = timeStamp.parse(dept.getCreateOn());
						Date date = new Date();
						String format = timeStamp.format(date);
						Date parse = timeStamp.parse(format);
						long createdTime = createdOn.getTime();
						long nowTime = parse.getTime();
						long activeHours = nowTime - createdTime;

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
						long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
						if (diffInHours < 1) {
							jobResponse.setActiveHours(diffInMinutes + " minuts");
						} else if (diffInHours >= 1 && diffInHours < 24) {
							jobResponse.setActiveHours(diffInHours + " hour");
						} else {
							jobResponse.setActiveHours(diffInDays + " day");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jobResponse.setDepartment(dept.getDepartment());
					jobResponses.add(jobResponse);
				});
			}
		});
		return jobResponses;
	}

	// baljinder
	@Override
	public JobResponseWithPagination findByJobAccordingToCandidateSkillSet(Long candidateId, Integer page,
			Integer size) {
		Candidate candidate = this.candidateRepository.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate Not Eist"));
		List<CandidateSkills> canidateSkills = this.candidateSkillsRepository.findByCandidate(candidate);
		
		
		
		
		
		List<JobResponse> jobResponses = new ArrayList<JobResponse>();
		canidateSkills.forEach(skill -> {
			
			System.err.println(skill);
			
			Specification<com.tuespotsolutions.entity.Job> jobSpecification = Specification
					.where(JobListAccordingToSkillSetSpecification.containSkills(skill.getSkill()));
			List<com.tuespotsolutions.entity.Job> jobList = this.jobRepository.findAll(jobSpecification);

			jobList.forEach(job -> {

				if (job.isStatus()) {

					JobResponse jobResponse = new JobResponse();
					jobResponse.setTitle(job.getTitle());
					jobResponse.setId(job.getId());
					Company company2 = job.getCompany();
					CompanyResponse companyResponse = new CompanyResponse();
					companyResponse.setName(company2.getName());
					companyResponse.setId(company2.getId());
					companyResponse.setMobileNumber(company2.getMobileNumber());
					companyResponse.setEmail(company2.getMobileNumber());
					if (company2.getLogo() != null) {
						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
					}
					jobResponse.setCompany(companyResponse);
					jobResponse.setDescription(job.getDescription());
					jobResponse.setExperience(job.getExperience());
					jobResponse.setJobType(job.getJobType());
					jobResponse.setLocation(job.getLocation());
					jobResponse.setSkills(job.getSkills());
					jobResponse.setStatus(job.isStatus());

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timeStamp.setTimeZone(istTimeZone);
					try {
						Date createdOn = timeStamp.parse(job.getCreateOn());
						Date date = new Date();
						String format = timeStamp.format(date);
						Date parse = timeStamp.parse(format);
						long createdTime = createdOn.getTime();
						long nowTime = parse.getTime();
						long activeHours = nowTime - createdTime;

						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
						long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
						long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
						long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
						if (diffInHours < 1) {
							jobResponse.setActiveHours(diffInMinutes + " minuts");
						} else if (diffInHours >= 1 && diffInHours < 24) {
							jobResponse.setActiveHours(diffInHours + " hour");
						} else {
							jobResponse.setActiveHours(diffInDays + " day");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jobResponse.setDepartment(job.getDepartment());
					jobResponses.add(jobResponse);

				}

			});

		});

		Pageable pageable = PageRequest.of(page, size);
		JobResponseWithPagination jobResponseWithPagination = new JobResponseWithPagination();

		PageImpl<JobResponse> pageImpl = new PageImpl<JobResponse>(jobResponses, pageable, jobResponses.size());
		List<JobResponse> content = pageImpl.getContent();
		jobResponseWithPagination.setContent(content);
		jobResponseWithPagination.setLastPage(pageImpl.isLast());
		jobResponseWithPagination.setPageNumber(pageImpl.getNumber());
		jobResponseWithPagination.setPageSize(pageImpl.getSize());
		jobResponseWithPagination.setTotalElement(pageImpl.getTotalElements());
		jobResponseWithPagination.setTotalPages(pageImpl.getTotalPages());

		return jobResponseWithPagination;
	}

	@Override
	public JobResponseWithPagination getJobsByComanyForCompanyPanelJobList(Integer page, Integer size, Long companyId) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

		Company company = this.companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company is not exist with companyId : " + companyId));
		Page<com.tuespotsolutions.entity.Job> findAll = this.jobRepository.findByCompany(pageable, company);

		List<com.tuespotsolutions.entity.Job> content = findAll.getContent();

		List<JobResponse> jobResponses = new ArrayList<JobResponse>();
		content.forEach(data -> {

			JobResponse jobResponse = new JobResponse();
			jobResponse.setTitle(data.getTitle());
			jobResponse.setId(data.getId());

			Company company2 = data.getCompany();
			CompanyResponse companyResponse = new CompanyResponse();
			companyResponse.setName(company2.getName());
			companyResponse.setId(company2.getId());
			companyResponse.setMobileNumber(company2.getMobileNumber());
			companyResponse.setEmail(company2.getMobileNumber());
			if (company2.getLogo() != null) {
				companyResponse.setLogo(jobLogoUrl + company2.getLogo());
			}
			jobResponse.setCompany(companyResponse);

			TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
			SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			timeStamp.setTimeZone(istTimeZone);
			try {
				Date createdOn = timeStamp.parse(data.getCreateOn());
				Date date = new Date();
				String format = timeStamp.format(date);
				Date parse = timeStamp.parse(format);
				long createdTime = createdOn.getTime();
				long nowTime = parse.getTime();
				long activeHours = nowTime - createdTime;

				long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(activeHours);
				long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(activeHours);
				long diffInHours = TimeUnit.MILLISECONDS.toHours(activeHours);
				long diffInDays = TimeUnit.MILLISECONDS.toDays(activeHours);
				if (diffInHours < 1) {
					jobResponse.setActiveHours(diffInMinutes + " minuts");
				} else if (diffInHours >= 1 && diffInHours < 24) {
					jobResponse.setActiveHours(diffInHours + " hour");
				} else {
					jobResponse.setActiveHours(diffInDays + " day");
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jobResponse.setDescription(data.getDescription());
			jobResponse.setExperience(data.getExperience());
			jobResponse.setJobType(data.getJobType());
			jobResponse.setLocation(data.getLocation());
			jobResponse.setSkills(data.getSkills());
			jobResponse.setStatus(data.isStatus());
			jobResponse.setDepartment(data.getDepartment());
			jobResponses.add(jobResponse);
		});
		
		
		Collections.sort(jobResponses, new Comparator<JobResponse>() {

			@Override
			public int compare(JobResponse o1, JobResponse o2) {
				return Boolean.compare(o2.isStatus(),o1.isStatus());
			}
			
		});
	

		JobResponseWithPagination jobResponseWithPagination = new JobResponseWithPagination();
		jobResponseWithPagination.setContent(jobResponses);
		jobResponseWithPagination.setLastPage(findAll.isLast());
		jobResponseWithPagination.setPageNumber(findAll.getNumber());
		jobResponseWithPagination.setPageSize(findAll.getSize());
		jobResponseWithPagination.setTotalElement(findAll.getTotalElements());
		jobResponseWithPagination.setTotalPages(findAll.getTotalPages());

		return jobResponseWithPagination;
	}

	@Override
	public List<CityResponse> findAllCities() {
		List<City> findAll = this.cityRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
		List<CityResponse> cityResponses = new ArrayList<CityResponse>();
		findAll.forEach(data -> {
			CityResponse cityResponse = new CityResponse();
			cityResponse.setCityName(data.getName());
			cityResponse.setId(data.getId());
			cityResponses.add(cityResponse);
		});
		return cityResponses;
	}

}
