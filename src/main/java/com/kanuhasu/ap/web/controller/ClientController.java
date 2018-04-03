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
import com.kanuhasu.ap.business.pojo.Address;
import com.kanuhasu.ap.business.pojo.ClientBasic;
import com.kanuhasu.ap.business.pojo.Contact;
import com.kanuhasu.ap.business.service.impl.ClientServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/client")
public class ClientController {
	@Autowired
	private ClientServiceImpl service;

	@RequestMapping(value = "/empty", method = RequestMethod.GET)
	public @ResponseBody Response emptyUser() {
		return service.empty();
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public @ResponseBody Response newClient(@RequestBody ClientBasic basic) {
		return service.newClient(basic);
	}

	@RequestMapping(value = "/basic", method = RequestMethod.POST)
	public @ResponseBody Response basic(@RequestBody ClientBasic basic) {
		return service.updateBasic(basic);
	}

	@RequestMapping(value = "/address", method = RequestMethod.POST)
	public @ResponseBody Response address(@RequestBody Address addressPojo) {
		return service.saveOrUpdateAddress(addressPojo);
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public @ResponseBody Response contact(@RequestBody Contact contactPojo) {
		return service.saveOrUpdateContact(contactPojo);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long id) {
		return service.get(id);
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		return service.search(searchInput);
	}

	@RequestMapping(value = "/seachByName", method = RequestMethod.GET)
	public @ResponseBody Response seachByName(@RequestParam("name") String name) {
		return service.searchByName(name);
	}
}