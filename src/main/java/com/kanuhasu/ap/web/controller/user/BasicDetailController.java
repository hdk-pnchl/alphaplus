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
import com.kanuhasu.ap.business.bo.user.BasicDetailEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.user.BasicDetailServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/user/basicDetail")
public class BasicDetailController {
	private static final Logger logger = Logger.getLogger(BasicDetailController.class);
	@Autowired
	private BasicDetailServiceImpl basicDetailService;
	
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
	public @ResponseBody ResponseEntity save(@RequestBody BasicDetailEntity basicDetail, @RequestParam("userID") long userID) {
		logger.info(basicDetail);
		UserEntity user = basicDetailService.save(basicDetail, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody BasicDetailEntity basicDetail, @RequestParam("userID") long userID) {
		logger.info("["+userID+"]: "+basicDetail);
		UserEntity user = basicDetailService.update(basicDetail, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody BasicDetailEntity get(@RequestParam("basicDetailID") long basicDetailID) {
		logger.info("["+basicDetailID+"]");
		BasicDetailEntity basicDetail = basicDetailService.get(basicDetailID);
		return basicDetail;
	}
}