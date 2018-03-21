package com.kanuhasu.ap.web.controller.user;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Alert;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.user.UserServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

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

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Response saveOrUpdate(@RequestBody UserEntity user) {
		Response response = null;
		if (this.isValidRequest(user)) {
			// new user
			if (user.getId() == null) {
				// is emailID already taken
				UserEntity existingUser = userService.getByEmailID(user.getEmailID());
				if (existingUser == null) {
					user.setPassword(user.getRegNO().toString());
					user = userService.saveOrUpdate(user);
					response = Response.Success();
					response.setResponseEntity(user);
				} else {
					response = Response.Fail();
					response.addAlert(Alert.danger(Param.Error.EMAIL_ID_TAKEN.desc()));
				}
			}
			// existing user
			else {
				// user account access-detail and roles should not be managed from UI.
				UserEntity existingUser = userService.get(user.getId(), UserEntity.class);
				user.setAccountNonExpired(existingUser.isAccountNonExpired());
				user.setAccountNonLocked(existingUser.isAccountNonLocked());
				user.setCredentialsNonExpired(existingUser.isCredentialsNonExpired());
				user.setRoles(existingUser.getRoles());

				user = userService.saveOrUpdate(user);
				response = Response.Success();
				response.setResponseEntity(user);
			}
		}else {
			response = Response.Fail();
			response.addAlert(Alert.danger(Param.Error.INVALID_REQUEST.desc()));
		}
		return response;
	}

	private boolean isValidRequest(UserEntity user) {
		boolean isValidRequest = true;
		if (StringUtils.isBlank(user.getEmailID()) || StringUtils.isBlank(user.getPassword())) {
			isValidRequest = false;
		}
		return isValidRequest;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long id) {
		UserEntity user = userService.get(id, UserEntity.class);
		Response response = new Response();
		response.setResponseEntity(user);
		return response;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<UserEntity> list() {
		return userService.list(UserEntity.class);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		if (!CommonUtil.isAdmin()) {
			searchInput.getSearchData().get(0).put(Param.EMAIL_ID.name(), CommonUtil.fetchLoginID());
		}

		List<UserEntity> list = userService.search(searchInput, UserEntity.class);
		long rowCount = userService.getTotalRowCount(searchInput, UserEntity.class);

		Map<String, Object> respMap = new HashMap<String, Object>();
		respMap.put(Param.ROW_COUNT.name(), rowCount);
		respMap.put(Param.CURRENT_PAGE_NO.name(), searchInput.getPageNo());
		respMap.put(Param.TOTAL_PAGE_COUNT.name(),CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage()));
		respMap.put(Param.ROWS_PER_PAGE.name(), searchInput.getRowsPerPage());

		Response response = new Response();
		response.setResponseData(respMap);
		response.setResponseEntity(list);

		return response;
	}

	@RequestMapping(value = "/seachByName", method = RequestMethod.GET)
	public @ResponseBody Response seachByName(@RequestParam("name") String name) {
		List<UserEntity> list = userService.getAllByName(name);
		Response response = new Response();
		response.setResponseEntity(list);
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
		Resource columnJson = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			if (CommonUtil.isAdmin(authorities)) {
				columnJson = this.resourceLoader.getResource("classpath:data/json/user/adminColumnData.json");
			} else {
				columnJson = this.resourceLoader.getResource("classpath:data/json/user/memberColumnData.json");
			}
		}
		List<Object> columnData = objectMapper.readValue(columnJson.getInputStream(), List.class);
		return columnData;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getWizzardData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWizzardData() throws IOException {
		Resource wizzardDataResource = this.resourceLoader.getResource("classpath:data/json/user/wizzardData.json");
		Map<String, Object> wizzardDataMap = objectMapper.readValue(wizzardDataResource.getInputStream(), Map.class);

		return wizzardDataMap;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource formData = this.resourceLoader.getResource("classpath:data/json/user/formData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(formData.getInputStream(), Map.class);
		return messageFormDataMap;
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody Response updatePassword(@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword) throws ClassNotFoundException, IOException {
		UserEntity user = userService.getByEmailID(CommonUtil.fetchLoginID());
		if (StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(newPassword)
				&& currentPassword.equals(user.getPassword())) {
			user.setPassword(newPassword);
			userService.update(user);
			return Response.Success();
		}
		return Response.Fail();
	}
}
