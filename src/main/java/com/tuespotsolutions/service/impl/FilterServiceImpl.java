package com.tuespotsolutions.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import com.google.gson.Gson;
import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.FilterValues;
import com.tuespotsolutions.entity.Filters;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobFilter;
import com.tuespotsolutions.models.CompanyResponse;
import com.tuespotsolutions.models.FilterValueResponseForPostJob;
import com.tuespotsolutions.models.FilteredJobsWithFilters;
import com.tuespotsolutions.models.FiltersList;
import com.tuespotsolutions.models.FiltersListForJobPost;
import com.tuespotsolutions.models.FiltersRequest;
import com.tuespotsolutions.models.FiltersRequestWithPagination;
import com.tuespotsolutions.models.JobFilterModeR;
import com.tuespotsolutions.models.JobLocationsGroup;
import com.tuespotsolutions.models.JobResponse;
import com.tuespotsolutions.models.JobResponseWithPagination;
import com.tuespotsolutions.models.JobWorkModeModel;
import com.tuespotsolutions.models.PostedOnJob;
import com.tuespotsolutions.repository.FilterRepository;
import com.tuespotsolutions.repository.FilterRepository.JobCountByCity;
import com.tuespotsolutions.repository.FilterRepository.JobCountByWorkMode;
import com.tuespotsolutions.repository.FilterValueRepository;
import com.tuespotsolutions.repository.JobFilterRepository;
import com.tuespotsolutions.repository.JobFilterValuesRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.service.FilterService;
import com.tuespotsolutions.specifications.JobSpecification;

@Service
public class FilterServiceImpl implements FilterService {

	@Autowired
	FilterRepository filterRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private FilterValueRepository filterValueRepository;

	@Autowired
	JobFilterRepository jobFilterRepository;

	@Autowired
	JobFilterValuesRepository jobFilterValuesRepository;

	@Value("${file.get.url}")
	private String jobLogoUrl;

