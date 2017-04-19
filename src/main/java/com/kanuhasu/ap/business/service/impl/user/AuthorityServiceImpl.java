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
import com.kanuhasu.ap.business.service.impl.AbstractServiceImpl;
import com.kanuhasu.ap.business.type.bo.user.Roles;

@Service
@Transactional
public class AuthorityServiceImpl extends AbstractServiceImpl<RoleEntity> implements InitializingBean {
	
	@Autowired
	public void setDao(AuthorityDAOImpl dao) {
		this.dao = dao;
	}
	
	private Map<Roles, RoleEntity> authorityMap = new HashMap<Roles, RoleEntity>();
	
	public UserEntity addRoleToUser(RoleEntity role, long userID) {
		UserEntity user = this.addRoleToUser(role, userID);
		return user;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<RoleEntity> roles = super.list(RoleEntity.class);
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