package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Responsible: Manage Organization entity data</br>
 * 1.  Implements OrganizationDAO</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class OrganizationDAOImpl extends AbstractCrudDAO<OrganizationDTO, OrganizationEntity, Integer> implements OrganizationDAO {

    /**
     * Constructor
     */
    @Inject
    public OrganizationDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    public List<OrganizationDTO> findAll() throws DataAccessException {
        try {
            List<OrganizationEntity> oLst = em.createNamedQuery("Organization.findAll").getResultList();

            List<OrganizationDTO> retLst = new ArrayList<>();
            oLst.forEach(Errors.rethrow().wrap(o -> {
                OrganizationDTO dto = this.mapEntityToDTO(o);
                retLst.add(dto);
            }));

            return retLst;
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }


    @Override
    public void changeOrganizationActiveStatus(int id, boolean status) throws DataAccessException {
        try {
            OrganizationEntity orgEnt = em.find(OrganizationEntity.class, id);
            orgEnt.setActive(status);
            em.merge(orgEnt);
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "inactivateOrganization", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }


    /**
     * Don't map the collections of
     *
     * @param oe
     * @param dto
     * @return
     */
    @Override
    protected OrganizationEntity mapDtoToEntity(OrganizationEntity oe, OrganizationDTO dto) {
        oe.setAddressCity(dto.getAddressCity());
        oe.setAddressState(dto.getAddressState());
        oe.setAddressStreet(dto.getAddressStreet());
        oe.setAddressZip(dto.getAddressZip());
        oe.setId(dto.getId());
        oe.setName(dto.getName());
        oe.setPhone(dto.getPhone());
        oe.setActive(dto.isActive());
        oe.setPrimaryContactEmail(dto.getPrimaryContactEmail());
        oe.setPrimaryContactName(dto.getPrimaryContactName());
        oe.setPrimaryContactPhone(dto.getPrimaryContactPhone());
        oe.setPrimaryContactRole(dto.getPrimaryContactRole());
        oe.setCreatedBy(dto.getCreatedBy());
        oe.setModifiedBy(dto.getModifiedBy());
        oe.setModifiedDt(new Date());
        return oe;
    }

    @Override
    protected Class<OrganizationEntity> getGenericEntityClass() throws Exception {
        return OrganizationEntity.class;
    }

    @Override
    protected OrganizationDTO mapEntityToDTO(OrganizationEntity e) throws Exception {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setAddressCity(e.getAddressCity());
        dto.setAddressState(e.getAddressState());
        dto.setAddressStreet(e.getAddressStreet());
        dto.setAddressZip(e.getAddressZip());
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setPhone(e.getPhone());
        dto.setActive(e.isActive());
        dto.setPrimaryContactEmail(e.getPrimaryContactEmail());
        dto.setPrimaryContactName(e.getPrimaryContactName());
        dto.setPrimaryContactPhone(e.getPrimaryContactPhone());
        dto.setPrimaryContactRole(e.getPrimaryContactRole());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setModifiedBy(e.getModifiedBy());
        dto.setModifiedDt(e.getModifiedDt());
        return dto;
    }
}
