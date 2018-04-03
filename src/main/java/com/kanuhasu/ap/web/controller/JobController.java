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
import com.kanuhasu.ap.business.pojo.Bill;
import com.kanuhasu.ap.business.pojo.Challan;
import com.kanuhasu.ap.business.pojo.Ctp;
import com.kanuhasu.ap.business.pojo.Delivery;
import com.kanuhasu.ap.business.pojo.Docket;
import com.kanuhasu.ap.business.pojo.Instruction;
import com.kanuhasu.ap.business.pojo.Plate;
import com.kanuhasu.ap.business.pojo.Studio;
import com.kanuhasu.ap.business.service.impl.JobServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/job")
public class JobController {
	/** ------------| instance |------------ **/
	@Autowired
	private JobServiceImpl service;

	/** ------------| business |------------ **/

	@RequestMapping(value = "/empty", method = RequestMethod.GET)
	public @ResponseBody Response emptyUser() {
		return service.empty();
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public @ResponseBody Response newUser(@RequestBody Docket docket) {
		return service.newJob(docket);
	}

	@RequestMapping(value = "/docket", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response basicSaveOrUpdate(@RequestBody Docket docket) {
		return new Response(service.updateDocket(docket));
	}

	@RequestMapping(value = "/instruction", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response instSaveOrUpdate(@RequestBody Instruction instruction) {
		return service.updateInstruction(instruction);
	}

	@RequestMapping(value = "/plate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response plateSaveOrUpdate(@RequestBody Plate plate) {
		return service.updatePlate(plate);
	}

	@RequestMapping(value = "/studio", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response studioSaveOrUpdate(@RequestBody Studio studio) {
		return service.updateStudio(studio);
	}

	@RequestMapping(value = "/ctp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response ctpSaveOrUpdate(@RequestBody Ctp ctp) {
		return service.updateCtp(ctp);
	}

	@RequestMapping(value = "/delivery", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response deliverySaveOrUpdate(@RequestBody Delivery delivery) {
		return service.updateDelivery(delivery);
	}

	@RequestMapping(value = "/bill", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response billSaveOrUpdate(@RequestBody Bill bill) {
		return service.updateBill(bill);
	}

	@RequestMapping(value = "/challan", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody Response challanSaveOrUpdate(@RequestBody Challan challan) {
		return service.updateChallan(challan);
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