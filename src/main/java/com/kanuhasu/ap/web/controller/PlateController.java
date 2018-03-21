package com.kanuhasu.ap.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.service.impl.PlateServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public @ResponseBody Response save(@RequestBody PlateEntity plate) {
		plate = plateService.save(plate);
		Response response = new Response();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody PlateEntity plate) {
		plate = plateService.update(plate);
		Response response = new Response();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Response saveOrUpdate(@RequestBody PlateEntity plate) {
		plate = plateService.saveOrUpdate(plate);
		Response response = new Response();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long id) {
		PlateEntity plate = plateService.get(id, PlateEntity.class);
		Response response = new Response();
		response.setResponseEntity(plate);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<PlateEntity> list() {
		return plateService.list(PlateEntity.class);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		List<PlateEntity> list = plateService.search(searchInput, PlateEntity.class);
		long rowCount = plateService.getTotalRowCount(searchInput, PlateEntity.class);
		
		Response response = new Response();
		Map<String, Object> respMap = response.getResponseData();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		
		response.setResponseEntity(list);
		
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
		Resource columnJson = this.resourceLoader.getResource("classpath:data/json/plate/columnDataMember.json");
		List<Object> columnData = objectMapper.readValue(columnJson.getInputStream(), List.class);
		return columnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource messageFormData = this.resourceLoader.getResource("classpath:data/json/plate/formData.json");
		Map<String, Object> formDataMap = objectMapper.readValue(messageFormData.getInputStream(), Map.class);
		return formDataMap;
	}
}