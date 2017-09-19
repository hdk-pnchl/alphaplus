package com.kanuhasu.ap.business.dao.impl;

import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.JobInstEntity;
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;

@Repository
@Transactional
public class JobDAOImpl extends AbstractDAO<JobEntity> {
	@Autowired
	private JobInstDAOImpl jobInstDao;
	@Autowired
	private PlateDAOImpl plateDao;
	@Autowired
	private UserDAOImpl userDao;
	
	@Override
	public JobEntity saveOrUpdate(JobEntity job) {
		if(StringUtils.isBlank(job.getNo())){
			job.populateNo();
		}
		if(StringUtils.isBlank(job.getChallanNo())){
			job.populateChallanNo();
		}
	
		if(job.getInstructions() != null) {
			for (Entry<String, JobInstEntity> jobInstEntry : job.getInstructions().entrySet()) {
				JobInstEntity jobInst = jobInstEntry.getValue();
				if(jobInst != null) {
					jobInstDao.saveOrUpdate(jobInst);
				}
			}
		}
		if(job.getPlateDetail() != null) {
			for (Entry<String, PlateEntity> plateEntry : job.getPlateDetail().entrySet()) {
				PlateEntity plate = plateEntry.getValue();
				if(plate != null) {
					plateDao.saveOrUpdate(plate);
				}
			}
		}
		
		UserEntity user = null;
		if(job.getChallanBy() != null) {
			user = userDao.get(job.getChallanBy().getId());
			job.setChallanBy(user);
		}
		if(job.getDocketBy() != null) {
			user = userDao.get(job.getDocketBy().getId());
			job.setDocketBy(user);
		}
		if(job.getLastUpdatedBy() != null) {
			user = userDao.get(job.getLastUpdatedBy().getId());
			job.setLastUpdatedBy(user);
		}
		if(job.getPlateBy() != null) {
			user = userDao.get(job.getPlateBy().getId());
			job.setPlateBy(user);
		}
		if(job.getProcessBy() != null) {
			user = userDao.get(job.getProcessBy().getId());
			job.setProcessBy(user);
		}
		if(job.getRipBy() != null) {
			user = userDao.get(job.getRipBy().getId());
			job.setRipBy(user);
		}
		super.saveOrUpdate(job);
		return job;
	}
}