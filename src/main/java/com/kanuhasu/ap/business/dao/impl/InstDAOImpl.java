package com.kanuhasu.ap.business.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.InstructionEntity;

@Repository
@Transactional
public class InstDAOImpl extends AbstractDAO<InstructionEntity> {
	
	@Autowired
	private JobDAOImpl jobDAO; 
	
	public InstructionEntity save(long jobID, InstructionEntity inst) {
		Object jobO = jobDAO.get(jobID, JobEntity.class);
		if(jobO != null) {
			JobEntity job = (JobEntity) jobO;
			super.save(inst);
			jobDAO.saveOrUpdate(job);
		}
		return inst;
	}
}