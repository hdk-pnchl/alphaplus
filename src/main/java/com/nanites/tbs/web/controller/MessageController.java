package com.nanites.alphaplus.web.controller;

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
import com.nanites.alphaplus.business.bo.MessageEntity;
import com.nanites.alphaplus.business.bo.ResponseEntity;
import com.nanites.alphaplus.business.service.impl.MessageServiceImpl;
import com.nanites.alphaplus.business.type.response.Param;
import com.nanites.alphaplus.business.util.CommonUtil;
import com.nanites.alphaplus.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/message")
public class MessageController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(MessageController.class);
	
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
	public @ResponseBody ResponseEntity save(@RequestBody MessageEntity message) {
		message = messageService.save(message);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(message);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody MessageEntity message) {
		message = messageService.update(message);
		ResponseEntity response = new ResponseEntity();
		response.setResponseEntity(message);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity get(@RequestParam("messageID") long messageId) {
		MessageEntity message = messageService.get(messageId);
		ResponseEntity response = new ResponseEntity();
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
	
	@RequestMapping(value = "/listBySeach", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity listBySeach(@RequestBody SearchInput searchInput) {
		if(!CommonUtil.isAdmin()) {
			searchInput.getSearchData().put(Param.EMAIL_ID.getDesc(), CommonUtil.fetchLoginID());
		}
		
		List<MessageEntity> messageList = messageService.getAll(searchInput);
		long rowCount = messageService.getTotalRowCount(searchInput);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.getDesc(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.getDesc(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.getDesc(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.getDesc(), String.valueOf(searchInput.getRowsPerPage()));
		
		ResponseEntity response = new ResponseEntity();
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
		List<Object> patientColumnData = objectMapper.readValue(messageColumnJson.getFile(), List.class);
		logger.info(patientColumnData);
		return patientColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource messageFormData = this.resourceLoader.getResource("classpath:data/json/message/messageFormData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(messageFormData.getFile(), Map.class);
		logger.info(messageFormDataMap);
		return messageFormDataMap;
	}
}