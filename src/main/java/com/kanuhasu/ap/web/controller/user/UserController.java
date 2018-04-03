package com.kanuhasu.ap.web.controller.user;

import java.io.IOException;
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
import com.kanuhasu.ap.business.pojo.Contact;
import com.kanuhasu.ap.business.pojo.UserBasic;
import com.kanuhasu.ap.business.pojo.UserIDDetail;
import com.kanuhasu.ap.business.service.impl.user.UserServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/user")
public class UserController {
	/** ------------| instance |------------ **/

	@Autowired
	private UserServiceImpl service;

	/** ------------| business |------------ **/

	@RequestMapping(value = "/empty", method = RequestMethod.GET)
	public @ResponseBody Response emptyUser() {
		return service.empty();
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public @ResponseBody Response newUser(@RequestBody UserBasic userBasic) {
		return service.newUser(userBasic);
	}

	@RequestMapping(value = "/basic", method = RequestMethod.POST)
	public @ResponseBody Response basic(@RequestBody UserBasic basic) {
		return service.updateBasic(basic);
	}

	@RequestMapping(value = "/idDetail", method = RequestMethod.POST)
	public @ResponseBody Response idDetail(@RequestBody UserIDDetail idDetail) {
		return service.updateIdDetail(idDetail);
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

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody Response updatePassword(@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword) throws ClassNotFoundException, IOException {
		return service.updatePassword(currentPassword, newPassword);
	}
}