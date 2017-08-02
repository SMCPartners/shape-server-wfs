package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.impl;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.OrganizationEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape.UserEntity;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.frameworks.producers.annotations.ShapeDatabase;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.utils.SaltedPassword;
import com.smcpartners.shape.shapeserver.shared.utils.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Responsible: Manage User Entity data<br/>
 * 1.  Implements UserDAO
 * <p>
 * Created by johndestefano on 9/10/15.
 * <p>
 * Changes:<b/>
 */
@Stateless
public class UserDAOImpl extends AbstractCrudDAO<UserDTO, UserEntity, String> implements UserDAO {

    /**
     * Constructor
     */
    @Inject
    public UserDAOImpl(@ShapeDatabase EntityManager em) {
        this.em = em;
    }

    @Override
    public void setUserResetPwdChallenge(String userId, int choice) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            ue.setUserResetPwdChallenge(choice);
            em.merge(ue);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "validateUser", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDTO validateUser(String userId, String password) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {
                if (SecurityUtils.validatePassword(password, ue.getPasswordDigest(), ue.getPasswordSalt(), false) == true) {
                    return this.mapEntityToDTO(ue);
                }
            }

            // User not found or pwd not valid
            return null;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "validateUser", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    public UserEntity addUser(String userId, String password, String createdBy, String role, boolean active, Date createDt, int organizationId, String firstName,
                              String lastName, String email, boolean resetPwd) throws DataAccessException {
        try {
            // Check that userId is unique
            boolean isIdInUser = (this.checkUserId(userId)).isValue();

            if (isIdInUser == false) {
                // Get the salt and digest
                SaltedPassword sp = SecurityUtils.genSaltedPasswordAndSalt(password);

                // Create new entity
                UserEntity ue = new UserEntity();

                // Look up organization
                OrganizationEntity oe = em.find(OrganizationEntity.class, organizationId);
                ue.setOrganizationById(oe);

                ue.setActive(active);
                ue.setAdmin(false);
                ue.setCreateDt(new Date());
                ue.setCreatedBy(createdBy);
                ue.setModifiedDt(new Date());
                ue.setGeneratedPwdDt(new Date());
                ue.setId(userId);
                ue.setPasswordDigest(sp.getPwdDigest());
                ue.setPasswordSalt(sp.getSalt());
                ue.setRole(role);
                ue.setFirstName(firstName);
                ue.setLastName(lastName);
                ue.setEmail(email);
                ue.setResetPwd(resetPwd);
                ue.setUserResetPwdChallenge(0);
                em.persist(ue);
                em.flush();
                em.refresh(ue);
                return ue;
            } else {
                throw new DataAccessException("The user id is already in use.");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addUser", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    public void addUserSecurityQuestions(String userId, String question1, String question2,
                                         String answer1, String answer2) throws DataAccessException {
        try {
            // Find user
                UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {
                ue.setQuestionOne(question1);
                ue.setQuestionTwo(question2);
                ue.setAnswerOne(answer1);
                ue.setAnswerTwo(answer2);
                em.merge(ue);
            } else {
                throw new DataAccessException("The user does not exist");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addUserSecurityQuestions", e.getMessage(), e);
            throw new DataAccessException(e);
        }

    }


    @Override
    public UserDTO changePassword(String userId, String oldpassword, String newpassword) throws
            DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {
                // Test the old password
                boolean oldPwdValid = SecurityUtils.validatePassword(oldpassword, ue.getPasswordDigest(), ue.getPasswordSalt(), false);
                if (oldPwdValid == false) {
                    throw new DataAccessException("Old password invalid.");
                }

                // Get the salt and digest
                SaltedPassword sp = SecurityUtils.genSaltedPasswordAndSalt(newpassword);

                // Update and save
                ue.setPasswordSalt(sp.getSalt());
                ue.setPasswordDigest(sp.getPwdDigest());
                ue.setUserResetPwdChallenge(0);
                ue = em.merge(ue);
                return this.mapEntityToDTO(ue);
            } else {
                throw new DataAccessException("User does not exist!");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "changePassword", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void forcePasswordChange(String userId, String newPassword) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {

                // Get the salt and digest
                SaltedPassword sp = SecurityUtils.genSaltedPasswordAndSalt(newPassword);

                // Update and save
                ue.setPasswordSalt(sp.getSalt());
                ue.setPasswordDigest(sp.getPwdDigest());
                ue.setUserResetPwdChallenge(0);
                ue = em.merge(ue);
            } else {
                throw new DataAccessException("User does not exist!");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "forcePasswordChane", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public BooleanValueDTO checkUserId(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {
                return new BooleanValueDTO(true);
            }
            return new BooleanValueDTO(false);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "checkUserId", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<UserDTO> findAll() throws DataAccessException {
        try {
            List<UserEntity> eLst = em.createNamedQuery("User.findAll").getResultList();

            // Return DTO list
            List<UserDTO> retLst = new ArrayList<>();
            eLst.forEach(Errors.rethrow().wrap(e -> {
                UserDTO dto = this.mapEntityToDTO(e);
                retLst.add(dto);
            }));

            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void inactivateUser(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {
                ue.setActive(false);
                em.merge(ue);
            } else {
                throw new DataAccessException("User not found.");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void activateUser(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            if (ue != null) {
                ue.setActive(true);
                em.merge(ue);
            } else {
                throw new DataAccessException("User not found.");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<UserDTO> findByOrg(int orgId) throws DataAccessException {
        try {
            OrganizationEntity oe = em.find(OrganizationEntity.class, orgId);
            List<UserEntity> ueLst = em.createNamedQuery("User.findByOrg")
                    .setParameter("org", oe).getResultList();

            List<UserDTO> retLst = new ArrayList<>();
            ueLst.forEach(Errors.rethrow().wrap(ue -> {
                UserDTO dto = this.mapEntityToDTO(ue);
                retLst.add(dto);
            }));

            return retLst;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDTO findByEmail(String emailAddress) throws DataAccessException {
        try {
            UserEntity ue = em.createNamedQuery("User.findByEmail", UserEntity.class)
                    .setParameter("emailAddress", emailAddress).getSingleResult();
            UserDTO dto = this.mapEntityToDTO(ue);
            return dto;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findAll", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDTO create(UserDTO dto) throws DataAccessException {
        try {
            UserEntity ue = this.addUser(dto.getId(), dto.getPassword(), dto.getCreatedBy(), dto.getRole(), dto.isActive(),
                    dto.getCreateDt(), dto.getOrganizationId(), dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.isResetPwd());

            return this.mapEntityToDTO(ue);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "create", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    /**
     * This method will only update users active status, create date, role and program start date.
     *
     * @param dto
     * @return
     * @throws DataAccessException
     */
    @Override
    public UserDTO update(UserDTO dto, String key) throws DataAccessException {
        try {
            // Make sure use is valid
            UserEntity ue = em.find(UserEntity.class, key);

            OrganizationEntity oe = em.find(OrganizationEntity.class, dto.getOrganizationId());

            if (ue != null) {
                ue.setActive(dto.isActive());
                ue.setRole(dto.getRole());
                ue.setResetPwd(dto.isResetPwd());
                ue.setFirstName(dto.getFirstName());
                ue.setLastName(dto.getLastName());
                ue.setOrganizationById(oe);
                ue.setModifiedBy(dto.getModifiedBy());
                ue.setModifiedDt(new Date());
                ue.setEmail(dto.getEmail());
                if (dto.getQuestionOne() != null && dto.getQuestionOne() != null && dto.getAnswerTwo() != null &&
                        dto.getAnswerTwo() != null) {
                    ue.setQuestionOne(dto.getQuestionOne());
                    ue.setQuestionTwo(dto.getQuestionTwo());
                    ue.setAnswerOne(dto.getAnswerOne());
                    ue.setAnswerTwo(dto.getAnswerTwo());
                }
                ue.setUserResetPwdChallenge(dto.getUserResetPwdChallenge());
                ue = em.merge(ue);
                return this.mapEntityToDTO(ue);
            } else {
                throw new DataAccessException("User not found.");
            }

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "update", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void setActiveStatus(String userId, boolean status) throws DataAccessException {
        try {
            // Make sure use is valid
            UserEntity ue = em.find(UserEntity.class, userId);

            // Change the status
            if (ue != null) {
                ue.setActive(status);
                ue = em.merge(ue);
            } else {
                throw new DataAccessException("User not found.");
            }

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "setActiveStatus", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void changeUserRole(String userId, SecurityRoleEnum role) throws DataAccessException {
        try {
            // Make sure use is valid
            UserEntity ue = em.find(UserEntity.class, userId);

            // Change the status
            if (ue != null) {
                ue.setRole(role.getName());
                em.merge(ue);
            } else {
                throw new DataAccessException("User not found.");
            }

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "setActiveStatus", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    protected UserDTO mapEntityToDTO(UserEntity entity) throws Exception {
        UserDTO dto = new UserDTO();
        dto.setActive(entity.isActive());
        dto.setAdmin(entity.isAdmin());
        dto.setId(entity.getId());
        dto.setOrganizationId(entity.getOrganizationById().getId());
        dto.setOrganizationName(entity.getOrganizationById().getName());
        dto.setRole(entity.getRole());
        dto.setResetPwd(entity.isResetPwd());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setQuestionOne(entity.getQuestionOne());
        dto.setQuestionTwo(entity.getQuestionTwo());
        dto.setAnswerOne(entity.getAnswerOne());
        dto.setAnswerTwo(entity.getAnswerTwo());
        dto.setUserResetPwdChallenge(entity.getUserResetPwdChallenge());
        return dto;
    }

    @Override
    public Class<UserEntity> getGenericEntityClass() throws Exception {
        return UserEntity.class;
    }

    @Override
    public BooleanValueDTO isActive(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);

            if (ue != null) {
                return new BooleanValueDTO(ue.isActive());
            } else {
                return new BooleanValueDTO(false);
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "isActive", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean isGeneratedPwd(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);

            if (ue != null && ue.isGeneratedPwd()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "isGeneratedPwd", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    public boolean isExpired(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);

            Date now = new Date();
            long nowTime = now.getTime();
            long generatedTime = ue.getGeneratedPwdDt().getTime();
            long sum = nowTime - generatedTime;
            boolean expired = (sum > 86400000);

            if (ue != null && expired) return true;
            else return false;

        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "isExpired", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }
    /**
     * Not used in this implementation
     *
     * @param et
     * @param dto
     * @return
     */
    @Override
    protected UserEntity mapDtoToEntity(UserEntity et, UserDTO dto) { return null; }

    @Override
    public BooleanValueDTO setResetPwd(String userId) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            ue.setResetPwd(true);
            em.merge(ue);
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "setResetPwd", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public void resetPasswordToggle(String userId, boolean b) throws DataAccessException {
        try {
            UserEntity ue = em.find(UserEntity.class, userId);
            ue.setResetPwd(b);
            em.merge(ue);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "resetPasswordToggle", e.getMessage(), e);
            throw new DataAccessException(e);
        }
    }
}