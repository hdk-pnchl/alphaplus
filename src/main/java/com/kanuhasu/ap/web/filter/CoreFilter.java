package com.kanuhasu.ap.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CoreFilter implements Filter {
	private static final Logger logger = Logger.getLogger(CoreFilter.class);
	
	public void init(FilterConfig config) throws ServletException {
		// nothing goes here
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
		MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest((HttpServletRequest) request);
		/**request BODY**/
		String requestBody = IOUtils.toString(requestWrapper.getReader());
		if(StringUtils.isNotEmpty(requestBody)){
			logger.info(requestBody);	
		}
		// Pass request back down the filter chain
		chain.doFilter(requestWrapper, response);
	}
	
	public void destroy() {
		/* Called before the Filter instance is removed
		from service by the web container*/
	}
}
