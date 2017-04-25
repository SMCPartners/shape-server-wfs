package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.crosscutting.activitylogging.dto.ClickLogDTO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ClickLogDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.ClickLogEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Responsible:</br>
 * 1. Implements ClickLogDAO.</br>
 * <p>
 * Created by johndestefano on 10/3/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class ClickLogDAOImpl extends AbstractCrudDAO<ClickLogDTO, ClickLogEntity, Integer> implements ClickLogDAO {

    @Inject
    public ClickLogDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ClickLogDTO> findByUser(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            Query qry = em.createNamedQuery("ClickLog.findByUser");
            qry.setParameter("uId", ue);
            List<ClickLogEntity> eLst = qry.getResultList();

            // Create return list
            List<ClickLogDTO> retLst = new ArrayList<>();
            eLst.forEach(Errors.rethrow().wrap(ce -> {
                ClickLogDTO dto = this.mapEntityToDTO(ce);
                retLst.add(dto);
            }));

            // Return list
            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findByUserIdAndDate", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    protected Class<ClickLogEntity> getGenericEntityClass() throws Exception {
        return ClickLogEntity.class;
    }

    @Override
    protected ClickLogDTO mapEntityToDTO(ClickLogEntity entity) throws Exception {
        ClickLogDTO dto = new ClickLogDTO();
        dto.setAdditionalInfo(entity.getAdditionalInfo());
        dto.setClickLogId(entity.getClickLogId());
        dto.setEvent(entity.getEvent());
        dto.setEventDt(entity.getEventDt());
        dto.setUserId(entity.getUserByUserId().getId());
        dto.setRequestInfo(entity.getRequestInfo());
        dto.setResponseInfo(entity.getResponseInfo());
        return dto;
    }

    @Override
    protected ClickLogEntity mapDtoToEntity(ClickLogEntity et, ClickLogDTO dto) {
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        et.setAdditionalInfo(dto.getAdditionalInfo());
        et.setClickLogId(dto.getClickLogId());
        et.setEvent(dto.getEvent());
        et.setEventDt(dto.getEventDt());
        et.setUserByUserId(user);
        et.setRequestInfo(dto.getRequestInfo());
        et.setResponseInfo(dto.getResponseInfo());
        return et;
    }
}
