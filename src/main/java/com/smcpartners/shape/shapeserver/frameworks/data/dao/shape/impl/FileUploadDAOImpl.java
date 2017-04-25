package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.FileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.FileUploadEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.MeasureEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.FileUploadRequestDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Level;

/**
 * Responsible:<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 5/9/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. <br/>
 * </p>
 */
@Stateless
public class FileUploadDAOImpl extends AbstractCrudDAO<FileUploadRequestDTO, FileUploadEntity, Integer> implements FileUploadDAO {

    @Inject
    public FileUploadDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    /**
     * Create an OrganizationMeasure and File Upload record. If there is
     * an error then log the error but don't throw an exception. Just return a false result.
     *
     * @param measureDTO
     * @param fileUpload
     * @return
     * @throws DataAccessException
     */
    @Override
    public BooleanValueDTO createAndLogFileMeasureUpload(OrganizationMeasureDTO measureDTO, FileUploadRequestDTO fileUpload) throws DataAccessException {
        try {
            return BooleanValueDTO.get(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "createAndLogFileMeasureUpload", e.getMessage(), e);
            //throw new DataAccessException(e);
            return BooleanValueDTO.get(false);
        }
    }

    @Override
    protected FileUploadEntity mapDtoToEntity(FileUploadEntity et, FileUploadRequestDTO dto) {
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        MeasureEntity measureEntity = em.find(MeasureEntity.class, dto.getMeasureEntityId());
        et.setFileUploadId(dto.getFileUploadId());
        et.setMeasureEntityByMeasureEntityId(measureEntity);
        et.setUploadDt(dto.getUploadDt());
        et.setUploadedB64File(dto.getUploadedB64File());
        et.setUserByUserId(user);
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
        return dto;
    }
}
