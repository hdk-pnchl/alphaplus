
package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.service.impl.JobServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/job")
public class JobController implements ResourceLoaderAware {
	// instance
	
	@Autowired
	private JobServiceImpl jobService;
	
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	// setter-getter
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	// web
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Response save(@RequestBody JobEntity job) {
		job = jobService.save(job);
		Response response = new Response();
		response.setResponseEntity(job);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody JobEntity job) {
		job = jobService.update(job);
		Response response = new Response();
		response.setResponseEntity(job);
		return response;
	}
	
	@RequestMapping(value="/saveOrUpdate", method=RequestMethod.POST,
			consumes="application/json",produces="application/json")
    public @ResponseBody Response saveOrUpdate(@RequestBody JobEntity job) {
		job = jobService.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		return response;
    }
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("jobID") long jobId) {
		JobEntity job = jobService.get(jobId, JobEntity.class);
		Response response = new Response();
		response.setResponseEntity(job);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<JobEntity> list() {
		return jobService.list(JobEntity.class);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		List<JobEntity> jobList = jobService.search(searchInput, JobEntity.class);
		long rowCount = jobService.getTotalRowCount(searchInput, JobEntity.class);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		
		Response response = new Response();
		response.setResponseData(respMap);
		response.setResponseEntity(jobList);
		
		return response;
	}
	
	// data
	
	/**
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getColumnData() throws IOException {
		Resource jobColumnJson = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			if(CommonUtil.isAdmin(authorities)) {
				jobColumnJson = this.resourceLoader.getResource("classpath:data/json/job/columnDataAdmin.json");
			}
			else {
				jobColumnJson = this.resourceLoader.getResource("classpath:data/json/job/columnDataMember.json");
			}
		}
		List<Object> jobColumnData = objectMapper.readValue(jobColumnJson.getFile(), List.class);
		return jobColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getWizzardData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWizzardData() throws IOException {
		Resource jobFormData = this.resourceLoader.getResource("classpath:data/json/job/wizzardData.json");
		Map<String, Object> formDataMap = objectMapper.readValue(jobFormData.getFile(), Map.class);
		return formDataMap;
	}
}