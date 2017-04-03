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
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.service.impl.PlateServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/plate")
public class PlateController implements ResourceLoaderAware {
	// instance
	
	@Autowired
	private PlateServiceImpl plateService;
	
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
	public @ResponseBody ResponseEntity save(@RequestBody PlateEntity plate) {
		plate = plateService.save(plate);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody PlateEntity plate) {
		plate = plateService.update(plate);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity saveOrUpdate(@RequestBody PlateEntity plate) {
		plate = plateService.saveOrUpdate(plate);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity get(@RequestParam("plateId") long plateId) {
		PlateEntity plate = plateService.get(plateId);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<PlateEntity> list() {
		return plateService.list();
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity search(@RequestBody SearchInput searchInput) throws ParseException {
		List<PlateEntity> plateList = plateService.search(searchInput);
		long rowCount = plateService.getTotalRowCount(searchInput);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.val(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.val(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.val(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.val(), String.valueOf(searchInput.getRowsPerPage()));
		
		ResponseEntity response = new ResponseEntity();
		response.setResponseData(respMap);
		response.setResponseEntity(plateList);
		
		return response;
	}
	
	// data
	
	/**
	 * http://localhost:8080/alphaplus/ctrl/plate/getColumnData
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getColumnData() throws IOException {
		Resource jobColumnJson = this.resourceLoader.getResource("classpath:data/json/plate/plateColumnDataMember.json");
		List<Object> plateColumnData = objectMapper.readValue(jobColumnJson.getFile(), List.class);
		return plateColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource messageFormData = this.resourceLoader.getResource("classpath:data/json/plate/plateFormData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(messageFormData.getFile(), Map.class);
		return messageFormDataMap;
	}
}