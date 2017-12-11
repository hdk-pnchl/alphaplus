package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kanuhasu.ap.business.bo.MessageEntity;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.cit.auth.CITUserEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.CoreServiceImpl;
import com.kanuhasu.ap.business.service.impl.MessageServiceImpl;
import com.kanuhasu.ap.business.service.impl.user.UserServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/core")
public class CoreController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(CoreController.class);

	// instance

	private ResourceLoader resourceLoader;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private CoreServiceImpl coreService;	
	@Autowired
	private MessageServiceImpl messageService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().write("Yes, It works");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// setter-getter

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	// data

	/**
	 * http://localhost:8080/alphaplus/ctrl/core/getBannerData
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getBannerData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBannerData() throws IOException {
		Map<String, Object> bannerData = null;
		Resource bannerJson = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			if (CommonUtil.isAdmin(authorities)) {
				bannerJson = this.resourceLoader.getResource("classpath:data/json/banner/bannerDataAdmin.json");
			} else {
				bannerJson = this.resourceLoader.getResource("classpath:data/json/banner/bannerDataMember.json");
			}
			UserEntity user = userService.getByEmailID(auth.getName());
			bannerData = objectMapper.readValue(bannerJson.getFile(), Map.class);
			bannerData.put(Param.USER_DATA.name(), user);
		} else {
			bannerJson = this.resourceLoader.getResource("classpath:data/json/banner/bannerDataGuest.json");
			bannerData = objectMapper.readValue(bannerJson.getFile(), Map.class);
		}
		return bannerData;
	}

	/**
	 * http://localhost:8080/alphaplus/ctrl/core/getUserData
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUserData", method = RequestMethod.GET)
	public @ResponseBody UserEntity getUserData() throws IOException {
		UserEntity basicDetail = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (CommonUtil.isAuth(auth)) {
			basicDetail = userService.getByEmailID(auth.getName());
		}
		return basicDetail;
	}

	// web

	/**
	 * http://localhost:8080/alphaplus/ctrl/core/signUp
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public @ResponseBody UserEntity signUp(@RequestBody UserEntity user) throws IOException {
		user = userService.save(user);
		return user;
	}

	/**
	 * http://localhost:8080/alphaplus/ctrl/core/isEmailIdTaken?emailId=hdk.pnchl@gmail.com
	 *
	 * @param emailID
	 * @return
	 */
	@RequestMapping(value = "/isEmailIdTaken", method = RequestMethod.POST)
	public @ResponseBody Map<String, Boolean> isEmailIdTaken(@RequestParam("emailID") String emailID) {
		UserEntity user = userService.getByEmailID(emailID);
		Map<String, Boolean> responseMap = new HashMap<String, Boolean>();
		responseMap.put(Param.IS_EMAILID_TAKEN.name(), (user != null) ? true : false);
		return responseMap;
	}

	@RequestMapping(value = "/saveMessage", method = RequestMethod.POST)
	public @ResponseBody Response saveMessage(@RequestBody MessageEntity message) {
		System.out.println("/Core" + " : " + "/save");
		message = messageService.save(message);
		Response response = Response.Success();
		response.setResponseEntity(message);
		return response;
	}

	@RequestMapping(value = "/initiatePasswordUpdate", method = RequestMethod.POST)
	public @ResponseBody Response initiatePasswordUpdate(HttpServletRequest request,
			@RequestParam("emailID") String emailID) throws ClassNotFoundException, IOException {
		UserEntity user = userService.getByEmailID(emailID);
		Map<String, String> respMap = new HashMap<String, String>();
		if (user != null) {
			String pwUpdateReqToken = UUID.randomUUID().toString();
			user.setChangePasswordReqToken(pwUpdateReqToken);
			user = userService.update(user);
			Map<String, String> pwUpdateReqMap = new HashMap<String, String>();
			pwUpdateReqMap.put(Param.EMAIL_ID.name(), emailID);
			pwUpdateReqMap.put(Param.PW_UPDATE_REQ_TOKEN.name(), pwUpdateReqToken);

			// http://localhost:8080/alphaplus-static/#/signIn
			StringBuilder pwUpdateReq = new StringBuilder(
					CommonUtil.buildUrl(request, "static/#/user/updateForgottenPassword/"));
			pwUpdateReq.append(CommonUtil.mapToString(pwUpdateReqMap));

			respMap.put(Param.STATUS.name(), Boolean.TRUE.toString());
			respMap.put(Param.PW_UPDATE_URL.name(), pwUpdateReq.toString());
		} else {
			respMap.put(Param.STATUS.name(), Boolean.FALSE.toString());
			respMap.put(Param.ERR_USER_DOESNT_EXISTS.name(), Boolean.TRUE.toString());
		}

		return Response.builder().responseData(respMap).build();
	}

	@RequestMapping(value = "/updateForgottenPassword", method = RequestMethod.POST)
	public @ResponseBody Response updateForgottenPassword(HttpServletRequest request,
			@RequestParam("token") String token, @RequestParam("newPassword") String password)
			throws ClassNotFoundException, IOException {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.STATUS.name(), Boolean.FALSE.toString());

		Map<String, String> pwUpdateReqMap = CommonUtil.stringToMap(token);
		String emailID = pwUpdateReqMap.get(Param.EMAIL_ID.name());
		if (emailID != null) {
			UserEntity user = userService.getByEmailID(emailID);
			if (user != null) {
				String reqToken = pwUpdateReqMap.get(Param.PW_UPDATE_REQ_TOKEN.name());
				if (!StringUtils.isEmpty(user.getChangePasswordReqToken()) && !StringUtils.isEmpty(reqToken)
						&& user.getChangePasswordReqToken().equals(reqToken)) {
					user.setPassword(password);
					userService.update(user);
					respMap.put(Param.STATUS.name(), Boolean.TRUE.toString());
				}
			} else {
				respMap.put(Param.ERR_USER_DOESNT_EXISTS.name(), Boolean.TRUE.toString());
			}
		}
		return Response.builder().responseData(respMap).build();
	}

	@RequestMapping(value = "/makeItAdmin", method = RequestMethod.GET)
	public @ResponseBody Response makeItAdmin(@RequestParam("emailID") String emailID)
			throws ClassNotFoundException, IOException {
		Map<String, String> respMap = new HashMap<String, String>();
		if (StringUtils.isEmpty(emailID)) {
			emailID = "hdk.pnchl@gmail.com";
			UserEntity user = userService.getByEmailID(emailID);
			if (user == null) {
				user = new UserEntity();
				user.setEmailID(emailID);
				user.setName("Hardik P");
				user.setPassword("1");
				userService.save(user);
				Boolean flag = userService.makeItAdmin(emailID);
				respMap.put(Param.STATUS.name(), flag.toString());
			} else {
				respMap.put(Param.STATUS.name(), "false");
				respMap.put(Param.ERROR.name(), Param.Error.EMAIL_ID_TAKEN.desc());
			}
		} else {
			Boolean flag = userService.makeItAdmin(emailID);
			respMap.put(Param.STATUS.name(), flag.toString());
		}
		return Response.builder().responseData(respMap).build();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public @ResponseBody List<Object> roles() throws IOException {
		Resource rolesJson = this.resourceLoader.getResource("classpath:data/json/test/roles.json");
		List<Object> roles = objectMapper.readValue(rolesJson.getFile(), List.class);
		return roles;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tools", method = RequestMethod.GET)
	public @ResponseBody List<Object> tools() throws IOException {
		Resource toolsJson = this.resourceLoader.getResource("classpath:data/json/test/tools.json");
		List<Object> tools = objectMapper.readValue(toolsJson.getFile(), List.class);
		return tools;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/networks", method = RequestMethod.GET)
	public @ResponseBody List<Object> networks() throws IOException {
		Resource networksJson = this.resourceLoader.getResource("classpath:data/json/test/networks.json");
		List<Object> networks = objectMapper.readValue(networksJson.getFile(), List.class);
		return networks;
	}

	/**
	 * http://localhost:8080/alphaplus/ctrl/user/getBanner
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getColumnData() throws IOException {
		Resource columnJson = this.resourceLoader.getResource("classpath:data/json/test/userColumnData.json");
		List<Object> columnData = objectMapper.readValue(columnJson.getFile(), List.class);
		return columnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> userForm() throws IOException {
		Resource formData = this.resourceLoader.getResource("classpath:data/json/test/userFormData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(formData.getFile(), Map.class);
		return messageFormDataMap;
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Response saveUser(@RequestBody CITUserEntity testUser) {
		logger.info(testUser);
		coreService.saveOrUpdate(testUser);
		Response response = Response.Success();
		response.setResponseEntity(testUser);
		return response;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		List<CITUserEntity> list = coreService.search(searchInput, CITUserEntity.class);
		long rowCount = coreService.getTotalRowCount(searchInput, CITUserEntity.class);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		
		Response response = new Response();
		response.setResponseData(respMap);
		response.setResponseEntity(list);
		
		return response;
	}	
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long id) {
		logger.info("[" + id + "]");
		CITUserEntity citUser = coreService.get(id, CITUserEntity.class);
		Response response = new Response();
		response.setResponseEntity(citUser);
		return response;
	}	
}
