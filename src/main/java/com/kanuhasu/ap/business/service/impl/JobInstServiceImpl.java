package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.job.JobInstructionEntity;
import com.kanuhasu.ap.business.dao.impl.JobInstDAOImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class JobInstServiceImpl {
	
	@Autowired
	private JobInstDAOImpl plateDAO;
	
	public JobInstructionEntity save(JobInstructionEntity jobInst, long jobID) {
		return plateDAO.save(jobID, jobInst);
	}
	
	public JobInstructionEntity update(JobInstructionEntity jobInst) {
		return plateDAO.update(jobInst);
	}
	
	public JobInstructionEntity saveOrUpdate(JobInstructionEntity jobInst) {
		return plateDAO.saveOrUpdate(jobInst);
	}
	
	public JobInstructionEntity get(long jobInstId) {
		return plateDAO.get(jobInstId, JobInstructionEntity.class);
	}
	
	public List<JobInstructionEntity> list() {
		return plateDAO.list(JobInstructionEntity.class);
	}
	
	public List<JobInstructionEntity> search(SearchInput searchInput) throws ParseException {
		return plateDAO.search(searchInput);
	}
	
	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return plateDAO.getTotalRowCount(searchInput);
	}
	
	public void delete(JobInstructionEntity plate) {
		plateDAO.delete(plate);
	}
	
	public void deletePermanently(JobInstructionEntity plate) {
		plateDAO.deletePermanently(plate);
	}
}