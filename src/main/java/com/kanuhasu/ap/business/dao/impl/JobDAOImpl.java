package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.BillEntity;
import com.kanuhasu.ap.business.bo.job.CTPEntity;
import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.bo.job.DeliveryEntity;
import com.kanuhasu.ap.business.bo.job.InstructionEntity;
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class JobDAOImpl extends AbstractDAO<JobEntity> {
	@Autowired
	private UserDAOImpl userDao;
	@Autowired
	private InstDAOImpl instDao;
	@Autowired
	private PlateDAOImpl plateDao;
	@Autowired
	private BillDAOImpl billDao;
	@Autowired
	private CTPDAOImpl ctpDao;
	@Autowired
	private StudioDAOImpl studioDao;
	@Autowired
	private DeliveryDAOImpl deliveryDao;
	@Autowired
	private ChallanDAOImpl challanDao;
	
	@Override
	public JobEntity saveOrUpdate(JobEntity job) {
		UserEntity loggedInUser= this.userDao.getByEmailID(CommonUtil.fetchLoginID());
		
		if(StringUtils.isBlank(job.getNo())){
			job.populateNo();
		}
		if(job.getChallan()!=null){
			job.getChallan().populateChallanNo();
		}
		job.processInternal();
		
		//job instructions
		if(job.getInstructions() != null) {
			for (Entry<String, InstructionEntity> instEntry : job.getInstructions().entrySet()) {
				InstructionEntity inst = instEntry.getValue();
				if(inst != null) {
					inst.setLastUpdatedOn(new Date());
					inst.setLastUpdatedBy(loggedInUser);
					instDao.saveOrUpdate(inst);
				}
			}
		}
		//plate
		if(job.getPlateDetail() != null) {
			for (Entry<String, PlateEntity> plateEntry : job.getPlateDetail().entrySet()) {
				PlateEntity plate = plateEntry.getValue();
				if(plate != null) {
					plate.setLastUpdatedOn(new Date());
					plate.setLastUpdatedBy(loggedInUser);
					plateDao.saveOrUpdate(plate);
				}
			}
		}
		//studio
		if(job.getStudio()!=null){
			StudioEntity studio= job.getStudio();
			studio.setLastUpdatedOn(new Date());
			studio.setLastUpdatedBy(loggedInUser);			
			if(studio.getExeBy()!=null){
				UserEntity exeBy;
				if(studio.getExeBy().getId()==loggedInUser.getId()){
					exeBy= loggedInUser;
				}else{
					exeBy= this.userDao.get(studio.getExeBy().getId(), UserEntity.class);	
				}
				studio.setExeBy(exeBy);				
			}
			//studio instructions
			if(studio.getInstructions() != null) {
				for (Entry<String, InstructionEntity> instEntry : studio.getInstructions().entrySet()) {
					InstructionEntity inst = instEntry.getValue();
					if(inst != null) {
						instDao.saveOrUpdate(inst);
					}
				}
			}
			this.studioDao.saveOrUpdate(studio);
		}
		//ctp
		if(job.getCtp()!=null){
			CTPEntity ctp= job.getCtp();
			ctp.setLastUpdatedOn(new Date());
			ctp.setLastUpdatedBy(loggedInUser);
			if(ctp.getExeBy()!=null){
				UserEntity exeBy;
				if(ctp.getExeBy().getId()==loggedInUser.getId()){
					exeBy= loggedInUser;
				}else{
					exeBy= this.userDao.get(ctp.getExeBy().getId(), UserEntity.class);	
				}
				ctp.setExeBy(exeBy);				
			}
			//ctp instructions
			if(ctp.getInstructions() != null) {
				for (Entry<String, InstructionEntity> instEntry : ctp.getInstructions().entrySet()) {
					InstructionEntity inst = instEntry.getValue();
					if(inst != null) {
						instDao.saveOrUpdate(inst);
					}
				}
			}

			this.ctpDao.saveOrUpdate(job.getCtp());
		}
		//challan
		if(job.getChallan()!=null){
			ChallanEntity challan= job.getChallan();
			challan.setLastUpdatedOn(new Date());
			challan.setLastUpdatedBy(loggedInUser);
			if(challan.getExeBy()!=null){
				UserEntity exeBy;
				if(challan.getExeBy().getId()==loggedInUser.getId()){
					exeBy= loggedInUser;
				}else{
					exeBy= this.userDao.get(challan.getExeBy().getId(), UserEntity.class);	
				}
				challan.setExeBy(exeBy);
			}
			//challan instructions
			if(challan.getInstructions() != null) {
				for (Entry<String, InstructionEntity> instEntry : challan.getInstructions().entrySet()) {
					InstructionEntity inst = instEntry.getValue();
					if(inst != null) {
						instDao.saveOrUpdate(inst);
					}
				}
			}

			this.challanDao.saveOrUpdate(job.getChallan());
		}
		//delivery
		if(job.getDelivery()!=null){
			DeliveryEntity delivery= job.getDelivery();
			delivery.setLastUpdatedOn(new Date());
			delivery.setLastUpdatedBy(loggedInUser);
			if(delivery.getExeBy()!=null){
				UserEntity exeBy;
				if(delivery.getExeBy().getId()==loggedInUser.getId()){
					exeBy= loggedInUser;
				}else{
					exeBy= this.userDao.get(delivery.getExeBy().getId(), UserEntity.class);	
				}
				delivery.setExeBy(exeBy);							
			}			
			//delivery instructions
			if(delivery.getInstructions() != null) {
				for (Entry<String, InstructionEntity> instEntry : delivery.getInstructions().entrySet()) {
					InstructionEntity inst = instEntry.getValue();
					if(inst != null) {
						instDao.saveOrUpdate(inst);
					}
				}
			}
			this.deliveryDao.saveOrUpdate(job.getDelivery());
		}
		//bill
		if(job.getBill()!=null){
			BillEntity bill= job.getBill();
			bill.setLastUpdatedOn(new Date());
			bill.setLastUpdatedBy(loggedInUser);
			if(bill.getExeBy()!=null){
				UserEntity exeBy;
				if(bill.getExeBy().getId()==loggedInUser.getId()){
					exeBy= loggedInUser;
				}else{
					exeBy= this.userDao.get(bill.getExeBy().getId(), UserEntity.class);	
				}
				bill.setExeBy(exeBy);				
			}
			//bill instructions
			if(bill.getInstructions() != null) {
				for (Entry<String, InstructionEntity> instEntry : bill.getInstructions().entrySet()) {
					InstructionEntity inst = instEntry.getValue();
					if(inst != null) {
						instDao.saveOrUpdate(inst);
					}
				}
			}
			this.billDao.saveOrUpdate(job.getBill());
		}
		
		//Docket
		job.setLastUpdatedOn(new Date());
		job.setLastUpdatedBy(loggedInUser);
		UserEntity docketBy;
		if(job.getDocketBy().getId()==loggedInUser.getId()){
			docketBy= loggedInUser;
		}else{
			docketBy= this.userDao.get(job.getDocketBy().getId(), UserEntity.class);	
		}
		job.setDocketBy(docketBy);
		
		super.saveOrUpdate(job);
		
		if(job.getPlateDetail() != null) {
			for (Entry<String, PlateEntity> plateEntry : job.getPlateDetail().entrySet()) {
				PlateEntity plate = plateEntry.getValue();
				if(plate != null) {
					plate.setJob(job);
				}
			}
		}
		if(job.getStudio()!=null){
			job.getStudio().setJob(job);
		}
		if(job.getCtp()!=null){
			job.getCtp().setJob(job);
		}
		if(job.getChallan()!=null){
			job.getChallan().setJob(job);
		}
		if(job.getDelivery()!=null){
			job.getDelivery().setJob(job);
		}	
		if(job.getBill()!=null){
			job.getBill().setJob(job);
		}	
		return job;
	}
}