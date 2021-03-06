package com.kanuhasu.ap.web.controller.user;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.service.impl.user.AddressServiceImpl;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.AuthUtil;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@CrossOrigin
@Controller
@RequestMapping("/address")
public class AddressController implements ResourceLoaderAware {
	private static final Logger logger = Logger.getLogger(AddressController.class);
	@Autowired
	private AddressServiceImpl addressService;
	private ResourceLoader resourceLoader;

	@Autowired
	private ObjectMapper objectMapper;

	// setter-getter

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Response save(@RequestBody AddressEntity address) {
		address = addressService.save(address);
		return Response.builder().responseEntity(address).build();
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response update(@RequestBody AddressEntity address) {
		address = addressService.merge(address);
		return Response.builder().responseEntity(address).build();
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Response get(@RequestParam("id") long id) {
		logger.info("[" + id + "]");
		AddressEntity address = addressService.get(id, AddressEntity.class);
		Response response = new Response();
		response.setResponseEntity(address);
		return response;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Response search(@RequestBody SearchInput searchInput) throws ParseException {
		List<AddressEntity> list = addressService.search(searchInput, AddressEntity.class);
		long rowCount = addressService.getTotalRowCount(searchInput, AddressEntity.class);

		Response response = new Response();
		Map<String, Object> respMap = response.getResponseData();
		respMap.put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		respMap.put(Param.CURRENT_PAGE_NO.name(), String.valueOf(searchInput.getPageNo()));
		respMap.put(Param.TOTAL_PAGE_COUNT.name(),
				String.valueOf(CommonUtil.calculateNoOfPages(rowCount, searchInput.getRowsPerPage())));
		respMap.put(Param.ROWS_PER_PAGE.name(), String.valueOf(searchInput.getRowsPerPage()));

		response.setResponseEntity(list);

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
		List<Object> addressColumnData = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (AuthUtil.isAuth(auth)) {
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			Resource addressColumnJson = null;
			if (AuthUtil.isAdmin(authorities)) {
				addressColumnJson = this.resourceLoader.getResource("classpath:data/json/address/columnDataAdmin.json");
			} else {
				addressColumnJson = this.resourceLoader
						.getResource("classpath:data/json/address/columnDataMember.json");
			}
			if (addressColumnJson != null) {
				addressColumnData = objectMapper.readValue(addressColumnJson.getInputStream(), List.class);
			}
		}
		if (addressColumnData == null) {
			addressColumnData = new ArrayList<Object>();
		}
		return addressColumnData;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFormData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFormData() throws IOException {
		Map<String, Object> formDataMap = null;
		Resource formData = this.resourceLoader.getResource("classpath:data/json/address/formData.json");
		if (formData != null) {
			formDataMap = objectMapper.readValue(formData.getInputStream(), Map.class);
		}
		if (formDataMap == null) {
			formDataMap = new HashMap<String, Object>();
		}
		return formDataMap;
	}
}