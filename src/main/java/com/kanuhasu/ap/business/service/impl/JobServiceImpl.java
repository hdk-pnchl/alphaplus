package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.BillEntity;
import com.kanuhasu.ap.business.bo.job.CTPEntity;
import com.kanuhasu.ap.business.bo.job.ChallanEntity;
import com.kanuhasu.ap.business.bo.job.ClientEntity;
import com.kanuhasu.ap.business.bo.job.DeliveryEntity;
import com.kanuhasu.ap.business.bo.job.InstructionDetailEntity;
import com.kanuhasu.ap.business.bo.job.InstructionEntity;
import com.kanuhasu.ap.business.bo.job.JobEntity;
import com.kanuhasu.ap.business.bo.job.PlateDetailEntity;
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.bo.job.StudioEntity;
import com.kanuhasu.ap.business.bo.user.UserEntity;
import com.kanuhasu.ap.business.dao.impl.BillDAOImpl;
import com.kanuhasu.ap.business.dao.impl.CTPDAOImpl;
import com.kanuhasu.ap.business.dao.impl.ChallanDAOImpl;
import com.kanuhasu.ap.business.dao.impl.ClientDAOImpl;
import com.kanuhasu.ap.business.dao.impl.DeliveryDAOImpl;
import com.kanuhasu.ap.business.dao.impl.InstDAOImpl;
import com.kanuhasu.ap.business.dao.impl.InstructionDetailDAOImpl;
import com.kanuhasu.ap.business.dao.impl.JobDAOImpl;
import com.kanuhasu.ap.business.dao.impl.PlateDAOImpl;
import com.kanuhasu.ap.business.dao.impl.PlateDetailDAOImpl;
import com.kanuhasu.ap.business.dao.impl.StudioDAOImpl;
import com.kanuhasu.ap.business.dao.impl.user.UserDAOImpl;
import com.kanuhasu.ap.business.pojo.Bill;
import com.kanuhasu.ap.business.pojo.Challan;
import com.kanuhasu.ap.business.pojo.Ctp;
import com.kanuhasu.ap.business.pojo.Delivery;
import com.kanuhasu.ap.business.pojo.Docket;
import com.kanuhasu.ap.business.pojo.Instruction;
import com.kanuhasu.ap.business.pojo.Job;
import com.kanuhasu.ap.business.pojo.Plate;
import com.kanuhasu.ap.business.pojo.Studio;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class JobServiceImpl {
	@Autowired
	private UserDAOImpl userDao;
	@Autowired
	private ClientDAOImpl clientDao;
	@Autowired
	private InstDAOImpl instDao;
	@Autowired
	private PlateDAOImpl plateDao;
	@Autowired
	private JobDAOImpl dao;
	@Autowired
	private StudioDAOImpl studioDao;
	@Autowired
	private BillDAOImpl billDao;
	@Autowired
	private ChallanDAOImpl challanDao;
	@Autowired
	private CTPDAOImpl ctpDao;
	@Autowired
	private DeliveryDAOImpl deliveryDao;
	@Autowired
	private PlateDetailDAOImpl plateDetailDao;
	@Autowired
	private InstructionDetailDAOImpl instructionDetailDao;

	public Response empty() {
		return Response.Success(new JobEntity().pojoFull());
	}

	public Response newJob(Docket docket) {
		JobEntity entity = new JobEntity();
		// docket
		this.buildDocket(entity, docket);
		// plateDetail
		PlateDetailEntity plateDetail = new PlateDetailEntity();
		plateDetail = plateDetailDao.save(plateDetail);
		entity.setPlateDetailID(plateDetail.getId());
		// instructionDetail
		InstructionDetailEntity instructionDetail = new InstructionDetailEntity();
		instructionDetail = instructionDetailDao.save(instructionDetail);
		entity.setInstructionDetailId(instructionDetail.getId());
		// studio
		StudioEntity studio = new StudioEntity();
		InstructionDetailEntity studioInstructionDetail = new InstructionDetailEntity();
		studioInstructionDetail = instructionDetailDao.save(studioInstructionDetail);
		studio.setInstructionDetail(studioInstructionDetail);
		studio.setInstructionDetailId(studioInstructionDetail.getId());
		studio = studioDao.save(studio);
		entity.setStudioId(studio.getId());
		// ctp
		CTPEntity ctp = new CTPEntity();
		InstructionDetailEntity ctpInstructionDetail = new InstructionDetailEntity();
		ctpInstructionDetail = instructionDetailDao.save(ctpInstructionDetail);
		ctp.setInstructionDetail(ctpInstructionDetail);
		ctp.setInstructionDetailId(ctpInstructionDetail.getId());
		ctp = ctpDao.save(ctp);
		entity.setCtpId(ctp.getId());
		// delivery
		DeliveryEntity delivery = new DeliveryEntity();
		InstructionDetailEntity deliveryInstructionDetail = new InstructionDetailEntity();
		deliveryInstructionDetail = instructionDetailDao.save(deliveryInstructionDetail);
		delivery.setInstructionDetail(deliveryInstructionDetail);
		delivery.setInstructionDetailId(deliveryInstructionDetail.getId());
		delivery = deliveryDao.save(delivery);
		entity.setDeliveryId(delivery.getId());
		// bill
		BillEntity bill = new BillEntity();
		InstructionDetailEntity billInstructionDetail = new InstructionDetailEntity();
		billInstructionDetail = instructionDetailDao.save(billInstructionDetail);
		bill.setInstructionDetail(billInstructionDetail);
		bill.setInstructionDetailId(billInstructionDetail.getId());
		bill = billDao.save(bill);
		entity.setBillId(bill.getId());
		// challan
		ChallanEntity challan = new ChallanEntity();
		InstructionDetailEntity challanInstructionDetail = new InstructionDetailEntity();
		challanInstructionDetail = instructionDetailDao.save(challanInstructionDetail);
		challan.setInstructionDetail(challanInstructionDetail);
		challan.setInstructionDetailId(challanInstructionDetail.getId());
		challan = challanDao.save(challan);
		entity.setChallanId(challan.getId());

		entity = dao.save(entity);
		return Response.Success(entity.pojoFull());
	}

	public Response updateDocket(Docket docket) {
		JobEntity entity = dao.fetchByID(docket.getJobID());
		this.buildDocket(entity, docket);
		entity.overrideDocket(docket);
		entity = dao.merge(entity);
		return Response.Success(entity.pojoFull());
	}

	private void buildDocket(JobEntity entity, Docket docket) {
		entity.overrideDocket(docket);
		// docketBy
		if (entity.isDocketByChanged(docket.getDocketByID())) {
			UserEntity docketBy = this.userDao.fetchByID(docket.getDocketByID());
			entity.setDocketBy(docketBy);
			entity.setDocketById(docketBy.getId());
		}
		// client
		if (entity.isClientChanged(docket.getClientID())) {
			ClientEntity client = clientDao.fetchByID(docket.getClientID());
			entity.setClient(client);
			entity.setClientId(client.getId());
		}
	}

	public Response updateStudio(Studio pojo) {
		JobEntity job = dao.fetchByID(pojo.getJobID());
		StudioEntity studio = job.getStudio().override(pojo);
		// exeBy
		if (job.isExeByChanged(studio.getExeBy(), pojo.getExeByID())) {
			UserEntity exeBy = this.userDao.fetchByID(pojo.getExeByID());
			studio.setExeBy(exeBy);
			studio.setExeById(exeBy.getId());
		}
		studio = studioDao.merge(studio);
		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("studio", studio.pojoFull());
		return response;
	}

	public Response updateCtp(Ctp pojo) {
		JobEntity job = dao.fetchByID(pojo.getJobID());
		CTPEntity ctp = job.getCtp().override(pojo);
		// exeBy
		if (job.isExeByChanged(ctp.getExeBy(), pojo.getExeByID())) {
			UserEntity exeBy = this.userDao.fetchByID(pojo.getExeByID());
			ctp.setExeBy(exeBy);
			ctp.setExeById(exeBy.getId());
		}
		ctp = ctpDao.merge(ctp);
		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("ctp", ctp.pojoFull());
		return response;
	}

	public Response updateDelivery(Delivery pojo) {
		JobEntity job = dao.fetchByID(pojo.getJobID());
		DeliveryEntity delivery = job.getDelivery().override(pojo);
		// exeBy
		if (job.isExeByChanged(delivery.getExeBy(), pojo.getExeByID())) {
			UserEntity exeBy = userDao.fetchByID(pojo.getExeByID());
			delivery.setExeBy(exeBy);
			delivery.setExeById(exeBy.getId());
		}
		delivery = deliveryDao.merge(delivery);
		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("delivery", delivery.pojoFull());
		return response;
	}

	public Response updateBill(Bill pojo) {
		JobEntity job = dao.fetchByID(pojo.getJobID());
		BillEntity bill = job.getBill().override(pojo);
		// exeBy
		if (job.isExeByChanged(bill.getExeBy(), pojo.getExeByID())) {
			UserEntity exeBy = userDao.fetchByID(pojo.getExeByID());
			bill.setExeBy(exeBy);
			bill.setExeById(exeBy.getId());
		}
		bill = billDao.merge(bill);
		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("bill", bill.pojoFull());
		return response;
	}

	public Response updateChallan(Challan pojo) {
		JobEntity job = dao.fetchByID(pojo.getJobID());
		ChallanEntity challan = job.getChallan().override(pojo);
		// exeBy
		if (job.isExeByChanged(challan.getExeBy(), pojo.getExeByID())) {
			UserEntity exeBy = userDao.fetchByID(pojo.getExeByID());
			challan.setExeBy(exeBy);
			challan.setExeById(exeBy.getId());
		}
		challan = challanDao.merge(challan);
		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("challan", challan.pojoFull());
		return response;
	}

	public Response updatePlate(Plate platePojo) {
		JobEntity job = dao.fetchByID(platePojo.getJobID());
		PlateDetailEntity plateDetail = plateDetailDao.fetchByID(job.getPlateDetailID(), PlateDetailEntity.class);
		job.setPlateDetail(plateDetail);
		PlateEntity plate = job.getPlateDetail().processPlate(platePojo);
		plate = plateDao.saveOrUpdate(plate);
		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("plate", plate.pojoFull());
		return response;
	}

	public Response updateInstruction(Instruction pojo) {
		JobEntity job = dao.fetchByID(pojo.getJobID());
		String part = pojo.getPart();

		/** instructionDetail **/
		InstructionDetailEntity instructionDetail = null;
		// docket
		if (part.equals("docket")) {
			instructionDetail = instructionDetailDao.fetchByID(job.getInstructionDetailId());
		}
		// studio
		else if (part.equals("studio")) {
			StudioEntity studio = studioDao.fetchByID(job.getStudioId(), StudioEntity.class);
			instructionDetail = instructionDetailDao.fetchByID(studio.getInstructionDetailId());
		}
		// ctp
		else if (part.equals("ctp")) {
			CTPEntity ctp = ctpDao.fetchByID(job.getCtpId(), CTPEntity.class);
			instructionDetail = instructionDetailDao.fetchByID(ctp.getInstructionDetailId());
		}
		// delivery
		else if (part.equals("delivery")) {
			DeliveryEntity delivery = deliveryDao.fetchByID(job.getDeliveryId(), DeliveryEntity.class);
			instructionDetail = instructionDetailDao.fetchByID(delivery.getInstructionDetailId());
		}
		// bill
		else if (part.equals("bill")) {
			BillEntity bill = billDao.fetchByID(job.getBillId(), BillEntity.class);
			instructionDetail = instructionDetailDao.fetchByID(bill.getInstructionDetailId());
		}
		// challan
		else if (part.equals("challan")) {
			ChallanEntity challan = challanDao.fetchByID(job.getChallanId(), ChallanEntity.class);
			instructionDetail = instructionDetailDao.fetchByID(challan.getInstructionDetailId());
		}

		InstructionEntity instruction = instructionDetail.processInst(pojo);
		instruction = instDao.saveOrUpdate(instruction);

		job = dao.merge(job);

		Response response = new Response(job.pojoFull());
		response.getResponseData().put("instruction", instruction.pojoFull());
		return response;
	}

	public Response get(long id) {
		Job pojo = dao.fetchByID(id).pojoFull();
		return new Response(pojo);
	}

	public Response search(SearchInput searchInput) throws ParseException {
		// entities
		List<JobEntity> entities = dao.search(searchInput, JobEntity.class);
		// pojo, clientIds
		Set<Long> clientIds = new HashSet<>();
		List<Job> pojos = new ArrayList<>();
		for (JobEntity entity : entities) {
			clientIds.add(entity.getClientId());
			pojos.add(entity.pojo());
		}
		// job.clientName
		Map<Long, ClientEntity> clientMap = clientDao.searchById(clientIds);
		for (Job job : pojos) {
			job.setClientName(clientMap.get(job.getClientId()).getName());
		}
		// rowCount
		long rowCount = dao.getTotalRowCount(searchInput, JobEntity.class);
		// response
		Response response = new Response(pojos);
		response.getResponseData().put(Param.ROW_COUNT.name(), rowCount);
		return response;
	}
}