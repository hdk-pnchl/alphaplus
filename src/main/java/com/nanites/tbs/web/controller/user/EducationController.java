package com.nanites.alphaplus.web.controller.user;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nanites.alphaplus.business.bo.ResponseEntity;
import com.nanites.alphaplus.business.bo.user.EducationEntity;
import com.nanites.alphaplus.business.bo.user.UserEntity;
import com.nanites.alphaplus.business.service.impl.user.EducationServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/user/education")
public class EducationController {
	private static final Logger logger = Logger.getLogger(EducationController.class);
	@Autowired
	private EducationServiceImpl educationService;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().write("Yes, It works");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity save(@RequestBody EducationEntity education, @RequestParam("userID") long userID) {
		logger.info("[" + userID + "]: " + education);
		UserEntity user = educationService.save(education, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody EducationEntity education, @RequestParam("userID") long userID) {
		logger.info("[" + userID + "]: " + education);
		UserEntity user = educationService.update(education, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody EducationEntity get(@RequestParam("educationID") long educationID) {
		logger.info("[" + educationID + "]");
		EducationEntity education = educationService.get(educationID);
		return education;
	}
}