package com.kanuhasu.ap.business.dao.impl.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kanuhasu.ap.business.bo.user.IDDetailEntity;
import com.kanuhasu.ap.business.dao.impl.AbstractDAO;

@Repository
@Transactional
public class IDDetailDAOImpl extends AbstractDAO<IDDetailEntity>{
}