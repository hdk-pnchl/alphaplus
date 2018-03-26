package com.kanuhasu.ap.business.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.util.CommonUtil;

@Repository
@Transactional
public class JobDAOImpl extends AbstractDAO<JobEntity> {
	@Autowired
	private UserDAOImpl userDao;

	@Override
	public JobEntity saveOrUpdate(JobEntity job) {
		// loggedInUser
		UserEntity loggedInUser = this.userDao.getByEmailID(CommonUtil.fetchLoginID());
		// job
		if (job.getId() == null) {
			job.populateNo();
			job.getChallan().populateChallanNo();
		}
		//job-internal
		job.processInternal();
		job.setLastUpdatedOn(new Date());
		job.setLastUpdatedBy(loggedInUser);

		super.saveOrUpdate(job);

		return job;
	}
}