package com.kanuhasu.ap.web.controller.user;

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

import com.kanuhasu.ap.business.bo.ResponseEntity;
import com.kanuhasu.ap.business.bo.user.OccupationEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.user.OccupationServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/user/occupation")
public class OccupationController {
	private static final Logger logger = Logger.getLogger(OccupationController.class);
	@Autowired
	private OccupationServiceImpl occupationService;
	
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
	public @ResponseBody ResponseEntity save(@RequestBody OccupationEntity occupation, @RequestParam("userID") long userID) {
		logger.info("[" + userID + "]: " + occupation);
		UserEntity user = occupationService.save(occupation, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody OccupationEntity occupation, @RequestParam("userID") long userID) {
		logger.info("[" + userID + "]: " + occupation);
		UserEntity user = occupationService.update(occupation, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody OccupationEntity get(@RequestParam("occupationID") long occupationID) {
		logger.info("[" + occupationID + "]");
		OccupationEntity occupation = occupationService.get(occupationID);
		return occupation;
	}
}