package com.kanuhasu.ap.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.IDDetailEntity;
import com.kanuhasu.ap.business.service.impl.user.IDDetailServiceImpl;

@CrossOrigin
@Controller
@RequestMapping("/user/idDetail")
public class IDDetailController {
	@Autowired
	private IDDetailServiceImpl idDetailService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Response save(@RequestBody IDDetailEntity idDetail) {
		idDetailService.save(idDetail);
		return Response.builder().responseEntity(idDetail).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody IDDetailEntity idDetail) {
		idDetailService.update(idDetail);
		return Response.builder().responseEntity(idDetail).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody IDDetailEntity get(@RequestParam("idDetailID") long idDetailID) {
		IDDetailEntity idDetail = idDetailService.get(idDetailID, IDDetailEntity.class);
		return idDetail;
	}
}