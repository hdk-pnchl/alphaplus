package com.kanuhasu.ap.web.controller.user;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.BasicDetailEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.service.impl.user.BasicDetailServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
	public @ResponseBody Response save(@RequestBody BasicDetailEntity basicDetail, @RequestParam("userID") long userID) {
		logger.info(basicDetail);
		UserEntity user = basicDetailService.save(basicDetail, userID);
		return Response.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody BasicDetailEntity basicDetail, @RequestParam("userID") long userID) {
		logger.info("["+userID+"]: "+basicDetail);
		UserEntity user = basicDetailService.update(basicDetail, userID);
		return Response.builder().responseEntity(user).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody BasicDetailEntity get(@RequestParam("basicDetailID") long basicDetailID) {
		logger.info("["+basicDetailID+"]");
		BasicDetailEntity basicDetail = basicDetailService.get(basicDetailID, BasicDetailEntity.class);
		return basicDetail;
	}
}