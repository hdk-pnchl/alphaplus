package com.kanuhasu.ap.business.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.JobInstEntity;

@Repository
@Transactional
public class JobInstDAOImpl extends AbstractDAO<JobInstEntity> {
	
	@Autowired
	private JobDAOImpl jobDAO; 
	
	public JobInstEntity save(long jobID, JobInstEntity jobInst) {
		Object jobObj = jobDAO.get(jobID, JobEntity.class);
		if(jobObj != null) {
			JobEntity job = (JobEntity) jobObj;
			jobInst.setJob(job);
			super.save(jobInst);
			job.getInstructions().add(jobInst);
			jobDAO.saveOrUpdate(job);
		}
		return jobInst;
	}
}