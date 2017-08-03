package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationStratificationFileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationStratificationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationStratificationFileUploadEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationStratificationFileUploadRequestDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Responsibility: Manage Organization Stratification File Upload data</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/24/17
 */
@Stateless
public class OrganizationStratificationFileUploadDAOImpl extends
        AbstractCrudDAO<OrganizationStratificationFileUploadRequestDTO, OrganizationStratificationFileUploadEntity, Integer>
        implements OrganizationStratificationFileUploadDAO {

    @Inject
    public OrganizationStratificationFileUploadDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    protected OrganizationStratificationFileUploadEntity mapDtoToEntity(OrganizationStratificationFileUploadEntity et,
                                                                        OrganizationStratificationFileUploadRequestDTO dto) {
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        OrganizationEntity org = em.find(OrganizationEntity.class, dto.getOrgId());
        OrganizationStratificationEntity orgStrat = em.find(OrganizationStratificationEntity.class, dto.getOrgStratId());
        et.setFileName(dto.getFileName());
        et.setOrganizationEntity(org);
        et.setOrganizationStratificationEntity(orgStrat);
        et.setOrgStratUploadId(dto.getOrgStratUploadId());
        et.setUploadDt(dto.getUploadDt());
        et.setUploadedB64File(dto.getUploadedB64File());
        et.setUserByUserId(user);
        return et;
    }

    @Override
    protected Class<OrganizationStratificationFileUploadEntity> getGenericEntityClass() throws Exception {
        return OrganizationStratificationFileUploadEntity.class;
    }

    @Override
    protected OrganizationStratificationFileUploadRequestDTO mapEntityToDTO(OrganizationStratificationFileUploadEntity entity) throws Exception {
        OrganizationStratificationFileUploadRequestDTO dto = new OrganizationStratificationFileUploadRequestDTO();
        dto.setFileName(entity.getFileName());
        dto.setOrgId(entity.getOrganizationEntity().getId());
        dto.setOrgStratUploadId(entity.getOrgStratUploadId());
        dto.setUploadDt(entity.getUploadDt());
        dto.setUploadedB64File(entity.getUploadedB64File());
        dto.setUserId(entity.getUserByUserId().getId());
        return dto;
    }
}
