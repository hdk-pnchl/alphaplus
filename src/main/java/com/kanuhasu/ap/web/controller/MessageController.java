package com.kanuhasu.ap.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.MessageEntity;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.service.impl.MessageServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/message")
public class MessageController implements ResourceLoaderAware {
	// instance
	
	@Autowired
	private MessageServiceImpl messageService;
	
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
	public @ResponseBody Response save(@RequestBody MessageEntity message) {
		message = messageService.save(message);
		Response response = new Response();
		response.setResponseEntity(message);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody MessageEntity message) {
		message = messageService.update(message);
		Response response = new Response();
		response.setResponseEntity(message);
		return response;
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Response saveOrUpdate(@RequestBody MessageEntity message) {
		message = messageService.saveOrUpdate(message);
		Response response = new Response();
		response.setResponseEntity(message);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("messageID") long messageId) {
		MessageEntity message = messageService.get(messageId);
		Response response = new Response();
		response.setResponseEntity(message);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<MessageEntity> list() {
		List<MessageEntity> messageList = null;
		if(CommonUtil.isAdmin()) {
			messageList = messageService.list();
		}
		else {
			messageList = messageService.listByEmailID(CommonUtil.fetchLoginID());
		}
		return messageList;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		if(!CommonUtil.isAdmin()) {
			searchInput.getSearchData().get(0).put(Param.EMAIL_ID.name(), CommonUtil.fetchLoginID());
		}
		
		List<MessageEntity> messageList = messageService.search(searchInput, MessageEntity.class);
		long rowCount = messageService.getTotalRowCount(searchInput, MessageEntity.class);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		
		Response response = new Response();
		response.setResponseData(respMap);
		response.setResponseEntity(messageList);
		
		return response;
	}
	
	@RequestMapping(value = "/listByEmailID", method = RequestMethod.GET)
	public @ResponseBody List<MessageEntity> listByEmailID(@RequestParam("emailID") String emailId) {
		List<MessageEntity> messageList = messageService.listByEmailID(emailId);
		return messageList;
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
		Resource messageColumnJson = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			if(CommonUtil.isAdmin(authorities)) {
				messageColumnJson = this.resourceLoader.getResource("classpath:data/json/message/messageColumnDataAdmin.json");
			}
			else {
				messageColumnJson = this.resourceLoader.getResource("classpath:data/json/message/messageColumnDataMember.json");
			}
		}
		List<Object> messageColumnData = objectMapper.readValue(messageColumnJson.getFile(), List.class);
		return messageColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource messageFormData = this.resourceLoader.getResource("classpath:data/json/message/messageFormData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(messageFormData.getFile(), Map.class);
		return messageFormDataMap;
	}
}