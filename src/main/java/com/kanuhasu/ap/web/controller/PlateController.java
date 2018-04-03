package com.kanuhasu.ap.web.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.pojo.Plate;
import com.kanuhasu.ap.business.service.impl.PlateServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/plate")
public class PlateController {
	// instance

	@Autowired
	private PlateServiceImpl service;

	@RequestMapping(value = "/empty", method = RequestMethod.GET)
	public @ResponseBody Response emptyUser() {
		return service.empty();
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public @ResponseBody Response newUser(@RequestBody Plate pojo) {
		return service.newPlate(pojo);
	}

	@RequestMapping(value = "/basic", method = RequestMethod.POST)
	public @ResponseBody Response basic(@RequestBody Plate pojo) {
		return service.update(pojo);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long id) {
		return service.get(id);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		return service.search(searchInput);
	}
}