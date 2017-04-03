package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.ResponseEntity;
import com.kanuhasu.ap.business.bo.job.JobInstructionEntity;
import com.kanuhasu.ap.business.service.impl.JobInstServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/jobInst")
public class JobInstController implements ResourceLoaderAware {
	// instance
	
	@Autowired
	private JobInstServiceImpl jobInstService;
	
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
	public @ResponseBody ResponseEntity save(@RequestBody JobInstructionEntity jobInst, long jobID) {
		jobInst = jobInstService.save(jobInst, jobID);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(jobInst);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody JobInstructionEntity jobInst) {
		jobInst = jobInstService.update(jobInst);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(jobInst);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity get(@RequestParam("jobInstId") long jobInstId) {
		JobInstructionEntity jobInst = jobInstService.get(jobInstId);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(jobInst);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<JobInstructionEntity> list() {
		return jobInstService.list();
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity search(@RequestBody SearchInput searchInput) throws ParseException {
		List<JobInstructionEntity> jobInstList = jobInstService.search(searchInput);
		long rowCount = jobInstService.getTotalRowCount(searchInput);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.val(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.val(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.val(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.val(), String.valueOf(searchInput.getRowsPerPage()));
		
		ResponseEntity response = new ResponseEntity();
		response.setResponseData(respMap);
		response.setResponseEntity(jobInstList);
		
		return response;
	}
	
	// data
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getColumnData() throws IOException {
		Resource jobInstColumnJson = this.resourceLoader.getResource("classpath:data/json/job/inst/jobInstColumnDataMember.json");
		List<Object> jobInstColumnData = objectMapper.readValue(jobInstColumnJson.getFile(), List.class);
		return jobInstColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource jobInstFormData = this.resourceLoader.getResource("classpath:data/json/job/inst/jobInstFormData.json");
		Map<String, Object> jobInstFormDataMap = objectMapper.readValue(jobInstFormData.getFile(), Map.class);
		return jobInstFormDataMap;
	}
}