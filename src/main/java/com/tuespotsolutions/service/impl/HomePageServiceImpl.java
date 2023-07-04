package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.Company;
import com.tuespotsolutions.entity.FilterValues;
import com.tuespotsolutions.entity.Filters;
import com.tuespotsolutions.entity.Job;
import com.tuespotsolutions.entity.JobFilter;
import com.tuespotsolutions.models.CompanyLogo;
import com.tuespotsolutions.models.FiltersList;
import com.tuespotsolutions.models.HomePagePills;
import com.tuespotsolutions.models.HomePagePillsValue;
import com.tuespotsolutions.models.HomePageRandomJobs;
import com.tuespotsolutions.models.JobFilterModeR;
import com.tuespotsolutions.repository.CompanyRepository;
import com.tuespotsolutions.repository.FilterRepository;
import com.tuespotsolutions.repository.FilterValueRepository;
import com.tuespotsolutions.repository.JobFilterRepository;
import com.tuespotsolutions.repository.JobFilterValuesRepository;
import com.tuespotsolutions.repository.JobRepository;
import com.tuespotsolutions.service.HomePageService;

@Service
public class HomePageServiceImpl implements HomePageService{
	
	
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
	
	@Autowired
	CompanyRepository companyRepository;

	
	@Value("${file.get.url}")
	private String jobLogoUrl;

	@Override
	public List<HomePageRandomJobs> getRendomJobForHomePage() {
		
		List<Job> randomJobs = this.jobRepository.getRandomJobs();
		
		List<HomePageRandomJobs> jobResponses = new ArrayList<HomePageRandomJobs>();
		randomJobs.forEach(data->{
			HomePageRandomJobs homePageRandomJobs = new HomePageRandomJobs();
			homePageRandomJobs.setCompanyName(data.getCompany().getName());
			homePageRandomJobs.setId(data.getId());
			homePageRandomJobs.setJobTitle(data.getTitle());
			homePageRandomJobs.setLocation(data.getLocation());
			homePageRandomJobs.setSkillSet(data.getSkills());
			homePageRandomJobs.setLogo(jobLogoUrl+data.getCompany().getLogo());
			jobResponses.add(homePageRandomJobs);
		});
		return jobResponses;
	}

	@Override
	public List<HomePagePills> getFilterList() {
			List<Job> findAll = this.jobRepository.findAll();
			
			List<JobFilter> listofjobFilterByid = new ArrayList<JobFilter>();

			for (Job job : findAll) {
				System.err.println("job Id : " + job.getId());
				listofjobFilterByid.addAll(jobFilterRepository.findByJob(job));
			}

			// jobfilter table with filter id and value
			List<Filters> allfilter = filterRepository.findAll();

			// all filter list
			Map<Long, List<HomePagePillsValue>> jobMap = new LinkedHashMap<Long, List<HomePagePillsValue>>();
			for (Filters fl : allfilter) {

				List<HomePagePillsValue> ls = new ArrayList<HomePagePillsValue>();

				List<FilterValues> filterValues = fl.getFilterValues();
				for (FilterValues flv : filterValues) {
					HomePagePillsValue obj = new HomePagePillsValue();
					obj.setId(flv.getId());
					obj.setPillsValue(flv.getFilterValue());
					ls.add(obj);
				}

				jobMap.put(fl.getId(), ls);
			}

			// System.out.println("map "+jobMap);

			for (JobFilter filter : listofjobFilterByid) {
				;
				Long filterid = filter.getFilter().getId();
				List<HomePagePillsValue> list = jobMap.get(filterid);
				List<HomePagePillsValue> listUpdate = new ArrayList<HomePagePillsValue>();
				Long filterValueID = filter.getFilterValue().getId();
				for (HomePagePillsValue ls : list) {
					HomePagePillsValue obj = new HomePagePillsValue();
					obj.setId(ls.getId());
					obj.setPillsValue(ls.getPillsValue());
					if (ls.getId() == filterValueID) {
						
					}

					listUpdate.add(obj);
				}

				jobMap.put(filterid, listUpdate);

			}

			List<HomePagePills> res = new ArrayList<HomePagePills>();
			for (Entry<Long, List<HomePagePillsValue>> entry : jobMap.entrySet()) {
				Long key = entry.getKey();
				Filters filters = this.filterRepository.findById(key)
						.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
				HomePagePills filtersList = new HomePagePills();
				filtersList.setId(filters.getId());
				filtersList.setPillsTitle(filters.getFilterName());
				List<HomePagePillsValue> list = jobMap.get(key);
				filtersList.setPillsValue(list);
				 res.add(filtersList);
			}
		return res;
	}

	@Override
	public List<CompanyLogo> companyLogo() {
		List<Company> randomCompanyLogos = this.companyRepository.getRandomCompanyLogos();
		
		List<CompanyLogo> companyLogos = new ArrayList<CompanyLogo>();
		randomCompanyLogos.forEach(data->{
			CompanyLogo companyLogo = new CompanyLogo();
			companyLogo.setId(data.getId());
			companyLogo.setLogo(jobLogoUrl+data.getLogo());
			companyLogos.add(companyLogo);
		});
		return companyLogos;
	}

}
