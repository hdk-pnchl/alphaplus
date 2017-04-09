package com.kanuhasu.ap.web.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.user.UserServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
@RequestMapping("/user")
public class UserController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	// instance
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private ObjectMapper objectMapper;
	private ResourceLoader resourceLoader;
	
	// setter-getter
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	// web
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Response save(@RequestBody UserEntity user) {
		logger.info(user);
		user = userService.save(user);
		Response response = new Response();
		response.setResponseEntity(user);
		return response;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody UserEntity user) {
		user = userService.update(user);
		Response response = new Response();
		response.setResponseEntity(user);
		return response;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("userID") long userID) {
		UserEntity user = userService.get(userID);
		Response response = new Response();
		response.setResponseEntity(user);
		return response;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<UserEntity> list() {
		return userService.list();
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		if(!CommonUtil.isAdmin()) {
			searchInput.getSearchData().get(0).put(Param.EMAIL_ID.name(), CommonUtil.fetchLoginID());
		}
		
		List<UserEntity> complaintList = userService.search(searchInput, UserEntity.class);
		long rowCount = userService.getTotalRowCount(searchInput, UserEntity.class);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		
		Response response = new Response();
		response.setResponseData(respMap);
		response.setResponseEntity(complaintList);
		
		return response;
	}
	
	// data
	
	/**
	 * http://localhost:8080/alphaplus/ctrl/user/getBanner
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
				messageColumnJson = this.resourceLoader.getResource("classpath:data/json/user/userColumnDataAdmin.json");
			}
			else {
				messageColumnJson = this.resourceLoader.getResource("classpath:data/json/user/userColumnDataMember.json");
			}
		}
		List<Object> userColumnData = objectMapper.readValue(messageColumnJson.getFile(), List.class);
		return userColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getWizzardData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWizzardData() throws IOException {
		Resource messageFormData = this.resourceLoader.getResource("classpath:data/json/user/userWizzardData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(messageFormData.getFile(), Map.class);
		
		return messageFormDataMap;
	}
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody Response updatePassword(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword)
			throws ClassNotFoundException, IOException {
		UserEntity user = userService.get(CommonUtil.fetchLoginID());
		if(StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(newPassword) && currentPassword.equals(user.getBasicDetail().getPassword())) {
			user.getBasicDetail().setPassword(newPassword);
			userService.update(user);
			return Response.Success();
		}
		return Response.Fail();
	}
}
