package com.kanuhasu.ap.web.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.service.impl.user.ContactServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping("/contact")
public class ContactController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(ContactController.class);
	@Autowired
	private ContactServiceImpl contactService;
	private ResourceLoader resourceLoader;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	// setter-getter
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Response save(@RequestBody ContactEntity contact) {
		contact = contactService.save(contact);
		return Response.builder().responseEntity(contact).build();
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody ContactEntity contact) {
		contact = contactService.update(contact);
		return Response.builder().responseEntity(contact).build();
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody ContactEntity get(@RequestParam("contactID") long contactID) {
		logger.info("Fetch contact for: [" + contactID + "]");
		ContactEntity contact = contactService.get(contactID);
		return contact;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		List<ContactEntity> contactList = contactService.search(searchInput, ContactEntity.class);
		long rowCount = contactService.getTotalRowCount(searchInput, ContactEntity.class);
		
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(), String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));
		
		Response response = new Response();
		response.setResponseData(respMap);
		response.setResponseEntity(contactList);
		
		return response;
	}
	
	// data
	
	/**
	 * http://localhost:8080/alphaplus/ctrl/client/getColumnData
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnData", method = RequestMethod.GET)
	public @ResponseBody List<Object> getColumnData() throws IOException {
		List<Object> addressColumnData= null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(CommonUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			Resource addressColumnJson = null;
			if(CommonUtil.isAdmin(authorities)) {
				addressColumnJson = this.resourceLoader.getResource("classpath:data/json/contact/columnDataAdmin.json");
			}
			else {
				addressColumnJson = this.resourceLoader.getResource("classpath:data/json/contact/columnDataMember.json");
			}
			if(addressColumnJson!=null){
				addressColumnData = objectMapper.readValue(addressColumnJson.getFile(), List.class);				
			}
		}
		if(addressColumnData== null){
			addressColumnData= new ArrayList<Object>();
		}
		return addressColumnData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Resource clientFormData = this.resourceLoader.getResource("classpath:data/json/contact/formData.json");
		Map<String, Object> messageFormDataMap = objectMapper.readValue(clientFormData.getFile(), Map.class);
		return messageFormDataMap;
	}
}