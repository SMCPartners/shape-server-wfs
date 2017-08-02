package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.crosscutting.logging.filters.LogDTO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.LogDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.LogEntity;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Responsibility: Manage Logging entity data</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/26/17
 */
@Stateless
public class LogDAOImpl extends AbstractCrudDAO<LogDTO, LogEntity, Integer> implements LogDAO {

    /**
     * Constructor
     */
    @Inject
    public LogDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    protected LogEntity mapDtoToEntity(LogEntity et, LogDTO dto) {
        et.setRequestDt(dto.getRequestDt());
        et.setRequestEntity(dto.getRequestEntity());
        et.setRequestHeader(dto.getRequestHeader());
        et.setRequestPath(dto.getRequestPath());
        et.setResponseDt(dto.getResponseDt());
        et.setResponseEntity(dto.getResponseEntity());
        et.setResponseHeader(dto.getResponseHeader());
        et.setUser(dto.getUser());
        return et;
    }

    @Override
    protected Class<LogEntity> getGenericEntityClass() throws Exception {
        return LogEntity.class;
    }

    @Override
    protected LogDTO mapEntityToDTO(LogEntity entity) throws Exception {
        LogDTO dto = new LogDTO();
        dto.setResponseHeader(entity.getResponseHeader());
        dto.setResponseEntity(dto.getResponseEntity());
        dto.setResponseDt(entity.getResponseDt());
        dto.setRequestHeader(entity.getRequestHeader());
        dto.setRequestPath(entity.getRequestPath());
        dto.setRequestEntity(entity.getRequestEntity());
        dto.setRequestDt(entity.getRequestDt());
        dto.setId(entity.getId());
        dto.setUser(entity.getUser());
        return dto;
    }
}
