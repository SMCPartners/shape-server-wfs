package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserProviderDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.ProviderEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserProviderEntity;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserProviderDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Responsible: Manage User Provider Entity data</br>
 * 1.  Implements UserProviderDAO</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class UserProviderDAOImpl extends AbstractCrudDAO<UserProviderDTO, UserProviderEntity, Integer> implements UserProviderDAO {

    /**
     * Constructor
     */
    @Inject
    public UserProviderDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    protected UserProviderEntity mapDtoToEntity(UserProviderEntity et, UserProviderDTO dto) {
        et.setId(dto.getId());

        if (dto.getProviderId() != 0) {
            ProviderEntity pe = em.find(ProviderEntity.class, dto.getProviderId());
            et.setProviderByProviderId(pe);
        }

        if (dto.getUserId() != null) {
            UserEntity ue = em.find(UserEntity.class, dto.getProviderId());
            et.setUserByUserId(ue);
        }

        return et;
    }

    @Override
    protected Class<UserProviderEntity> getGenericEntityClass() throws Exception {
        return UserProviderEntity.class;
    }

    @Override
    protected UserProviderDTO mapEntityToDTO(UserProviderEntity entity) throws Exception {
        UserProviderDTO dto = new UserProviderDTO();
        dto.setId(entity.getId());

        if (entity.getProviderByProviderId() != null) {
            dto.setProviderId(entity.getProviderByProviderId().getId());
        }

        if (entity.getUserByUserId() != null) {
            dto.setUserId(entity.getUserByUserId().getId());
        }

        return dto;
    }
}
