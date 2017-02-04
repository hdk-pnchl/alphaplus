package com.kanuhasu.ap.business.service.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.AuthorityDAOImpl;
import com.kanuhasu.ap.business.type.bo.user.Roles;

@Service
@Transactional
public class AuthorityServiceImpl implements InitializingBean {
	
	@Autowired
	private AuthorityDAOImpl authorityDAO;
	
	private Map<Roles, RoleEntity> authorityMap = new HashMap<Roles, RoleEntity>();
	
	public RoleEntity save(RoleEntity authority) {
		authority = this.authorityDAO.save(authority);
		return authority;
	}
	
	public RoleEntity update(RoleEntity authority) {
		authority = this.authorityDAO.update(authority);
		return authority;
	}
	
	public UserEntity addRoleToUser(RoleEntity role, long userID) {
		UserEntity user = this.addRoleToUser(role, userID);
		return user;
	}
	
	public RoleEntity saveOrUpdate(RoleEntity role) {
		this.authorityDAO.saveOrUpdate(role);
		return role;
	}
	
	public RoleEntity get(long roleId) {
		RoleEntity role = this.get(roleId);
		return role;
	}
	
	public List<RoleEntity> list() {
		List<RoleEntity> roles = this.authorityDAO.list();
		return roles;
	}
	
	public void delete(RoleEntity family) {
		// TODO Auto-generated method stub
	}
	
	public void deletePermanently(RoleEntity family) {
		this.authorityDAO.delete(family);
	}
	
	public void afterPropertiesSet() throws Exception {
		List<RoleEntity> roles = this.list();
		if(roles.isEmpty()) {
			RoleEntity guest = this.save(new RoleEntity(Roles.GUEST.getName()));
			RoleEntity admin = this.save(new RoleEntity(Roles.ADMIN.getName()));
			RoleEntity member = this.save(new RoleEntity(Roles.MEMBER.getName()));
			this.getAuthorityMap().put(Roles.GUEST, guest);
			this.getAuthorityMap().put(Roles.ADMIN, admin);
			this.getAuthorityMap().put(Roles.MEMBER, member);
		}
		else {
			for (RoleEntity role : roles) {
				this.getAuthorityMap().put(Roles.fetchRoleByName(role.getRole()), role);
			}
		}
	}
	
	public Map<Roles, RoleEntity> getAuthorityMap() {
		return authorityMap;
	}
	
	public void setAuthorityMap(Map<Roles, RoleEntity> authorityMap) {
		this.authorityMap = authorityMap;
	}
}