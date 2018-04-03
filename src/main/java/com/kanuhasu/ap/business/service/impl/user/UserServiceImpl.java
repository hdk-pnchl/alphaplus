package com.kanuhasu.ap.business.service.impl.user;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Alert;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.bo.user.RoleEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.pojo.Address;
import com.kanuhasu.ap.business.pojo.Contact;
import com.kanuhasu.ap.business.pojo.User;
import com.kanuhasu.ap.business.pojo.UserBasic;
import com.kanuhasu.ap.business.pojo.UserIDDetail;
import com.kanuhasu.ap.business.type.bo.user.Roles;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.AuthUtil;
import com.kanuhasu.ap.business.util.CommonUtil;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class UserServiceImpl {
	@Autowired
	private AuthorityServiceImpl authorityService;
	@Autowired
	private UserDAOImpl dao;
	@Autowired
	private AddressDAOImpl addressDAO;
	@Autowired
	private ContactDAOImpl contactDAO;

	public Response empty() {
		return Response.Success(new UserEntity().pojoFull());
	}

	public Response newUser(UserBasic pojo) {
		Response response = null;
		User existingUser = this.getByEmailID(pojo.getEmailID());
		if (existingUser == null) {
			RoleEntity memberRole = authorityService.getAuthorityMap().get(Roles.MEMBER);
			UserEntity user = new UserEntity();
			user.overrideBasic(pojo);
			user.setPassword(pojo.getPassword());
			user.getRoles().add(memberRole);
			user = dao.save(user);
			response = Response.Success(user.pojo());
		} else {
			response = Response.Fail();
			response.addAlert(Alert.danger(Param.Error.EMAIL_ID_TAKEN.desc()));
		}
		return response;
	}

	public Response updateBasic(UserBasic pojo) {
		UserEntity user = dao.fetchByID(pojo.getId());
		user.overrideBasic(pojo);
		user = dao.update(user);
		return Response.Success(user.pojoFull());
	}

	public Response updateIdDetail(UserIDDetail pojo) {
		UserEntity user = dao.fetchByID(pojo.getId());
		user.overrideID(pojo);
		user = dao.update(user);
		return Response.Success(user.pojoFull());
	}

	public Response get(long id) {
		UserEntity user = dao.fetchByID(id);
		return Response.Success(user.pojoFull());
	}

	public User getByEmailID(String emailID) {
		UserEntity entity = dao.fetchByEmailID(emailID);
		if (entity != null) {
			return entity.pojoFull();
		} else {
			return null;
		}
	}

	public Response saveOrUpdateAddress(Address addressPojo) {
		UserEntity user = dao.fetchByID(addressPojo.getParentId());
		AddressEntity address = AddressEntity.processParent(addressPojo, user.getAddresses());
		address = addressDAO.saveOrUpdate(address);
		user = dao.saveOrUpdate(user);
		Response response = new Response(user.pojoFull());
		response.getResponseData().put("address", address.pojo());
		return response;
	}

	public Response saveOrUpdateContact(Contact contactPojo) {
		UserEntity user = dao.fetchByID(contactPojo.getParentId());
		ContactEntity contact = ContactEntity.processParent(contactPojo, user.getContacts());
		contact = contactDAO.saveOrUpdate(contact);
		user = dao.saveOrUpdate(user);
		Response response = new Response(user.pojoFull());
		response.getResponseData().put("contact", contact.pojo());
		return response;
	}

	public Response searchByName(String name) {
		List<UserEntity> users = dao.searchByName(name);
		List<User> pojos = new ArrayList<>();
		for (UserEntity user : users) {
			pojos.add(user.pojo());
		}
		return new Response(pojos);
	}

	public Response search(SearchInput searchInput) throws ParseException {
		List<UserEntity> users = dao.search(searchInput, UserEntity.class);
		List<User> pojos = new ArrayList<>();
		for (UserEntity user : users) {
			pojos.add(user.pojo());
		}
		long rowCount = this.fetchTotalRowCount(searchInput);
		Response response = new Response(pojos);
		response.putParam(Param.ROW_COUNT.name(), rowCount);
		return response;
	}

	public Long fetchTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, UserEntity.class);
	}

	public Response updatePassword(String currentPassword, String newPassword) {
		UserEntity user = dao.fetchByEmailID(AuthUtil.fetchLoginID());
		if (StringUtils.isNotEmpty(currentPassword) && StringUtils.isNotEmpty(newPassword)
				&& currentPassword.equals(user.getPassword())) {
			user.setPassword(newPassword);
			dao.saveOrUpdate(user);
			return Response.Success();
		} else {
			return Response.Fail();
		}
	}

	public Response initiatePasswordUpdate(HttpServletRequest request, String emailID)
			throws ClassNotFoundException, IOException {
		Response response = null;
		UserEntity user = dao.fetchByEmailID(emailID);
		if (user != null) {
			String pwUpdateReqToken = UUID.randomUUID().toString();
			user.setChangePasswordReqToken(pwUpdateReqToken);
			user = dao.saveOrUpdate(user);
			Map<String, String> pwUpdateReqMap = new HashMap<String, String>();
			pwUpdateReqMap.put(Param.EMAIL_ID.name(), emailID);
			pwUpdateReqMap.put(Param.PW_UPDATE_REQ_TOKEN.name(), pwUpdateReqToken);

			// http://localhost:8080/alphaplus-static/#/signIn
			StringBuilder pwUpdateReq = new StringBuilder(
					CommonUtil.buildUrl(request, "static/#/user/updateForgottenPassword/"));
			pwUpdateReq.append(CommonUtil.mapToString(pwUpdateReqMap));

			response = Response.Success();
			response.putParam(Param.STATUS.name(), Boolean.TRUE);
			response.putParam(Param.PW_UPDATE_URL.name(), pwUpdateReq);
		} else {
			response = Response.Fail();
			response.putParam(Param.STATUS.name(), Boolean.FALSE.toString());
			response.putParam(Param.ERR_USER_DOESNT_EXISTS.name(), Boolean.TRUE.toString());
		}
		return response;
	}

	public Response updateForgottenPassword(String token, String password) throws ClassNotFoundException, IOException {
		Response response = null;
		Map<String, String> reqMap = CommonUtil.stringToMap(token);
		String emailID = reqMap.get(Param.EMAIL_ID.name());
		if (emailID != null) {
			UserEntity user = dao.fetchByEmailID(emailID);
			if (user != null) {
				String reqToken = reqMap.get(Param.PW_UPDATE_REQ_TOKEN.name());
				if (!StringUtils.isEmpty(user.getChangePasswordReqToken()) && !StringUtils.isEmpty(reqToken)
						&& user.getChangePasswordReqToken().equals(reqToken)) {
					user.setPassword(password);
					dao.saveOrUpdate(user);
					response = Response.Success();
				} else {
					response = Response.Fail();
					response.putParam(Param.ERR_UPDATE_PW_TOKEN_DOESNT_EXISTS.name(), Boolean.TRUE);
				}
			} else {
				response = Response.Fail();
				response.putParam(Param.ERR_USER_DOESNT_EXISTS.name(), Boolean.TRUE);
			}
		}
		return response;
	}

	public Response makeItAdmin(String emailID) {
		RoleEntity adminRole = authorityService.getAuthorityMap().get(Roles.ADMIN);
		return dao.makeItAdmin(emailID, adminRole);
	}
}