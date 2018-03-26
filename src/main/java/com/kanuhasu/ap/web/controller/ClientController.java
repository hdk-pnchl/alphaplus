package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
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
import com.kanuhasu.ap.business.bo.Alert;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.service.impl.ClientServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/client")
public class ClientController implements ResourceLoaderAware {
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
	public @ResponseBody Response save(@RequestBody ClientEntity client) {
		client = clientService.save(client);
		Response response = new Response();
		response.setResponseEntity(client);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody ClientEntity client) {
		client = clientService.merge(client);
		Response response = new Response();
		response.setResponseEntity(client);
		return response;
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Response saveOrUpdate(@RequestBody ClientEntity client) {
		Response response= null;
		if(client.getId()==null){
			ClientEntity existingClient= clientService.getByEmailID(client.getEmailID());
			if(existingClient==null){
				response= Response.Success();				
				client = clientService.saveOrUpdate(client);
				response.setResponseEntity(client);							
			}else{
				response= Response.Fail();
				response.addAlert(Alert.danger(Param.Error.EMAIL_ID_TAKEN.desc()));				
			}			
		}else{
			response= Response.Success();			
			client = clientService.saveOrUpdate(client);
			response.setResponseEntity(client);			
		}
		return response;			
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long clientID) {
		ClientEntity client = clientService.get(clientID, ClientEntity.class);
		Response response = new Response();
		response.setResponseEntity(client);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<ClientEntity> list() {
		List<ClientEntity> list= clientService.list(ClientEntity.class); 
		return list;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		List<ClientEntity> list = clientService.search(searchInput);
		long rowCount = clientService.getTotalRowCount(searchInput);
		
		Response response = new Response();		
		Map<String, Object> respMap = response.getResponseData();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		response.setResponseData(respMap);
		response.setResponseEntity(list);
		
		return response;
	}
	
	@RequestMapping(value = "/seachByName", method = RequestMethod.GET)
	public @ResponseBody Response seachByName(@RequestParam("name") String name) {
		List<ClientEntity> list = clientService.getAllByName(name);
		Response response = new Response();
		response.setResponseEntity(list);
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
		Resource columnJson = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			if(CommonUtil.isAdmin(authorities)) {
				columnJson = this.resourceLoader.getResource("classpath:data/json/client/columnDataAdmin.json");
			}
			else {
				columnJson = this.resourceLoader.getResource("classpath:data/json/client/columnDataMember.json");
			}
		}
		List<Object> clientColumnData = objectMapper.readValue(columnJson.getInputStream(), List.class);
		return clientColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource formData = this.resourceLoader.getResource("classpath:data/json/client/formData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(formData.getInputStream(), Map.class);
		return messageFormDataMap;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getWizzardData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWizzardData() throws IOException {
		Resource wizzardData = this.resourceLoader.getResource("classpath:data/json/client/wizzardData.json");
		Map<String, Object> wizzardDataMap = objectMapper.readValue(wizzardData.getInputStream(), Map.class);
		return wizzardDataMap;
	}	
}