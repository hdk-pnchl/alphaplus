package com.kanuhasu.ap.business.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kanuhasu.ap.business.type.bo.user.Roles;

public class CommonUtil {
	protected static AtomicReference<Long> currentTime = new AtomicReference<>(System.currentTimeMillis());

	/**
	 * will fetch unique long value based on current time. to avoid duplicates,
	 * AtomicReference is used which provides volatile behaviour for counter.
	 */
	public static Long nextRegNo() {
		Long prev;
		Long next = System.currentTimeMillis();
		do {
			prev = currentTime.get();
			/*
			 * we need this, as its likely program executes so fast that, prev
			 * and next both might have the same value.
			 */
			next = next > prev ? next : prev + 1;
		}
		/*
		 * each time we fetch 1 new ID, we update its value in currentTime. And
		 * we take reference of it to make sure, we are threadsafe.
		 * 
		 * compareAndSet will provide a check against stale values. i.e. while
		 * updating currentTime's values, if existing copied value of
		 * currentTime isnt same as the current value of currentTime, some other
		 * thread already have taken the nextID.
		 */
		while (!currentTime.compareAndSet(prev, next));
		return next;
	}

	public void populateRoles() {

	}

	public static String fetchLoginID() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public static boolean isAdmin(Collection<SimpleGrantedAuthority> authorities) {
		boolean isAdmin = false;
		for (SimpleGrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals(Roles.ADMIN.getName())) {
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	public static boolean isAdmin() {
		boolean isAdmin = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			@SuppressWarnings("unchecked")
			Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			for (SimpleGrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals(Roles.ADMIN.getName())) {
					isAdmin = true;
				}
			}
		}
		return isAdmin;
	}

	public static boolean isAuth(Authentication auth) {
		boolean isAuth = false;
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			isAuth = true;
		}
		return isAuth;
	}

	public static boolean isAuth() {
		boolean isAuth = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			isAuth = true;
		}
		return isAuth;
	}

	public static long calculateNoOfPages(long rowCount, long rowPerPage) {
		long pageCount = 0;
		if (rowCount != 0 && rowPerPage != 0) {
			pageCount = rowCount / rowPerPage;
			if (rowCount % rowPerPage != 0) {
				pageCount++;
			}
		}
		return pageCount;
	}
	
	public static byte[] mapToByteAry(Map<String, String> ipMap) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(ipMap);
        oos.close();
		return bos.toByteArray();
	}
	
	public static Map<String, String> ByteAryToMap(byte[] ipByteAry) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bia= new ByteArrayInputStream(ipByteAry);
        ObjectInputStream ois = new ObjectInputStream(bia);
		@SuppressWarnings("unchecked")
		Map<String, String> opMap = (Map<String, String>) ois.readObject();
        ois.close();
		return opMap;	
	}
	
	public static String mapToString(Map<String, String> ipMap) throws ClassNotFoundException, IOException{
		if(ipMap != null){
			byte[] byteAry= CommonUtil.mapToByteAry(ipMap);
			String Base64URL = Base64.encodeBase64URLSafeString(byteAry);
			return Base64URL;			
		}
		return null;
	}
	
	public static Map<String, String> stringToMap(String mapStr) throws ClassNotFoundException, IOException{
		if(StringUtils.isNotEmpty(mapStr)){
			byte[] byteAry = Base64.decodeBase64(mapStr);
			Map<String, String> opMap = CommonUtil.ByteAryToMap(byteAry);
			return opMap;
		}
		return null;
	}
	
	public static String fetchBaseUrl(HttpServletRequest request){
		StringBuilder builder= new StringBuilder();
		//http://
		builder.append(request.getScheme()).append("://");
		//localhost:8080/
		builder.append(request.getServerName()).append(":").append(request.getServerPort());
		//alphaplus
		builder.append(request.getContextPath());
		return builder.toString();
	}
	
	public static String buildUrl(HttpServletRequest request, String resourcePath){
		StringBuilder builder= new StringBuilder(CommonUtil.fetchBaseUrl(request));
		builder.append("/").append(resourcePath);
		return builder.toString();
	}
}
