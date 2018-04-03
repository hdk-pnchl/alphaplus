package com.kanuhasu.ap.business.service.impl.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.AuthUtil;

public class UserSecurityServiceImpl implements UserDetailsService {

	@Autowired
	private UserDAOImpl userDao;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		User springUser = null;
		UserEntity user = userDao.fetchByEmailID(emailId);
		if (user != null) {
			List<GrantedAuthority> roles = this.buildUserAuthority(user.getRoles());
			springUser = this.buildUserForAuthentication(user, roles);
			AuthUtil.storeLoggedInUser(user);
			return springUser;
		} else {
			throw new UsernameNotFoundException("No User associated with EmailID: " + emailId);
		}
	}

	private User buildUserForAuthentication(UserEntity user, List<GrantedAuthority> authorities) {
		return new User(user.getEmailID(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
				user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<RoleEntity> roles) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		// Build user's authorities
		for (RoleEntity userRole : roles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
		}
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
		return Result;
	}
}
