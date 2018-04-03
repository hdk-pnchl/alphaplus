package com.kanuhasu.ap.business.util;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.type.bo.user.Roles;

public class AuthUtil {
	private static UserEntity loggedInUser;

	public static Long fetchLoggedInUserID() {
		if (AuthUtil.fetchLoggedInUser() != null) {
			return AuthUtil.fetchLoggedInUser().getId();
		} else {
			return null;
		}
	}

	public static UserEntity fetchLoggedInUser() {
		return loggedInUser;
	}

	public static void storeLoggedInUser(UserEntity loggedInUser) {
		AuthUtil.loggedInUser = loggedInUser;
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

	public static String fetchAuthName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
