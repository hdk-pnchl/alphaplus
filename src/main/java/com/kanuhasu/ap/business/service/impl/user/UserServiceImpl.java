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

import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import com.kanuhasu.ap.business.type.bo.user.Roles;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<UserEntity> implements UserDetailsService{
	@Autowired
	private AuthorityServiceImpl authorityService;
	
	@Autowired
	public void setDao(UserDAOImpl dao) {
		this.dao = dao;
	}
	
	@Override
	public UserEntity save(UserEntity user) {
		RoleEntity role = authorityService.getAuthorityMap().get(Roles.MEMBER);
		user.getRoles().add(role);
		super.save(user);
		return user;
	}
	
	public ClientEntity searchByName(String name) {
		return ((UserDAOImpl)dao).searchByName(name);
	}
	
	public boolean makeItAdmin(String emailID) {
		RoleEntity adminRole = authorityService.getAuthorityMap().get(Roles.ADMIN);
		return ((UserDAOImpl)dao).makeItAdmin(emailID, adminRole);
	}
	
	public UserEntity getByEmailID(String emailID) {
		return ((UserDAOImpl)dao).getByEmailID(emailID);
	}
	
	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		User user = null;
		UserEntity userDetails = this.getByEmailID(emailId);
		if(userDetails != null) {
			List<GrantedAuthority> roles = this.buildUserAuthority(userDetails.getRoles());
			user = this.buildUserForAuthentication(userDetails, roles);
		}
		return user;
	}
	
	private User buildUserForAuthentication(UserEntity user, List<GrantedAuthority> authorities) {
		return new User(user.getEmailID(), user.getPassword(), user.isAccountEnabled(), user.isAccountExpired(), user.isAccountCredentialsExpired(),
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
	
	public List<UserEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, UserEntity.class);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, UserEntity.class);
	}
}