package com.kanuhasu.ap.business.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.JobInstructionEntity;
import com.kanuhasu.ap.business.util.SearchInput;

@Repository
@Transactional
public class JobInstDAOImpl extends AbstractDAO {
	
	public JobInstructionEntity save(long jobID, JobInstructionEntity jobInst) {
		Object jobObj = this.getSession().get(JobEntity.class, jobID);
		if(jobObj != null) {
			JobEntity job = (JobEntity) jobObj;
			
			jobInst.setJob(job);
			this.getSession().save(jobInst);
			job.getInstructions().add(jobInst);
			this.getSession().update(job);
		}
		return jobInst;
	}
	
	@SuppressWarnings("unchecked")
	public List<JobInstructionEntity> search(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(JobInstructionEntity.class);
		super.search(searchInput, criteria);
		return criteria.list();
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		Criteria criteria = this.getSession().createCriteria(JobInstructionEntity.class);
		super.getTotalRowCount(searchInput, criteria);
		Long rowCount = (Long) criteria.uniqueResult();
		return rowCount;
	}
}