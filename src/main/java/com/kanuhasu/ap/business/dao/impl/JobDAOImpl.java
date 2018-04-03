package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.BillEntity;
import com.kanuhasu.ap.business.bo.job.CTPEntity;
import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.job.DeliveryEntity;
import com.kanuhasu.ap.business.bo.job.InstructionDetailEntity;
import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.PlateDetailEntity;
import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.AuthUtil;

@Repository
@Transactional
public class JobDAOImpl extends AbstractDAO<JobEntity> {
	@Autowired
	private StudioDAOImpl studioDao;
	@Autowired
	private CTPDAOImpl ctpDao;
	@Autowired
	private BillDAOImpl billDao;
	@Autowired
	private ChallanDAOImpl challanDao;
	@Autowired
	private DeliveryDAOImpl deliveryDao;
	@Autowired
	private UserDAOImpl userDao;
	@Autowired
	private PlateDetailDAOImpl plateDetailDAO;
	@Autowired
	private InstructionDetailDAOImpl instructionDetailDAO;
	@Autowired
	private ClientDAOImpl clientDao;

	@Override
	public JobEntity save(JobEntity entity) {
		entity.populateNo();
		entity.getChallan().populateChallanNo();
		entity.setLastUpdatedOn(new Date());
		entity.setLastUpdatedById(AuthUtil.fetchLoggedInUserID());
		entity = super.save(entity);
		return entity;
	}

	public JobEntity update(JobEntity entity) {
		entity.processInternal();
		entity.setLastUpdatedOn(new Date());
		entity.setLastUpdatedById(AuthUtil.fetchLoggedInUserID());
		this.initLazyProp(entity);
		return entity;
	}

	public JobEntity fetchByID(long id) {
		JobEntity entity = super.fetchByID(id, JobEntity.class);
		this.initLazyProp(entity);
		return entity;
	}

	public void initLazyProp(JobEntity job) {
		// studio
		StudioEntity studio = studioDao.fetchByID(job.getStudioId(), StudioEntity.class);
		if (studio != null) {
			InstructionDetailEntity studioInstructionDetail = instructionDetailDAO
					.fetchByID(studio.getInstructionDetailId(), InstructionDetailEntity.class);
			studio.setInstructionDetail(studioInstructionDetail);
			UserEntity studioBy = userDao.fetchByID(studio.getExeById());
			studio.setExeBy(studioBy);
		}

		// ctp
		CTPEntity ctp = ctpDao.fetchByID(job.getCtpId(), CTPEntity.class);
		if (ctp != null) {
			InstructionDetailEntity ctpInstructionDetail = instructionDetailDAO.fetchByID(ctp.getInstructionDetailId(),
					InstructionDetailEntity.class);
			ctp.setInstructionDetail(ctpInstructionDetail);
			UserEntity ctpBy = userDao.fetchByID(ctp.getExeById());
			ctp.setExeBy(ctpBy);
		}

		// delivery
		DeliveryEntity delivery = deliveryDao.fetchByID(job.getDeliveryId(), DeliveryEntity.class);
		if (delivery != null) {
			InstructionDetailEntity deliveryInstructionDetail = instructionDetailDAO
					.fetchByID(delivery.getInstructionDetailId(), InstructionDetailEntity.class);
			delivery.setInstructionDetail(deliveryInstructionDetail);
			UserEntity deliveryBy = userDao.fetchByID(delivery.getExeById());
			delivery.setExeBy(deliveryBy);
		}

		// challan
		ChallanEntity challan = challanDao.fetchByID(job.getChallanId(), ChallanEntity.class);
		if (challan != null) {
			InstructionDetailEntity challanInstructionDetail = instructionDetailDAO
					.fetchByID(challan.getInstructionDetailId(), InstructionDetailEntity.class);
			challan.setInstructionDetail(challanInstructionDetail);
			UserEntity challanBy = userDao.fetchByID(challan.getExeById());
			challan.setExeBy(challanBy);
		}

		// bill
		BillEntity bill = billDao.fetchByID(job.getBillId(), BillEntity.class);
		if (bill != null) {
			InstructionDetailEntity billInstructionDetail = instructionDetailDAO
					.fetchByID(bill.getInstructionDetailId(), InstructionDetailEntity.class);
			bill.setInstructionDetail(billInstructionDetail);
			UserEntity billBy = userDao.fetchByID(bill.getExeById());
			bill.setExeBy(billBy);
		}

		// plateDetail
		PlateDetailEntity plateDetail = plateDetailDAO.fetchByID(job.getPlateDetailID(), PlateDetailEntity.class);
		InstructionDetailEntity instructionDetail = instructionDetailDAO.fetchByID(job.getInstructionDetailId(),
				InstructionDetailEntity.class);

		// client
		ClientEntity client = clientDao.fetchByID(job.getClientId());
		// closedBy
		UserEntity closedBy = userDao.fetchByID(job.getClosedById());
		// docketBy
		UserEntity docketBy = userDao.fetchByID(job.getDocketById());

		job.setBill(bill);
		job.setStudio(studio);
		job.setCtp(ctp);
		job.setChallan(challan);
		job.setDelivery(delivery);
		job.setPlateDetail(plateDetail);
		job.setInstructionDetail(instructionDetail);
		job.setClient(client);
		job.setDocketBy(docketBy);
		job.setClosedBy(closedBy);
	}
}