package com.kanuhasu.ap.business.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.JobStatus;
import com.kanuhasu.ap.business.bo.job.Status;
import com.kanuhasu.ap.business.bo.user.AddressEntity;
import com.kanuhasu.ap.business.bo.user.ContactEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.BillDAOImpl;
import com.kanuhasu.ap.business.dao.impl.CTPDAOImpl;
import com.kanuhasu.ap.business.dao.impl.ChallanDAOImpl;
import com.kanuhasu.ap.business.dao.impl.ClientDAOImpl;
import com.kanuhasu.ap.business.dao.impl.DeliveryDAOImpl;
import com.kanuhasu.ap.business.dao.impl.InstDAOImpl;
import com.kanuhasu.ap.business.dao.impl.InstructionDetailDAOImpl;
import com.kanuhasu.ap.business.dao.impl.JobDAOImpl;
import com.kanuhasu.ap.business.dao.impl.PlateDAOImpl;
import com.kanuhasu.ap.business.dao.impl.PlateDetailDAOImpl;
import com.kanuhasu.ap.business.dao.impl.StudioDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.AddressDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.ContactDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.type.bo.user.BindingStyle;
import com.kanuhasu.ap.business.type.bo.user.ColorCopySize;
import com.kanuhasu.ap.business.type.bo.user.Gender;
import com.kanuhasu.ap.business.util.CommonUtil;

@Service
@Transactional
public class TestServiceImpl {
	@Autowired
	private UserDAOImpl userDao;
	@Autowired
	private ClientDAOImpl clientDao;
	@Autowired
	private InstDAOImpl instDao;
	@Autowired
	private PlateDAOImpl plateDao;
	@Autowired
	private JobDAOImpl dao;
	@Autowired
	private StudioDAOImpl studioDao;
	@Autowired
	private BillDAOImpl billDao;
	@Autowired
	private ChallanDAOImpl challanDao;
	@Autowired
	private CTPDAOImpl ctpDao;
	@Autowired
	private DeliveryDAOImpl deliveryDao;
	@Autowired
	private PlateDetailDAOImpl plateDetailDao;
	@Autowired
	private InstructionDetailDAOImpl instructionDetailDao;
	@Autowired
	private AddressDAOImpl addressDao;
	@Autowired
	private ContactDAOImpl contactDao;
	@Autowired
	private JobDAOImpl jobDao;

	public Response popuateTestData(int count) {
		this.populateUsers(count);
		this.populateClients(count);
		this.populateJobs(count);
		return Response.Success();
	}

	public Response populateUsers(int count) {
		for (int i = 1; i < count; i++) {
			Long no = CommonUtil.nextRegNo();
			String ip = no + "000";
			UserEntity user = new UserEntity();
			user.setName(ip);
			user.setEmailID(ip + "g.com");

			user.setDob(new Date());
			user.setGender(Gender.MALE);
			user.setMarried(true);
			user.setEducation(ip);
			user.setOccupation(ip);
			user.setRegNO(Long.valueOf(i));
			// id
			user.setPan(ip);
			user.setAdhar(ip);
			user.setDrivingLicence(ip);
			user.setPassport(ip);
			// core
			user.setLastUpdatedOn(new Date());
			user.setCreatedOn(new Date());
			user.setPassword(i + "");
			user.setLastUpdatedById(i);

			for (int j = 1; j < 5; j++) {
				AddressEntity newAddress = this.populateNewAddress();
				newAddress = addressDao.save(newAddress);
				user.getAddresses().add(newAddress);
				user.setAddressStr(newAddress.getAddressStr());
			}

			for (int j = 1; j < 5; j++) {
				ContactEntity newContact = this.populateNewContact();
				newContact = contactDao.save(newContact);
				user.getContacts().add(newContact);
				user.setContactStr(newContact.getContactStr());
			}
			userDao.save(user);
		}
		return Response.Success();
	}

	public Response populateClients(int count) {
		for (int i = 1; i < count; i++) {
			String ip = i + "000";
			ClientEntity client = new ClientEntity();
			client.setEmailID(ip + "g.com");
			client.setName(ip);
			// core
			client.setLastUpdatedOn(new Date());
			client.setCreatedOn(new Date());

			for (int j = 1; j < 5; j++) {
				AddressEntity newAddress = this.populateNewAddress();
				newAddress = addressDao.save(newAddress);
				client.getAddresses().add(newAddress);
			}

			for (int j = 1; j < 5; j++) {
				ContactEntity newContact = this.populateNewContact();
				newContact = contactDao.save(newContact);
				client.getContacts().add(newContact);
			}
			clientDao.save(client);
		}
		return Response.Success();
	}

	private AddressEntity populateNewAddress() {
		Long no = CommonUtil.nextRegNo();
		String ip = String.valueOf(no);
		AddressEntity address = new AddressEntity();
		address.setAddressLine1(ip);
		address.setAddressLine2(ip);
		address.setAddressLine3(ip);
		address.setAddressStr(ip);
		address.setCity(ip);
		address.setCountry(ip);
		address.setPincode(no.intValue());
		address.setState(ip);
		address.setTitle(ip);
		address.setAddressStr(ip);
		return address;
	}

	private ContactEntity populateNewContact() {
		Long no = CommonUtil.nextRegNo();
		String ip = String.valueOf(no);
		ContactEntity contact = new ContactEntity();
		contact.setTitle(ip);
		contact.setNo(ip);
		contact.setContactStr(ip);
		return contact;
	}

	public Response populateJobs(int count) {
		for (int i = 1; i < count; i++) {
			Long no = CommonUtil.nextRegNo();
			String ip = no + "000";
			JobEntity job = new JobEntity();
			job.setName(ip);
			job.setNo(ip);
			job.setReceivedDate(new Date());
			job.setTargetDate(new Date());
			job.setCut(1);
			job.setOpen(1);
			job.setPage(1);
			job.setBindingStyle(BindingStyle.CENTER);
			job.setColorCopySize(ColorCopySize.A4);
			job.setFrontBack(1);
			job.setSelfBack(1);
			job.setDoubleGripper(1);
			job.setOneSide(1);
			job.setFb_sb_dg_os(ip);
			job.setTotalSet(4);
			job.setTotalPlates(4);
			job.setDocketStatus(Status.InProgress);
			job.setStatus(JobStatus.CTP);
			job.setClientId(1);
			job.setLastUpdatedById(1);
			job.setDocketById(1);
			jobDao.save(job);
		}
		return Response.Success();
	}
}