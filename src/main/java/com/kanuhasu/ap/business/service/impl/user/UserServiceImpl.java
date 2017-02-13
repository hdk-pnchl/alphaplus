package com.kanuhasu.ap.business.service.impl.user;

import java.text.ParseException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.BasicDetailEntity;
import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.type.bo.user.Roles;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService {
	@Autowired
	private UserDAOImpl userDAO;
	@Autowired
	private AuthorityServiceImpl authorityService;
	
	public UserEntity save(UserEntity user) {
		RoleEntity role = authorityService.getAuthorityMap().get(Roles.MEMBER);
		user.getRoles().add(role);
		this.userDAO.save(user);
		return user;
	}
	
	public boolean makeItAdmin(String emailID) {
		RoleEntity adminRole = authorityService.getAuthorityMap().get(Roles.ADMIN);
		return this.userDAO.makeItAdmin(emailID, adminRole);
	}
	
	public UserEntity update(UserEntity user) {
		this.userDAO.update(user);
		return user;
	}
	
	public UserEntity get(long userID) {
		return this.userDAO.get(userID);
	}
	
	public UserEntity get(String emailID) {
		return this.userDAO.get(emailID);
	}
	
	public List<UserEntity> list() {
		return this.userDAO.list();
	}
	
	public List<UserEntity> search(SearchInput searchInput) throws ParseException {
		return this.userDAO.search(searchInput);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return this.userDAO.getTotalRowCount(searchInput);
	}

		
	public void deletePermanently(UserEntity user) {
		this.userDAO.delete(user);
	}
	
	public void delete(UserEntity user) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		User user = null;
		UserEntity userDetails = userDAO.get(emailId);
		if(userDetails != null) {
			List<GrantedAuthority> roles = this.buildUserAuthority(userDetails.getRoles());
			user = this.buildUserForAuthentication(userDetails, roles);
		}
		return user;
	}
	
	private User buildUserForAuthentication(UserEntity user, List<GrantedAuthority> authorities) {
		BasicDetailEntity basicDetail= user.getBasicDetail();
		return new User(basicDetail.getEmailID(), basicDetail.getPassword(), user.isAccountEnabled(), user.isAccountExpired(), user.isAccountCredentialsExpired(),
				user.isAccountLocked(), authorities);
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