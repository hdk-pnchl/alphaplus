package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.text.ParseException;
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
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.service.impl.ClientServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/client")
public class ClientController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(ClientController.class);
	
	// instance
	
	@Autowired
	private ClientServiceImpl clientService;
	
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
	public @ResponseBody ResponseEntity save(@RequestBody ClientEntity client) {
		client = clientService.save(client);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(client);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody ClientEntity client) {
		client = clientService.update(client);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(client);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity get(@RequestParam("clientId") long clientId) {
		ClientEntity client = clientService.get(clientId);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(client);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<ClientEntity> list() {
		return clientService.list();
	}
	
	@RequestMapping(value = "/seach", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity seach(@RequestBody SearchInput searchInput) throws ParseException {
		List<ClientEntity> clientList = clientService.search(searchInput);
		long rowCount = clientService.getTotalRowCount(searchInput);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.val(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.val(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.val(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.val(), String.valueOf(searchInput.getRowsPerPage()));
		
		ResponseEntity response = new ResponseEntity();
		response.setResponseData(respMap);
		response.setResponseEntity(clientList);
		
		return response;
	}
	
	@RequestMapping(value = "/seachByName", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity seachByName(@RequestParam("name") String name) {
		List<ClientEntity> clientList = clientService.searchByName(name);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(clientList);
		return response;
	}
	
	// data
	
	/**
	 * http://localhost:8080/alphaplus/ctrl/client/getColumnData
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getColumnData() throws IOException {
		Resource clientColumnJson = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			if(CommonUtil.isAdmin(authorities)) {
				clientColumnJson = this.resourceLoader.getResource("classpath:data/json/client/clientColumnDataAdmin.json");
			}
			else {
				clientColumnJson = this.resourceLoader.getResource("classpath:data/json/client/clientColumnDataMember.json");
			}
		}
		List<Object> clientColumnData = objectMapper.readValue(clientColumnJson.getFile(), List.class);
		return clientColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getWizzardData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWizzardData() throws IOException {
		Resource clientWizzardData = this.resourceLoader.getResource("classpath:data/json/client/clientWizzardData.json");
		Map<String, Object> clientWizzardDataMap = objectMapper.readValue(clientWizzardData.getFile(), Map.class);
		return clientWizzardDataMap;
	}
}