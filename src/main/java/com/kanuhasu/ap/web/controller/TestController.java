package com.kanuhasu.ap.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.util.SearchInputBasic;
import com.kanuhasu.ap.test.RException;

@CrossOrigin
@Controller
@RequestMapping("/test")
public class TestController implements ResourceLoaderAware {
	// instance

	private ResourceLoader resourceLoader;
	@Autowired
	private ObjectMapper objectMapper;

	// setter-getter

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	// web
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void test(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().write("Yes, It works");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 'charge','refund','reversal','chargeback','credit'
	// 'success','failed','unknown','reversed'
	/**
	 * http://localhost:8080/alphaplus/ctrl/test/exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exceptions", method = RequestMethod.POST)
	public @ResponseBody Response exceptions(@RequestBody SearchInputBasic searchInput) throws IOException {
		Resource resource = this.resourceLoader.getResource("classpath:data/json/test/rExceptions.json");
		Map<String, Object> resourceData = objectMapper.readValue(resource.getInputStream(), Map.class);

		// exceptions
		List<RException> exceptions = (List<RException>) resourceData.get("data");
		// ((page*rows)-page)+1

		int from = ((searchInput.getPageNo() * searchInput.getRowsPerPage()) - searchInput.getRowsPerPage());
		// (page*rows)
		int to = searchInput.getPageNo() * searchInput.getRowsPerPage();
		if (to > exceptions.size()) {
			to = exceptions.size();
		}

		Response internalData = new Response();
		internalData.setResponseEntity(exceptions.subList(from, to));
		internalData.getResponseData().put("total", String.valueOf(exceptions.size()));

		return internalData;
	}

}
