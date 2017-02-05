package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import com.kanuhasu.ap.business.bo.ResponseEntity;
import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.service.impl.JobServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/job")
public class JobController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(JobController.class);
	
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
	public @ResponseBody ResponseEntity save(@RequestBody JobEntity job) {
		job = jobService.save(job);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(job);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody JobEntity job) {
		job = jobService.update(job);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(job);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity get(@RequestParam("messageID") long jobId) {
		JobEntity job = jobService.get(jobId);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(job);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<JobEntity> list() {
		return jobService.list();
	}
	
	@RequestMapping(value = "/seach", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity listBySeach(@RequestBody SearchInput searchInput) {
		if(!CommonUtil.isAdmin()) {
			searchInput.getSearchData().put(Param.EMAIL_ID.getDesc(), CommonUtil.fetchLoginID());
		}
		
		List<JobEntity> jobList = jobService.search(searchInput);
		long rowCount = jobService.getTotalRowCount(searchInput);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.getDesc(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.getDesc(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.getDesc(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.getDesc(), String.valueOf(searchInput.getRowsPerPage()));
		
		ResponseEntity response = new ResponseEntity();
		response.setResponseData(respMap);
		response.setResponseEntity(jobList);
		
		return response;
	}
	
	// data
	
	/**
	 * http://localhost:8080/alphaplus/ctrl/message/getColumnData
	 * 
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
				jobColumnJson = this.resourceLoader.getResource("classpath:data/json/job/jobColumnDataAdmin.json");
			}
			else {
				jobColumnJson = this.resourceLoader.getResource("classpath:data/json/job/jobColumnDataMember.json");
			}
		}
		List<Object> jobColumnData = objectMapper.readValue(jobColumnJson.getFile(), List.class);
		return jobColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getWizzardData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWizzardData() throws IOException {
		Resource jobFormData = this.resourceLoader.getResource("classpath:data/json/job/jobWizzardData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(jobFormData.getFile(), Map.class);
		return messageFormDataMap;
	}
}