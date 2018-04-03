package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Alert;
import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.dao.impl.ClientDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.pojo.Address;
import com.kanuhasu.ap.business.pojo.Client;
import com.kanuhasu.ap.business.pojo.ClientBasic;
import com.kanuhasu.ap.business.pojo.Contact;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class ClientServiceImpl {
	@Autowired
	private ClientDAOImpl dao;
	@Autowired
	private AddressDAOImpl addressDAO;
	@Autowired
	private ContactDAOImpl contactDAO;

	public Response empty() {
		return Response.Success(new ClientEntity().pojoFull());
	}

	public Response newClient(ClientBasic userBasic) {
		Response response = null;
		ClientEntity existingClient = dao.fetchByEmailID(userBasic.getEmailID());
		if (existingClient == null) {
			ClientEntity newClient = new ClientEntity();
			newClient.overrideBasic(userBasic);
			newClient = dao.save(newClient);
			response = Response.Success(newClient.pojoFull());
		} else {
			response = Response.Fail();
			response.addAlert(Alert.danger(Param.Error.EMAIL_ID_TAKEN.desc()));
		}
		return response;
	}

	public Response updateBasic(ClientBasic userBasic) {
		ClientEntity client = dao.fetchByID(userBasic.getId());
		client.overrideBasic(userBasic);
		client = dao.update(client);
		return Response.Success(client.pojoFull());
	}

	public Response get(long clientID) {
		ClientEntity client = dao.fetchByID(clientID);
		return Response.Success(client.pojoFull());
	}

	public Client getByEmailID(String emailID) {
		ClientEntity entity = dao.fetchByEmailID(emailID);
		if (entity != null) {
			return entity.pojo();
		} else {
			return null;
		}
	}

	public Response saveOrUpdateAddress(Address addressPojo) {
		ClientEntity client = dao.fetchByID(addressPojo.getParentId());
		AddressEntity address = AddressEntity.processParent(addressPojo, client.getAddresses());
		address = addressDAO.saveOrUpdate(address);
		client = dao.saveOrUpdate(client);
		Response response = new Response(client.pojoFull());
		response.getResponseData().put("address", address.pojo());
		return response;
	}

	public Response saveOrUpdateContact(Contact contactPojo) {
		ClientEntity client = dao.fetchByID(contactPojo.getParentId());
		ContactEntity contact = ContactEntity.processParent(contactPojo, client.getContacts());
		contact = contactDAO.saveOrUpdate(contact);
		client = dao.saveOrUpdate(client);
		Response response = new Response(client.pojoFull());
		response.getResponseData().put("contact", contact.pojo());
		return response;
	}

	public Response searchByName(String name) {
		List<ClientEntity> entities = dao.searchByName(name);
		List<Client> pojos = new ArrayList<>();
		for (ClientEntity entity : entities) {
			pojos.add(entity.pojo());
		}
		return new Response(pojos);
	}

	public Response search(SearchInput searchInput) throws ParseException {
		List<ClientEntity> entities = dao.search(searchInput, ClientEntity.class);
		List<Client> pojos = new ArrayList<>();
		for (ClientEntity entity : entities) {
			pojos.add(entity.pojo());
		}
		long rowCount = this.getTotalRowCount(searchInput);
		Response response = new Response(pojos);
		response.getResponseData().put(Param.ROW_COUNT.name(), String.valueOf(rowCount));
		return response;
	}

	private Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, ClientEntity.class);
	}
}