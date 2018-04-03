package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
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
import com.kanuhasu.ap.business.pojo.User;
import com.kanuhasu.ap.business.service.impl.MessageServiceImpl;
import com.kanuhasu.ap.business.service.impl.TestServiceImpl;
import com.kanuhasu.ap.business.service.impl.user.UserServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.AuthUtil;

@CrossOrigin
@Controller
@RequestMapping("/core")
public class CoreController implements ResourceLoaderAware {
	// instance

	private ResourceLoader resourceLoader;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private MessageServiceImpl messageService;
	@Autowired
	private TestServiceImpl testService;

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
		if (AuthUtil.isAuth()) {
			if (AuthUtil.isAdmin()) {
				bannerJson = this.resourceLoader.getResource("classpath:data/json/banner/bannerDataAdmin.json");
			} else {
				bannerJson = this.resourceLoader.getResource("classpath:data/json/banner/bannerDataMember.json");
			}
			User user = userService.getByEmailID(AuthUtil.fetchAuthName());
			bannerData = objectMapper.readValue(bannerJson.getInputStream(), Map.class);
			bannerData.put(Param.USER_DATA.name(), user);
		} else {
			bannerJson = this.resourceLoader.getResource("classpath:data/json/banner/bannerDataGuest.json");
			bannerData = objectMapper.readValue(bannerJson.getInputStream(), Map.class);
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
	public @ResponseBody User getUserData() throws IOException {
		User user = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (AuthUtil.isAuth(auth)) {
			user = userService.getByEmailID(auth.getName());
		}
		return user;
	}

	// web

	/**
	 * http://localhost:8080/alphaplus/ctrl/core/isEmailIdTaken?emailId=hdk.pnchl@gmail.com
	 */
	@RequestMapping(value = "/isEmailIdTaken", method = RequestMethod.POST)
	public @ResponseBody Map<String, Boolean> isEmailIdTaken(@RequestParam("emailID") String emailID) {
		User user = userService.getByEmailID(emailID);
		Map<String, Boolean> responseMap = new HashMap<String, Boolean>();
		responseMap.put(Param.IS_EMAILID_TAKEN.name(), (user != null) ? true : false);
		return responseMap;
	}

	@RequestMapping(value = "/saveMessage", method = RequestMethod.POST)
	public @ResponseBody Response saveMessage(@RequestBody MessageEntity message) {
		message = messageService.save(message);
		Response response = Response.Success();
		response.setResponseEntity(message);
		return response;
	}

	@RequestMapping(value = "/initiatePasswordUpdate", method = RequestMethod.POST)
	public @ResponseBody Response initiatePasswordUpdate(HttpServletRequest request,
			@RequestParam("emailID") String emailID) throws ClassNotFoundException, IOException {
		return userService.initiatePasswordUpdate(request, emailID);
	}

	@RequestMapping(value = "/updateForgottenPassword", method = RequestMethod.POST)
	public @ResponseBody Response updateForgottenPassword(HttpServletRequest request,
			@RequestParam("token") String token, @RequestParam("newPassword") String password)
			throws ClassNotFoundException, IOException {
		return userService.updateForgottenPassword(token, password);
	}

	@RequestMapping(value = "/makeItAdmin", method = RequestMethod.GET)
	public @ResponseBody Response makeItAdmin(@RequestParam("emailID") String emailID)
			throws ClassNotFoundException, IOException {
		return userService.makeItAdmin(emailID);
	}

	@RequestMapping(value = "/popuateTestData", method = RequestMethod.GET)
	public @ResponseBody Response popuateTestData(@RequestParam("count") int count) {
		return testService.popuateTestData(count);
	}

	@RequestMapping(value = "/popuateUsers", method = RequestMethod.GET)
	public @ResponseBody Response popuateUsers(@RequestParam("count") int count) {
		return testService.populateUsers(count);
	}

	@RequestMapping(value = "/popuateClients", method = RequestMethod.GET)
	public @ResponseBody Response popuateClients(@RequestParam("count") int count) {
		return testService.populateClients(count);
	}

	@RequestMapping(value = "/populateJobs", method = RequestMethod.GET)
	public @ResponseBody Response populateJobs(@RequestParam("count") int count) {
		return testService.populateJobs(count);
	}
}
