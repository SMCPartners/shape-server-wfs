package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.MeasureEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderMeasureResponseDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Responsible: Manage Measure entity data</br>
 * 1.  Implements MeasureDAO</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class MeasureDAOImpl extends AbstractCrudDAO<MeasureDTO, MeasureEntity, Integer> implements MeasureDAO {

    /**
     * Constructor
     */
    @Inject
    public MeasureDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    public void changeMeasureSelectStatus(int id, boolean status) throws DataAccessException {
        try {
            MeasureEntity m = em.find(MeasureEntity.class, id);
            m.setSelected(status);
            em.merge(m);
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "changeMeasureSelectStatus", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<MeasureDTO> findActiveMeasuresByName(String measureName) throws DataAccessException {
        try {
            List<MeasureEntity> mLst = em.createNamedQuery("Measure.findAllByName")
                    .setParameter("name", measureName)
                    .getResultList();
            List<MeasureDTO> retLst = new ArrayList<>();
            mLst.forEach(Errors.rethrow().wrap(m -> {
                MeasureDTO dto = this.mapEntityToDTO(m);
                retLst.add(dto);
            }));
            return retLst;
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findActiveMeasuresByName", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<MeasureDTO> findAllMeasures() throws DataAccessException {
        try {
            List<MeasureEntity> mLst = em.createNamedQuery("Measure.findAll").getResultList();
            List<MeasureDTO> retLst = new ArrayList<>();
            mLst.forEach(Errors.rethrow().wrap(m -> {
                MeasureDTO dto = this.mapEntityToDTO(m);
                retLst.add(dto);
            }));
            return retLst;
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllMeasures", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<MeasureDTO> findAllMeasuresById(int measureId) throws DataAccessException {
        try {
            MeasureEntity me = em.find(MeasureEntity.class, measureId);
            List<MeasureEntity> mLst = em.createNamedQuery("Measure.findAllById")
                    .setParameter("id", me)
                    .getResultList();
            List<MeasureDTO> retLst = new ArrayList<>();
            mLst.forEach(Errors.rethrow().wrap(m -> {
                MeasureDTO dto = this.mapEntityToDTO(m);
                retLst.add(dto);
            }));
            return retLst;
        } catch(Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAllMeasuresById", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }


    @Override
    protected MeasureEntity mapDtoToEntity(MeasureEntity e, MeasureDTO dto) {
        e.setDenominatorDescription(dto.getDenominatorDescription());
        e.setDescription(dto.getDescription());
        e.setExclusionsDescription(dto.getExclusionsDescription());
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setNqfId(dto.getNqfId());
        e.setNumeratorDescription(dto.getNumeratorDescription());
        e.setSelected(dto.isSelected());
        e.setWellControlledNumerator(dto.isWellControlledNumerator());
        e.setActive(dto.isActive());
        return e;
    }

    @Override
    protected Class<MeasureEntity> getGenericEntityClass() throws Exception {
        return MeasureEntity.class;
    }

    @Override
    protected MeasureDTO mapEntityToDTO(MeasureEntity entity) throws Exception {
        MeasureDTO dto = new MeasureDTO();
        dto.setDenominatorDescription(entity.getDenominatorDescription());
        dto.setDescription(entity.getDescription());
        dto.setExclusionsDescription(entity.getExclusionsDescription());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setNqfId(entity.getNqfId());
        dto.setNumeratorDescription(entity.getNumeratorDescription());
        dto.setSelected(entity.isSelected());
        dto.setWellControlledNumerator(entity.isWellControlledNumerator());
        dto.setActive(entity.isActive());

        // Check provider measures
        List<ProviderMeasureResponseDTO> pmLst = new ArrayList<>();
        return dto;
    }

}
