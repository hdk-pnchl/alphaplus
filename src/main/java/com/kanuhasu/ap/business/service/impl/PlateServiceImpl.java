package com.kanuhasu.ap.business.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.Response;
import com.kanuhasu.ap.business.bo.job.PlateEntity;
import com.kanuhasu.ap.business.dao.impl.PlateDAOImpl;
import com.kanuhasu.ap.business.pojo.Plate;
import com.kanuhasu.ap.business.type.response.Param;
import com.kanuhasu.ap.business.util.SearchInput;

@Service
@Transactional
public class PlateServiceImpl {

	@Autowired
	private PlateDAOImpl dao;

	public Response empty() {
		return Response.Success(new PlateEntity().pojoFull());
	}

	public Response newPlate(Plate pojo) {
		PlateEntity entity = new PlateEntity();
		entity.override(pojo);
		entity = dao.save(entity);
		return Response.Success(entity.pojoFull());
	}

	public Response update(Plate pojo) {
		PlateEntity entity = dao.fetchByID(pojo.getId());
		entity.override(pojo);
		entity = dao.update(entity);
		return Response.Success(entity.pojoFull());
	}

	public Response get(long id) {
		PlateEntity entity = dao.fetchByID(id);
		return Response.Success(entity.pojoFull());
	}

	public Response search(SearchInput searchInput) throws ParseException {
		List<PlateEntity> entities = dao.search(searchInput, PlateEntity.class);
		List<Plate> pojos = new ArrayList<>();
		for (PlateEntity entity : entities) {
			pojos.add(entity.pojo());
		}
		long rowCount = dao.getTotalRowCount(searchInput, PlateEntity.class);
		Response response = new Response(pojos);
		response.putParam(Param.ROW_COUNT.name(), rowCount);
		return response;
	}
}