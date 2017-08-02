package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureFileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.*;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.logging.Level;

/**
 * Responsible: Manage Organization Measure File Upload Entity data<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 5/9/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. Added Organization reference - 7/19/17 - johndestefano<br/>
 * </p>
 */
@Stateless
public class OrganizationMeasureFileUploadDAOImpl
        extends AbstractCrudDAO<OrganizationMeasureFileUploadRequestDTO, OrganizationMeasureFileUploadEntity, Integer>
        implements OrganizationMeasureFileUploadDAO {

    @Inject
    public OrganizationMeasureFileUploadDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    /**
     * Find the file upload for the given organization measure and upload date and time
     *
     * @param orgMeasureId
     * @param dt
     * @return
     * @throws DataAccessException
     */
    @Override
    public OrganizationMeasureFileUploadRequestDTO getOrgMeasureFileUploadForOrgMeasureIdAndUploadDt(int orgMeasureId, Date dt) throws DataAccessException {
        try {
            OrganizationMeasureFileUploadEntity en = em.createNamedQuery("OrganizationMeasureFileUpload.findByOrgMeasureIdAndUploadDt", OrganizationMeasureFileUploadEntity.class)
                    .setParameter("omId", orgMeasureId)
                    .setParameter("fileUploadDt", dt)
                    .getSingleResult();

            OrganizationMeasureFileUploadRequestDTO ret = mapEntityToDTO(en);
            return ret;
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "getOrgMeasureFileUploadForOrgMeasureIdAndUploadDt", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    protected OrganizationMeasureFileUploadEntity mapDtoToEntity(OrganizationMeasureFileUploadEntity et, OrganizationMeasureFileUploadRequestDTO dto) {
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        OrganizationEntity org = em.find(OrganizationEntity.class, dto.getOrgId());
        MeasureEntity measureEntity = em.find(MeasureEntity.class, dto.getMeasureEntityId());
        OrganizationMeasureEntity orgMeasure = em.find(OrganizationMeasureEntity.class, dto.getOrgMeasureId());
        et.setFileUploadId(dto.getFileUploadId());
        et.setMeasureEntityByMeasureEntityId(measureEntity);
        et.setUploadDt(dto.getUploadDt());
        et.setUploadedB64File(dto.getUploadedB64File());
        et.setFileName(dto.getFileName());
        et.setUserByUserId(user);
        et.setOrganizationEntity(org);
        et.setOrganizationMeasureEntity(orgMeasure);
        return et;
    }

    @Override
    protected Class<OrganizationMeasureFileUploadEntity> getGenericEntityClass() throws Exception {
        return OrganizationMeasureFileUploadEntity.class;
    }

    @Override
    protected OrganizationMeasureFileUploadRequestDTO mapEntityToDTO(OrganizationMeasureFileUploadEntity entity) throws Exception {
        OrganizationMeasureFileUploadRequestDTO dto = new OrganizationMeasureFileUploadRequestDTO();
        dto.setFileUploadId(entity.getFileUploadId());
        dto.setMeasureEntityId(entity.getMeasureEntityByMeasureEntityId().getId());
        dto.setUploadDt(entity.getUploadDt());
        dto.setUploadedB64File(entity.getUploadedB64File());
        dto.setFileName(entity.getFileName());
        dto.setUserId(entity.getUserByUserId().getId());
        dto.setOrgId(entity.getOrganizationEntity().getId());
        dto.setOrgMeasureId(entity.getOrganizationMeasureEntity().getId());
        return dto;
    }
}