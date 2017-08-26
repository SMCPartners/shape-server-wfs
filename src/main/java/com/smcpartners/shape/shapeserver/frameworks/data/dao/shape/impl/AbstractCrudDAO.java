package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;


import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;

import javax.persistence.EntityManager;

/**
 * Responsible: Abstract generic class to support CRUD functionality for data access implementation classes</br>
 * 1. </br>
 *
 * <p>
 * Created by johndestefano on 9/12/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
public abstract class AbstractCrudDAO<T, E, K> implements CrudDAO<T, K> {

    /**
     * Child classes must set this through constructor injection
     */
    protected EntityManager em;

    @Override
    //@Transactional
    public void delete(K key) throws DataAccessException {
        try {
            E e = em.find(this.getGenericEntityClass(), key);
            em.remove(e);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public T findById(K key) throws DataAccessException {
        try {
            E e = em.find(this.getGenericEntityClass(), key);
            return this.mapEntityToDTO(e);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public T create(T dto) throws DataAccessException {
        try {
            E et = this.getGenericEntityClass().newInstance();
            et = this.mapDtoToEntity(et, dto);
            em.persist(et);
            em.flush();
            em.refresh(et);
            return this.mapEntityToDTO(et);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Update an Entity
     *
     * @param dto
     * @return
     * @throws DataAccessException
     */
    @Override
    public T update(T dto, K key) throws DataAccessException {
        try {
            E et = em.find(this.getGenericEntityClass(), key);
            et = this.mapDtoToEntity(et, dto);
            et = em.merge(et);
            return this.mapEntityToDTO(et);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Maps the DTO to the entity bean
     *
     * @param et
     * @param dto
     * @return
     */
    protected abstract E mapDtoToEntity(E et, T dto);

    /**
     * Gets the Entity bean class
     *
     * @return
     * @throws Exception
     */
    protected abstract Class<E> getGenericEntityClass() throws Exception;

    /**
     * Maps the entity class to the DTO
     *
     * @param entity
     * @return
     * @throws Exception
     */
    protected abstract T mapEntityToDTO(E entity) throws Exception;

    /**
     * Figures out the Entity bean type for use with the entity bean manager.
     * This is cluncky but effective.</br>
     * Alternative is to have a method in the extending class that returns
     * the entity bean class from the generic type passed in.
     *
     * @return
     * @throws Exception
     */
    /*
    public Class<E> getGenericClass() throws Exception {
        if (entityBeanClazz == null) {
            Type mySuperclass = getClass().getGenericSuperclass();
            Type tType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[1];
            String className = tType.getTypeName();
            this.entityBeanClazz = (Class<E>) Class.forName(className);
        }
        return entityBeanClazz;
    }
    */
}

