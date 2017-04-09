package com.kanuhasu.ap.business.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobEntity;

@Repository
@Transactional
public class JobDAOImpl extends AbstractDAO<JobEntity> {
	
}