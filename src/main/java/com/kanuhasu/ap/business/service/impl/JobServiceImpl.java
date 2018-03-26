package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.BillEntity;
import com.kanuhasu.ap.business.bo.job.CTPEntity;
import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.job.DeliveryEntity;
import com.kanuhasu.ap.business.bo.job.InstructionEntity;
import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.JobDAOImpl;
import com.kanuhasu.ap.business.pojo.Bill;
import com.kanuhasu.ap.business.pojo.Challan;
import com.kanuhasu.ap.business.pojo.Ctp;
import com.kanuhasu.ap.business.pojo.Delivery;
import com.kanuhasu.ap.business.pojo.JobDocket;
import com.kanuhasu.ap.business.pojo.Studio;
import com.kanuhasu.ap.business.service.impl.user.UserServiceImpl;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class JobServiceImpl extends AbstractServiceImpl<JobEntity> {
	@Autowired
	private ClientServiceImpl clientService;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private PlateServiceImpl plateService;
	@Autowired
	private InstServiceImpl instService;
	@Autowired
	private CTPServiceImpl ctpService;
	@Autowired
	private StudioServiceImpl studioService;
	@Autowired
	private BillServiceImpl billService;
	@Autowired
	private ChallanServiceImpl challanService;
	@Autowired
	private DeliveryServiceImpl deliveryService;

	@Autowired
	public void setDao(JobDAOImpl dao) {
		this.dao = dao;
	}

	public List<JobEntity> search(SearchInput searchInput) throws ParseException {
		return dao.search(searchInput, JobEntity.class);
	}

	public Long getTotalRowCount(SearchInput searchInput) throws ParseException {
		return dao.getTotalRowCount(searchInput, JobEntity.class);
	}

	public JobEntity docketSaveOrUpdate(JobDocket jobBasic) {
		JobEntity job;
		if (jobBasic.getJobID() != null) {
			job = dao.get(jobBasic.getJobID(), JobEntity.class);
		} else {
			job = new JobEntity();
		}
		job.setName(jobBasic.getName());
		job.setReceivedDate(jobBasic.getReceivedDate());
		job.setTargetDate(jobBasic.getTargetDate());
		job.setClient(clientService.get(jobBasic.getClientID(), ClientEntity.class));
		job.setCut(jobBasic.getCut());
		job.setOpen(jobBasic.getOpen());
		job.setPage(jobBasic.getPage());
		job.setBindingStyle(jobBasic.getBindingStyle());
		job.setColorCopySize(jobBasic.getColorCopySize());
		job.setDocketStatus(jobBasic.getDocketStatus());
		job.setDocketBy(userService.get(jobBasic.getDocketByID(), UserEntity.class));
		dao.saveOrUpdate(job);

		return job;
	}

	public Response plateSaveOrUpdate(PlateEntity plate, long jobID) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		if (plate.getId() != null) {
			plate = job.overridePlate(plate);
		} else {
			plateService.saveOrUpdate(plate);
			plate.setJob(job);
			job.getPlates().add(plate);
		}
		
		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("plate", plate);
		return response;
	}

	public Response studioSaveOrUpdate(Studio ipStudio, long jobID) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		StudioEntity studio = job.getStudio().override(ipStudio);
		studio.setExeBy(userService.get(ipStudio.getExeByID(), UserEntity.class));
		studioService.merge(studio);

		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("studio", studio);
		return response;
	}

	public Response ctpSaveOrUpdate(Ctp ipCtp, long jobID) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		CTPEntity ctp = job.getCtp().override(ipCtp);
		ctp.setExeBy(userService.get(ipCtp.getExeByID(), UserEntity.class));
		ctpService.merge(ctp);

		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("ctp", ipCtp);
		return response;
	}

	public Response deliverySaveOrUpdate(Delivery ipDelivery, long jobID) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		DeliveryEntity delivery = job.getDelivery().override(ipDelivery);
		delivery.setExeBy(userService.get(ipDelivery.getExeByID(), UserEntity.class));
		deliveryService.merge(delivery);

		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("delivery", delivery);
		return response;
	}

	public Response billSaveOrUpdate(Bill ipBill, long jobID) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		BillEntity bill = job.getBill().override(ipBill);
		bill.setExeBy(userService.get(ipBill.getExeByID(), UserEntity.class));
		billService.merge(bill);

		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("bill", bill);
		return response;
	}

	public Response challanSaveOrUpdate(Challan ipChallan, long jobID) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		ChallanEntity challan = job.getChallan().override(ipChallan);
		challan.setExeBy(userService.get(ipChallan.getExeByID(), UserEntity.class));
		challanService.merge(challan);

		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("challan", challan);
		return response;
	}

	public Response instSaveOrUpdate(InstructionEntity instruction, Long jobID, String part) {
		JobEntity job = dao.get(jobID, JobEntity.class);
		instruction = job.processInst(instruction, part);
		instruction= instService.saveOrUpdate(instruction);
		
		dao.saveOrUpdate(job);
		Response response = new Response();
		response.setResponseEntity(job);
		response.getResponseData().put("instruction", instruction);
		return response;
	}
}