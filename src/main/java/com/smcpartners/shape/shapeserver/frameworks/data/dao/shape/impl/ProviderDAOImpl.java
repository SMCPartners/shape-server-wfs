package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.ProviderDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.ProviderEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Responsible: Manage Provider Entity data</br>
 * 1.  Implements ProviderDAO</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class ProviderDAOImpl extends AbstractCrudDAO<ProviderDTO, ProviderEntity, Integer> implements ProviderDAO {

    @Override
    public List<ProviderDTO> findAll() throws DataAccessException {
        try {
            List<ProviderEntity> pLst = em.createNamedQuery("Provider.findAll").getResultList();
            List<ProviderDTO> retLst = new ArrayList<>();
            pLst.forEach(Errors.rethrow().wrap(p -> {
                ProviderDTO dto = mapEntityToDTO(p);
                retLst.add(dto);
            }));
            return retLst;
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<ProviderDTO> findAllByOrg(int orgId) throws DataAccessException {
        try {
            OrganizationEntity oe = em.find(OrganizationEntity.class, orgId);
            List<ProviderEntity> pLst = em.createNamedQuery("Provider.findByOrg")
                    .setParameter("org", oe).getResultList();

            List<ProviderDTO> retLst = new ArrayList<>();
            pLst.forEach(Errors.rethrow().wrap(p -> {
                ProviderDTO dto = this.mapEntityToDTO(p);
                retLst.add(dto);
            }));
            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllByOrg", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void changeProviderActiveStatus(int id, boolean status) throws DataAccessException {
        try {
            ProviderEntity prov = em.find(ProviderEntity.class, id);
            prov.setActive(status);
            em.merge(prov);
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "changeProviderActiveStatus", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    /**
     * Constructor
     */
    @Inject
    public ProviderDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    protected ProviderEntity mapDtoToEntity(ProviderEntity et, ProviderDTO dto) {
        et.setId(dto.getId());
        et.setName(dto.getName());
        et.setNpi(dto.getNpi());
        et.setActive(dto.isActive());
        et.setCreatedBy(dto.getCreatedBy());
        et.setModifiedBy(dto.getModifiedBy());
        et.setModifiedDt(new Date());

        if (dto.getOrganizationId() != 0) {
            OrganizationEntity oe = em.find(OrganizationEntity.class, dto.getOrganizationId());
            et.setOrganizationById(oe);
        }

        // Return object
        return et;
    }

    @Override
    protected Class<ProviderEntity> getGenericEntityClass() throws Exception {
        return ProviderEntity.class;
    }

    @Override
    protected ProviderDTO mapEntityToDTO(ProviderEntity entity) throws Exception {
        ProviderDTO dto = new ProviderDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setNpi(entity.getNpi());
        dto.setActive(entity.isActive());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDt(entity.getModifiedDt());

        if (entity.getOrganizationById() != null) {
            dto.setOrganizationId(entity.getOrganizationById().getId());
        }

        // Return dto
        return dto;
    }
}
