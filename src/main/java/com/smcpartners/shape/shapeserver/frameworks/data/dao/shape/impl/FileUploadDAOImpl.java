package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.FileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.FileUploadEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.MeasureEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.FileUploadRequestDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Responsible:<br/>
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
public class FileUploadDAOImpl extends AbstractCrudDAO<FileUploadRequestDTO, FileUploadEntity, Integer> implements FileUploadDAO {

    @Inject
    public FileUploadDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    protected FileUploadEntity mapDtoToEntity(FileUploadEntity et, FileUploadRequestDTO dto) {
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        OrganizationEntity org = em.find(OrganizationEntity.class, dto.getOrgId());
        MeasureEntity measureEntity = em.find(MeasureEntity.class, dto.getMeasureEntityId());
        et.setFileUploadId(dto.getFileUploadId());
        et.setMeasureEntityByMeasureEntityId(measureEntity);
        et.setUploadDt(dto.getUploadDt());
        et.setUploadedB64File(dto.getUploadedB64File());
        et.setUserByUserId(user);
        et.setOrganizationEntity(org);
        return et;
    }

    @Override
    protected Class<FileUploadEntity> getGenericEntityClass() throws Exception {
        return FileUploadEntity.class;
    }

    @Override
    protected FileUploadRequestDTO mapEntityToDTO(FileUploadEntity entity) throws Exception {
        FileUploadRequestDTO dto = new FileUploadRequestDTO();
        dto.setFileUploadId(entity.getFileUploadId());
        dto.setMeasureEntityId(entity.getMeasureEntityByMeasureEntityId().getId());
        dto.setUploadDt(entity.getUploadDt());
        dto.setUploadedB64File(entity.getUploadedB64File());
        dto.setUserId(entity.getUserByUserId().getId());
        dto.setOrgId(entity.getOrganizationEntity().getId());
        return dto;
    }
}