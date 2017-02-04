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
import com.kanuhasu.ap.business.bo.user.IDDetailEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.user.IDDetailServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/user/idDetail")
public class IDDetailController {
	private static final Logger logger = Logger.getLogger(IDDetailController.class);
	@Autowired
	private IDDetailServiceImpl idDetailService;
	
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
	public @ResponseBody ResponseEntity save(@RequestBody IDDetailEntity idDetail, @RequestParam("userID") long userID) {
		logger.info("[" + userID + "]: " + idDetail);
		UserEntity user = idDetailService.save(idDetail, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity update(@RequestBody IDDetailEntity idDetail, @RequestParam("userID") long userID) {
		logger.info("[" + userID + "]: " + idDetail);
		UserEntity user = idDetailService.update(idDetail, userID);
		return ResponseEntity.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody IDDetailEntity get(@RequestParam("idDetailID") long idDetailID) {
		logger.info("[" + idDetailID + "]");
		IDDetailEntity idDetail = idDetailService.get(idDetailID);
		return idDetail;
	}
}