	@Override
	public FiltersRequest addFilters(FiltersRequest filtersRequest) {

		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		String pattern = "MM/dd/yyyy HH:mm:ss";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date according to the chosen pattern
		DateFormat df = new SimpleDateFormat(pattern);
		String format = df.format(utilDate);
		Date nowDate = new Date();
		try {
			nowDate = timeStamp.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Filters filters = new Filters();
		filters.setCreatedOn(nowDate);
		filters.setFilterName(filtersRequest.getFilterName());
		filters.setFilterType(filtersRequest.getFilterType());
		filters.setModifiedOn(nowDate);
		filters.setStatus(false);

		Filters save = this.filterRepository.save(filters);
		FiltersRequest filtersResponse = new FiltersRequest();
		filtersResponse.setCreatedOn(save.getCreatedOn());
		filtersResponse.setFilterName(save.getFilterName());
		filtersResponse.setFilterType(save.getFilterType());
		filtersResponse.setModifiedOn(save.getModifiedOn());
		filtersResponse.setStatus(save.getStatus());
		filtersResponse.setId(save.getId());
		return filtersResponse;
	}

	@Override
	public FiltersRequest updateFilters(FiltersRequest filtersRequest) {
		java.util.Date utilDate = new java.util.Date();
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp.setTimeZone(istTimeZone);

		String pattern = "MM/dd/yyyy HH:mm:ss";
		// Create an instance of SimpleDateFormat used for formatting
		// the string representation of date according to the chosen pattern
		DateFormat df = new SimpleDateFormat(pattern);
		String format = df.format(utilDate);
		Date nowDate = new Date();
		try {
			nowDate = timeStamp.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Filters filters = this.filterRepository.findById(filtersRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
		filters.setFilterName(filtersRequest.getFilterName());
		filters.setFilterType(filtersRequest.getFilterType());
		filters.setModifiedOn(nowDate);
		filters.setStatus(filtersRequest.isStatus());

		Filters save = this.filterRepository.save(filters);
		FiltersRequest filtersResponse = new FiltersRequest();
		filtersResponse.setCreatedOn(save.getCreatedOn());
		filtersResponse.setFilterName(save.getFilterName());
		filtersResponse.setFilterType(save.getFilterType());
		filtersResponse.setModifiedOn(save.getModifiedOn());
		filtersResponse.setStatus(save.getStatus());
		filtersResponse.setId(save.getId());
		return filtersResponse;
	}

	@Override
	public FiltersRequestWithPagination getFilters(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Filters> findAll = this.filterRepository.findAll(pageable);

		FiltersRequestWithPagination filtersRequestWithPagination = new FiltersRequestWithPagination();

		List<Filters> content = findAll.getContent();
		List<FiltersRequest> response = new ArrayList<FiltersRequest>();
		content.forEach(filters -> {
			FiltersRequest filtersResponse = new FiltersRequest();
			filtersResponse.setCreatedOn(filters.getCreatedOn());
			filtersResponse.setFilterName(filters.getFilterName());
			filtersResponse.setFilterType(filters.getFilterType());
			filtersResponse.setModifiedOn(filters.getModifiedOn());
			filtersResponse.setStatus(filters.getStatus());
			filtersResponse.setId(filters.getId());
			response.add(filtersResponse);
		});
		filtersRequestWithPagination.setFiltersRequests(response);
		filtersRequestWithPagination.setLastPage(findAll.isLast());
		filtersRequestWithPagination.setPageNumber(findAll.getNumber());
		filtersRequestWithPagination.setPageSize(findAll.getSize());
		filtersRequestWithPagination.setTotalElement(findAll.getTotalElements());
		filtersRequestWithPagination.setTotalPages(findAll.getTotalPages());
		return filtersRequestWithPagination;
	}

	@Override
	public FiltersRequest getFilterById(Long id) {
		Filters filters = this.filterRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));

		FiltersRequest filtersResponse = new FiltersRequest();
		filtersResponse.setCreatedOn(filters.getCreatedOn());
		filtersResponse.setFilterName(filters.getFilterName());
		filtersResponse.setFilterType(filters.getFilterType());
		filtersResponse.setModifiedOn(filters.getModifiedOn());
		filtersResponse.setStatus(filters.getStatus());
		filtersResponse.setId(filters.getId());

		return filtersResponse;
	}

	@Override
	public void deleteFilter(long id) {
		Filters filters = this.filterRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
		this.filterRepository.delete(filters);
	}

	@Override
	public List<FiltersRequest> getEnabledFilters() {
		List<Filters> findByStatus = this.filterRepository.findByStatus(true);
		List<FiltersRequest> resp = new ArrayList<FiltersRequest>();
		findByStatus.forEach(data -> {
			FiltersRequest filtersResponse = new FiltersRequest();
			filtersResponse.setCreatedOn(data.getCreatedOn());
			filtersResponse.setFilterName(data.getFilterName());
			filtersResponse.setFilterType(data.getFilterType());
			filtersResponse.setModifiedOn(data.getModifiedOn());
			filtersResponse.setStatus(data.getStatus());
			filtersResponse.setId(data.getId());
			resp.add(filtersResponse);
		});
		return resp;
	}

	@Override
	public List<JobLocationsGroup> findJobLoactionGroup(String jobTitile) {
		List<JobCountByCity> findJobCountByCity = this.filterRepository.findJobCountByCity(jobTitile);
		List<JobLocationsGroup> jobLocationsGroupResponse = new ArrayList<JobLocationsGroup>();
		findJobCountByCity.forEach(data -> {
			JobLocationsGroup jobLocationsGroup = new JobLocationsGroup();
			jobLocationsGroup.setCityId(Integer.parseInt(data.getId()));
			jobLocationsGroup.setCityName(data.getName());
			jobLocationsGroup.setJobCount(data.getJobCount());
			jobLocationsGroup.setJobTitle(jobTitile);
			jobLocationsGroupResponse.add(jobLocationsGroup);
		});
		return jobLocationsGroupResponse;
	}

	@Override
	public FilteredJobsWithFilters filterJobsByCity(List<JobLocationsGroup> jobLocationsGroups, Integer page,
			Integer size) {
//		Pageable pageable = PageRequest.of(page, size);
//		List<JobResponse> jobResp = new ArrayList<JobResponse>();
//		jobLocationsGroups.forEach(jobLocationFilterData -> {
//			if (jobLocationFilterData.isStatus()) {
//				System.err.println("line no 222 : " + jobLocationFilterData.toString());
//				List<Job> findByCityId = this.jobRepository.getJobByCityAndTitle(jobLocationFilterData.getCityId(),
//						jobLocationFilterData.getJobTitle());
//				System.err.println("line no 224 : " + findByCityId.toString());
//				findByCityId.forEach(data -> {
//					System.err.println(data.toString());
//					JobResponse jobResponse = new JobResponse();
//					jobResponse.setTitle(data.getTitle());
//					jobResponse.setId(data.getId());
//					Company company2 = data.getCompany();
//					CompanyResponse companyResponse = new CompanyResponse();
//					companyResponse.setName(company2.getName());
//					companyResponse.setId(company2.getId());
//					companyResponse.setMobileNumber(company2.getMobileNumber());
//					companyResponse.setEmail(company2.getMobileNumber());
//					if (company2.getLogo() != null) {
//						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
//					}
//					jobResponse.setCompany(companyResponse);
//
//					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
//					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					timeStamp.setTimeZone(istTimeZone);
//					try {
//						Date createdOn = timeStamp.parse(data.getCreateOn());
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
//					jobResponse.setDescription(data.getDescription());
//					jobResponse.setExperience(data.getExperience());
//					jobResponse.setJobType(data.getJobType());
//					jobResponse.setLocation(data.getLocation());
//					jobResponse.setSkills(data.getSkills());
//					jobResponse.setStatus(data.isStatus());
//					jobResponse.setDepartment(data.getDepartment());
//					jobResp.add(jobResponse);
//				});
//
//			}
//		});
//
//		PageImpl<JobResponse> pageImpl = new PageImpl<JobResponse>(jobResp, pageable, jobResp.size());
//		FilteredJobsWithFilters resp = new FilteredJobsWithFilters();
//		List<JobResponse> content = pageImpl.getContent();
//		resp.setContent(content);
//		resp.setLastPage(pageImpl.isLast());
//		resp.setPageNumber(pageImpl.getNumber());
//		resp.setPageSize(pageImpl.getSize());
//		resp.setTotalElement(pageImpl.getTotalElements());
//		resp.setTotalPages(pageImpl.getTotalPages());
		return null;
	}

	@Override
	public List<PostedOnJob> findPostedOnGroup(String jobTitle) {

		Integer findLastDayJobCount = this.jobRepository.getLastDayJobCount(jobTitle);
		Integer findLastWeekJobCount = this.jobRepository.getLastWeekJobCount(jobTitle);
		Integer findLastMonthJobCount = this.jobRepository.getLastMonthJobCount(jobTitle);
		Integer findLastYearJobCount = this.jobRepository.getLastYearJobCount(jobTitle);
		List<PostedOnJob> response = new ArrayList<PostedOnJob>();
		PostedOnJob lastDay = new PostedOnJob();
		lastDay.setPostedOn("Last Day");
		lastDay.setNumber(1);
		lastDay.setJobTitle(jobTitle);
		lastDay.setJobCount(findLastDayJobCount + "");
		response.add(lastDay);

		PostedOnJob lastWeek = new PostedOnJob();
		lastWeek.setPostedOn("Last Week");
		lastWeek.setNumber(2);
		lastWeek.setJobTitle(jobTitle);
		lastWeek.setJobCount(findLastWeekJobCount + "");
		response.add(lastWeek);

		PostedOnJob lastMonth = new PostedOnJob();
		lastMonth.setPostedOn("Last Month");
		lastMonth.setNumber(3);
		lastMonth.setJobTitle(jobTitle);
		lastMonth.setJobCount(findLastMonthJobCount + "");
		response.add(lastMonth);

		PostedOnJob lastYear = new PostedOnJob();
		lastYear.setPostedOn("Last Year");
		lastYear.setNumber(4);
		lastYear.setJobTitle(jobTitle);
		lastYear.setJobCount(findLastYearJobCount + "");
		response.add(lastYear);
		;
		return response;
	}

	@Override
	public FilteredJobsWithFilters filterJobsByPostedOn(List<PostedOnJob> postedOnJobs, Integer page, Integer size) {

		List<JobResponse> jobResp = new ArrayList<JobResponse>();
		postedOnJobs.forEach(postedOn -> {
			// last day jobs
			if (postedOn.getNumber() == 1 && postedOn.isStatus()) {

				List<Job> findJobByPostedOnLastDay = this.jobRepository
						.findJobByPostedOnLastDay(postedOn.getJobTitle());
				findJobByPostedOnLastDay.forEach(data -> {
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
					jobResp.add(jobResponse);
				});

			}

			// last week jobs
			if (postedOn.getNumber() == 2 && postedOn.isStatus()) {
				List<Job> findJobByPostedOnLastWeek = this.jobRepository
						.findJobByPostedOnLastWeek(postedOn.getJobTitle());

				findJobByPostedOnLastWeek.forEach(data -> {
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

					jobResp.add(jobResponse);
				});

			}

			// last month jobs
			if (postedOn.getNumber() == 3 && postedOn.isStatus()) {
				List<Job> findJobByPostedOnLastMonth = this.jobRepository
						.findJobByPostedOnLastMonth(postedOn.getJobTitle());

				findJobByPostedOnLastMonth.forEach(data -> {
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

					jobResp.add(jobResponse);
				});

			}

			// last year jobs
			if (postedOn.getNumber() == 4 && postedOn.isStatus()) {
				List<Job> findJobByPostedOnLastYear = this.jobRepository
						.findJobByPostedOnLastYear(postedOn.getJobTitle());

				findJobByPostedOnLastYear.forEach(data -> {
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

					jobResp.add(jobResponse);
				});
			}

		});
		Pageable pageable = PageRequest.of(page, size);
		PageImpl<JobResponse> pageImpl = new PageImpl<JobResponse>(jobResp, pageable, jobResp.size());
		FilteredJobsWithFilters resp = new FilteredJobsWithFilters();
		resp.setContent(pageImpl.getContent());
		resp.setLastPage(pageImpl.isLast());
		resp.setPageNumber(pageImpl.getNumber());
		resp.setPageSize(pageImpl.getSize());
		resp.setTotalElement(pageImpl.getTotalElements());
		resp.setTotalPages(pageImpl.getTotalPages());
		return resp;
	}

	@Override
	public List<JobWorkModeModel> workModeFilterList(String jobTitle) {
		List<JobCountByWorkMode> findJobCountByWorkMode = this.filterRepository.findJobCountByWorkMode(jobTitle);
		List<JobWorkModeModel> jobWorkModeModels = new ArrayList<JobWorkModeModel>();
		findJobCountByWorkMode.forEach(data -> {
			JobWorkModeModel jobWorkModeModel = new JobWorkModeModel();
			jobWorkModeModel.setId(data.getId());
			jobWorkModeModel.setJobCount(data.getJobCount());
			jobWorkModeModel.setStatus(false);
			jobWorkModeModel.setJobTitle(jobTitle);
			jobWorkModeModel.setTitle(data.getTitle());
			jobWorkModeModels.add(jobWorkModeModel);
		});
		return jobWorkModeModels;
	}

	@Override
	public FilteredJobsWithFilters getJobWithWorkModeFilter(List<JobWorkModeModel> jobWorkModeModels, int page,
			int size) {

		List<JobResponse> jobResp = new ArrayList<JobResponse>();
//		jobWorkModeModels.forEach(workMode -> {
//			if (workMode.isStatus()) {
//				List<Job> findJobByWorkModeId = this.jobRepository.findByWorkModeId(workMode.getId());
//				findJobByWorkModeId.forEach(data -> {
//					JobResponse jobResponse = new JobResponse();
//					jobResponse.setTitle(data.getTitle());
//					jobResponse.setId(data.getId());
//					Company company2 = data.getCompany();
//					CompanyResponse companyResponse = new CompanyResponse();
//					companyResponse.setName(company2.getName());
//					companyResponse.setId(company2.getId());
//					companyResponse.setMobileNumber(company2.getMobileNumber());
//					companyResponse.setEmail(company2.getMobileNumber());
//					if (company2.getLogo() != null) {
//						companyResponse.setLogo(jobLogoUrl + company2.getLogo());
//					}
//					jobResponse.setCompany(companyResponse);
//
//					TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
//					SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					timeStamp.setTimeZone(istTimeZone);
//					try {
//						Date createdOn = timeStamp.parse(data.getCreateOn());
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
//					jobResponse.setDescription(data.getDescription());
//					jobResponse.setExperience(data.getExperience());
//					jobResponse.setJobType(data.getJobType());
//					jobResponse.setLocation(data.getLocation());
//					jobResponse.setSkills(data.getSkills());
//					jobResponse.setStatus(data.isStatus());
//					jobResponse.setDepartment(data.getDepartment());
//
//					jobResp.add(jobResponse);
//				});
//			}
//		});
//
//		Pageable pageable = PageRequest.of(page, size);
//		PageImpl<JobResponse> pageImpl = new PageImpl<JobResponse>(jobResp, pageable, jobResp.size());
//		FilteredJobsWithFilters resp = new FilteredJobsWithFilters();
//		resp.setContent(pageImpl.getContent());
//		resp.setLastPage(pageImpl.isLast());
//		resp.setPageNumber(pageImpl.getNumber());
//		resp.setPageSize(pageImpl.getSize());
//		resp.setTotalElement(pageImpl.getTotalElements());
//		resp.setTotalPages(pageImpl.getTotalPages());
		return null;
	}

	@Override
	public List<FiltersListForJobPost> findAllFilterListWithValues() {
		List<Filters> findAll = this.filterRepository.findAll();
		List<FiltersListForJobPost> filtersListForJobPosts = new ArrayList<FiltersListForJobPost>();
		findAll.forEach(filter -> {
			FiltersListForJobPost filtersListForJobPost = new FiltersListForJobPost();
			filtersListForJobPost.setId(filter.getId());
			filtersListForJobPost.setFilterName(filter.getFilterName());
			filtersListForJobPost.setFilterType(filter.getFilterType());
			filtersListForJobPost.setStatus(filter.getStatus());
			List<FilterValues> findByFilters = this.filterValueRepository.findByFilter(filter);
			List<FilterValueResponseForPostJob> filterValueResponse = new ArrayList<FilterValueResponseForPostJob>();
			findByFilters.forEach(values -> {
				FilterValueResponseForPostJob filterValueResponseForPostJob = new FilterValueResponseForPostJob();
				filterValueResponseForPostJob.setId(values.getId());
				filterValueResponseForPostJob.setFilterValue(values.getFilterValue());
				filterValueResponse.add(filterValueResponseForPostJob);
			});

			filtersListForJobPost.setFilterValues(filterValueResponse);
			filtersListForJobPosts.add(filtersListForJobPost);
		});

		return filtersListForJobPosts;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public List<FiltersList> getFilterList(String jobTitle, String jobLocation) {

		Specification<com.tuespotsolutions.entity.Job> specification = Specification
				.where(JobSpecification.containsTitle(jobTitle).or(JobSpecification.containsLocation(jobLocation))
						.and(JobSpecification.isStatus(true)));

		List<Job> findAll = this.jobRepository.findAll(specification);
		List<JobFilter> listofjobFilterByid = new ArrayList<JobFilter>();

		for (Job job : findAll) {
			System.err.println("job Id : " + job.getId());
			listofjobFilterByid.addAll(jobFilterRepository.findByJob(job));
		}

		// jobfilter table with filter id and value
		List<Filters> allfilter = filterRepository.findAll();

		// all filter list
		Map<Long, List<JobFilterModeR>> jobMap = new LinkedHashMap<Long, List<JobFilterModeR>>();
		for (Filters fl : allfilter) {

			List<JobFilterModeR> ls = new ArrayList<JobFilterModeR>();

			List<FilterValues> filterValues = fl.getFilterValues();
			for (FilterValues flv : filterValues) {
				JobFilterModeR obj = new JobFilterModeR();
				obj.setCount(0);
				obj.setFilterValueID(flv.getId());
				obj.setFilterValue(flv.getFilterValue());
				ls.add(obj);
			}

			jobMap.put(fl.getId(), ls);
		}

		// System.out.println("map "+jobMap);

		for (JobFilter filter : listofjobFilterByid) {
			;
			Long filterid = filter.getFilter().getId();
			List<JobFilterModeR> list = jobMap.get(filterid);
			List<JobFilterModeR> listUpdate = new ArrayList<JobFilterModeR>();
			Long filterValueID = filter.getFilterValue().getId();
			for (JobFilterModeR ls : list) {
				JobFilterModeR obj = new JobFilterModeR();
				obj.setCount(ls.getCount());
				obj.setFilterValue(ls.getFilterValue());
				obj.setFilterValueID(ls.getFilterValueID());
				obj.setJobTitle(jobTitle);
				if (ls.getFilterValueID() == filterValueID) {
					int count = ls.getCount();
					obj.setCount(count + 1);
				}

				listUpdate.add(obj);
			}

			jobMap.put(filterid, listUpdate);

		}

		List<FiltersList> res = new ArrayList<FiltersList>();
		for (Map.Entry<Long, List<JobFilterModeR>> entry : jobMap.entrySet()) {
			Long key = entry.getKey();
			Filters filters = this.filterRepository.findById(key)
					.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
			FiltersList filtersList = new FiltersList();
			filtersList.setId(filters.getId());
			filtersList.setFilterName(filters.getFilterName());
			filtersList.setFilterType(filters.getFilterType());
			filtersList.setStatus(filters.getStatus());
			List<JobFilterModeR> list = jobMap.get(key);
			filtersList.setValueList(list);
			res.add(filtersList);
		}

		// set filter response
		Gson gson = new Gson();
		System.err.println(gson.toJson(jobMap));

		return res;

	}

	@Override
	public JobResponseWithPagination getJobListByCheckedFilters(List<FiltersList> filtersList, int page ,int size) {

		List<JobResponse> jobResponses = new ArrayList<JobResponse>();
		filtersList.forEach(filter -> {
			filter.getValueList().forEach(value -> {
				if (value.isStatus() && value.getCount() > 0) {
				 List<Job> jobList = this.jobRepository
							.getJobIdByFilterIdAndFilterValueId(value.getJobTitle(),filter.getId(), value.getFilterValueID());
				 jobList.forEach(data -> {
						
						
					
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
				}
			});
		});
		
		
		Pageable pageable = PageRequest.of(page, size);
		PageImpl<JobResponse> pageImpl = new PageImpl<JobResponse>(jobResponses, pageable, jobResponses.size());
		JobResponseWithPagination resp = new JobResponseWithPagination();
		resp.setContent(pageImpl.getContent());
		resp.setLastPage(pageImpl.isLast());
		resp.setPageNumber(pageImpl.getNumber());
		resp.setPageSize(pageImpl.getSize());
		resp.setTotalElement(pageImpl.getTotalElements());
		resp.setTotalPages(pageImpl.getTotalPages());
		return resp;
	}

}
