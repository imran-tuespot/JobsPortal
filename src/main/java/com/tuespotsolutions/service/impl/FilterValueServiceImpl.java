package com.tuespotsolutions.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tuespotsolutions.customexception.ResourceNotFoundException;
import com.tuespotsolutions.entity.FilterValues;
import com.tuespotsolutions.entity.Filters;
import com.tuespotsolutions.models.FilterResponseForFilterValue;
import com.tuespotsolutions.models.FilterValueResponse;
import com.tuespotsolutions.models.FilterValueResponseWithPagination;
import com.tuespotsolutions.models.FilterValuesRequest;
import com.tuespotsolutions.repository.FilterRepository;
import com.tuespotsolutions.repository.FilterValueRepository;
import com.tuespotsolutions.service.FilterValueService;

@Service
public class FilterValueServiceImpl implements FilterValueService {

	@Autowired
	private FilterValueRepository filterValueRepository;

	@Autowired
	private FilterRepository filterRepository;

	@Override
	public List<FilterValueResponse> addFilterValue(List<FilterValuesRequest> filterValuesRequest) {

		List<FilterValues> filterValues = new ArrayList<FilterValues>();
		filterValuesRequest.forEach(data->{
			FilterValues filterValue = new FilterValues();
			filterValue.setFilterValue(data.getFilterValue());
			Filters filters = this.filterRepository.findById(data.getFilterId())
					.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
			filterValue.setFilter(filters);
			filterValues.add(filterValue);
		});
		List<FilterValues> saveAll = this.filterValueRepository.saveAll(filterValues);
		List<FilterValueResponse> filterValueResponses = new ArrayList<FilterValueResponse>();
		saveAll.forEach(data->{
			FilterValueResponse filterValueResponse = new FilterValueResponse();
			filterValueResponse.setFilterValue(data.getFilterValue());
			filterValueResponse.setId(data.getId());
			filterValueResponses.add(filterValueResponse);
		});

		return filterValueResponses;
	}

	@Override
	public FilterValueResponse updateFilterValue(FilterValuesRequest filterValuesRequest) {

		FilterValues filterValues = new FilterValues();
		filterValues.setId(filterValuesRequest.getId());
		filterValues.setFilterValue(filterValuesRequest.getFilterValue());
		Filters filters = this.filterRepository.findById(filterValuesRequest.getFilterId())
				.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
		filterValues.setFilter(filters);
		FilterValues save = this.filterValueRepository.save(filterValues);

		FilterValueResponse filterValueResponse = new FilterValueResponse();
		filterValueResponse.setFilterValue(save.getFilterValue());
		filterValueResponse.setId(save.getId());

		return filterValueResponse;
	}

	@Override
	public FilterValueResponseWithPagination getFilterList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<FilterValues> findAll = this.filterValueRepository.findAll(pageable);

		List<FilterValueResponse> filterValueResponses = new ArrayList<FilterValueResponse>();
		findAll.getContent().forEach(data -> {
			FilterValueResponse filterValueResponse = new FilterValueResponse();
			filterValueResponse.setFilterValue(data.getFilterValue());
			filterValueResponse.setId(data.getId());
			Filters filters = this.filterRepository.findById(data.getFilter().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
			FilterResponseForFilterValue filterResponse = new FilterResponseForFilterValue();
			filterResponse.setId(filters.getId());
			filterResponse.setFilteName(filters.getFilterName());
			filterValueResponse.setFilterResponse(filterResponse);
			filterValueResponses.add(filterValueResponse);
		});

		FilterValueResponseWithPagination filterValueResponseWithPagination = new FilterValueResponseWithPagination();
		filterValueResponseWithPagination.setContent(filterValueResponses);
		filterValueResponseWithPagination.setLastPage(findAll.isLast());
		filterValueResponseWithPagination.setPageNumber(findAll.getNumber());
		filterValueResponseWithPagination.setPageSize(findAll.getSize());
		filterValueResponseWithPagination.setTotalElement(findAll.getTotalElements());
		filterValueResponseWithPagination.setTotalPages(findAll.getTotalPages());
		return filterValueResponseWithPagination;
	}

	@Override
	public FilterValueResponse getFilterById(Long id) {
		FilterValues filterValues = this.filterValueRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filter Value Not Found"));
		FilterValueResponse filterValueResponse = new FilterValueResponse();
		filterValueResponse.setFilterValue(filterValues.getFilterValue());
		filterValueResponse.setId(filterValues.getId());
		Filters filters = this.filterRepository.findById(filterValues.getFilter().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Filter Not Found"));
		FilterResponseForFilterValue filterResponse = new FilterResponseForFilterValue();
		filterResponse.setId(filters.getId());
		filterResponse.setFilteName(filters.getFilterName());
		filterValueResponse.setFilterResponse(filterResponse);
		return filterValueResponse;
	}

	@Override
	public void deleteFilterValue(Long id) {
		FilterValues filterValues = this.filterValueRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filter Value Not Found"));
		this.filterValueRepository.delete(filterValues);
	}

}
