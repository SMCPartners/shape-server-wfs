package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationStratificationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationStratificationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationStratificationDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Responsible: Manage Organization Stratification data</br>
 * 1.  Implements OrganizationStartificationDAO</br>
 * <p>
 * Created by johndestefano on 10/29/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Stateless
public class OrganizationStratificationDAOImpl extends AbstractCrudDAO<OrganizationStratificationDTO, OrganizationStratificationEntity, Integer> implements OrganizationStratificationDAO {

    @Inject
    public OrganizationStratificationDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    public List<OrganizationStratificationDTO> findAllOrganizationStratification() throws DataAccessException {
        try {
            List<OrganizationStratificationEntity> omLst = em.createNamedQuery("OrganizationStratification.findAll").getResultList();

            List<OrganizationStratificationDTO> retLst = new ArrayList<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationStratificationDTO dto = this.mapEntityToDTO(om);
                    retLst.add(dto);
                }));
            }

            return retLst;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrganizationStratificationDTO> findAllOrganizationStratificationByOrgId(int orgId) throws DataAccessException {
        try {
            OrganizationEntity oe = em.find(OrganizationEntity.class, orgId);
            List<OrganizationStratificationEntity> omLst = em.createNamedQuery("OrganizationStratification.findAllByOrgId")
                    .setParameter("org", oe)
                    .getResultList();

            List<OrganizationStratificationDTO> retLst = new ArrayList<>();
            if (omLst != null && omLst.size() > 0) {
                omLst.forEach(Errors.rethrow().wrap(om -> {
                    OrganizationStratificationDTO dto = this.mapEntityToDTO(om);
                    retLst.add(dto);
                }));
            }

            return retLst;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    protected OrganizationStratificationEntity mapDtoToEntity(OrganizationStratificationEntity et, OrganizationStratificationDTO dto) {
        et.setAge17under(dto.getAge17under());
        et.setAge1844(dto.getAge1844());
        et.setAge4564(dto.getAge4564());
        et.setAgeOver65(dto.getAgeOver65());
        et.setEthnicityHispanicLatino(dto.getEthnicityHispanicLatino());
        et.setEthnicityNotHispanicLatino(dto.getEthnicityNotHispanicLatino());
        et.setGenderFemale(dto.getGenderFemale());
        et.setGenderMale(dto.getGenderMale());
        et.setId(dto.getId());
        et.setRaceAfricanAmerican(dto.getRaceAfricanAmerican());
        et.setRaceAmericanIndian(dto.getRaceAmericanIndian());
        et.setRaceAsian(dto.getRaceAsian());
        et.setRaceNativeHawaiian(dto.getRaceNativeHawaiian());
        et.setRaceOther(dto.getRaceOther());
        et.setRaceWhite(dto.getRaceWhite());
        et.setTotalPatients(dto.getTotalPatients());
        et.setTotalVisits(dto.getTotalVisits());
        et.setPatientsHypertension(dto.getPatientsHypertension());
        et.setPatientsDiabetes(dto.getPatientsDiabetes());
        et.setPatientsPreDiabetes(dto.getPatientsPreDiabetes());
        et.setPatientsHighBp(dto.getPatientsHighBp());
        et.setRpDate(new Date());
        et.setPrimaryCarePractice(dto.isPrimaryCarePractice());
        et.setFqhcLookALike(dto.isFqhcLookALike());
        et.setComHealthCenter(dto.isComHealthCenter());
        et.setMultiSpecPractice(dto.isMultiSpecPractice());
        et.setPracConsortium(dto.isPracConsortium());
        et.setAmbulatoryClinic(dto.isAmbulatoryClinic());
        et.setHmo(dto.isHmo());
        et.setAco(dto.isAco());
        et.setPcmh(dto.isPcmh());
        et.setOtherOrgDescrip(dto.isOtherOrgDescrip());
        et.setPhysicians(dto.isPhysicians());
        et.setNursePrac(dto.isNursePrac());
        et.setRn(dto.isRn());
        et.setLpn(dto.isLpn());
        et.setPa(dto.isPa());
        et.setMedicalAssist(dto.isMedicalAssist());
        et.setResidents(dto.isResidents());
        et.setInterns(dto.isInterns());
        et.setCommunityHealthWorkers(dto.isCommunityHealthWorkers());
        et.setTrainedMotivationalInterview(dto.isTrainedMotivationalInterview());
        et.setMedicarePercent(dto.getMedicarePercent());
        et.setMedicaidPercent(dto.getMedicaidPercent());
        et.setHmoPercent(dto.getHmoPercent());
        et.setPpoPercent(dto.getPpoPercent());
        et.setUninsuredSelfPercent(dto.getUninsuredSelfPercent());
        et.setPrivatePercent(dto.getPrivatePercent());
        et.setVendor(dto.getVendor());
        et.setProduct(dto.getProduct());
        et.setVersionEHR(dto.getVersionEHR());
        et.setCompleteCEHRT(dto.isCompleteCEHRT());
        et.setPatientPortal(dto.isPatientPortal());

        // Look up organization
        OrganizationEntity oe = em.find(OrganizationEntity.class, dto.getOrganizationId());
        et.setOrganizationByOrganizationId(oe);

        // User
        UserEntity user = em.find(UserEntity.class, dto.getUserId());
        et.setUserByUserId(user);

        // Return
        return et;
    }

    @Override
    protected Class<OrganizationStratificationEntity> getGenericEntityClass() throws Exception {
        return OrganizationStratificationEntity.class;
    }

    @Override
    protected OrganizationStratificationDTO mapEntityToDTO(OrganizationStratificationEntity entity) throws Exception {
        OrganizationStratificationDTO dto = new OrganizationStratificationDTO();
        dto.setAge17under(entity.getAge17under());
        dto.setAge1844(entity.getAge1844());
        dto.setAge4564(entity.getAge4564());
        dto.setAgeOver65(entity.getAgeOver65());
        dto.setEthnicityHispanicLatino(entity.getEthnicityHispanicLatino());
        dto.setEthnicityNotHispanicLatino(entity.getEthnicityNotHispanicLatino());
        dto.setGenderFemale(entity.getGenderFemale());
        dto.setGenderMale(entity.getGenderMale());
        dto.setId(entity.getId());
        dto.setRaceAfricanAmerican(entity.getRaceAfricanAmerican());
        dto.setRaceAmericanIndian(entity.getRaceAmericanIndian());
        dto.setRaceAsian(entity.getRaceAsian());
        dto.setRaceNativeHawaiian(entity.getRaceNativeHawaiian());
        dto.setRaceOther(entity.getRaceOther());
        dto.setRaceWhite(entity.getRaceWhite());
        dto.setOrganizationId(entity.getOrganizationByOrganizationId().getId());
        dto.setUserId(entity.getUserByUserId().getId());
        dto.setRpDate(entity.getRpDate());
        dto.setTotalPatients(entity.getTotalPatients());
        dto.setTotalVisits(entity.getTotalVisits());
        dto.setPatientsHypertension(entity.getPatientsHypertension());
        dto.setPatientsDiabetes(entity.getPatientsDiabetes());
        dto.setPatientsPreDiabetes(entity.getPatientsPreDiabetes());
        dto.setPatientsHighBp(entity.getPatientsHighBp());
        dto.setPrimaryCarePractice(entity.isPrimaryCarePractice());
        dto.setFqhcLookALike(entity.isFqhcLookALike());
        dto.setComHealthCenter(entity.isComHealthCenter());
        dto.setMultiSpecPractice(entity.isMultiSpecPractice());
        dto.setPracConsortium(entity.isPracConsortium());
        dto.setAmbulatoryClinic(entity.isAmbulatoryClinic());
        dto.setHmo(entity.isHmo());
        dto.setAco(entity.isAco());
        dto.setPcmh(entity.isPcmh());
        dto.setOtherOrgDescrip(entity.isOtherOrgDescrip());
        dto.setPhysicians(entity.isPhysicians());
        dto.setNursePrac(entity.isNursePrac());
        dto.setRn(entity.isRn());
        dto.setLpn(entity.isLpn());
        dto.setPa(entity.isPa());
        dto.setMedicalAssist(entity.isMedicalAssist());
        dto.setResidents(entity.isResidents());
        dto.setInterns(entity.isInterns());
        dto.setCommunityHealthWorkers(entity.isCommunityHealthWorkers());
        dto.setTrainedMotivationalInterview(entity.isTrainedMotivationalInterview());
        dto.setMedicarePercent(entity.getMedicarePercent());
        dto.setMedicaidPercent(entity.getMedicaidPercent());
        dto.setHmoPercent(entity.getHmoPercent());
        dto.setPpoPercent(entity.getPpoPercent());
        dto.setUninsuredSelfPercent(entity.getUninsuredSelfPercent());
        dto.setPrivatePercent(entity.getPrivatePercent());
        dto.setVendor(entity.getVendor());
        dto.setProduct(entity.getProduct());
        dto.setVersionEHR(entity.getVersionEHR());
        dto.setCompleteCEHRT(entity.isCompleteCEHRT());
        dto.setPatientPortal(entity.isPatientPortal());
        return dto;
    }
}
