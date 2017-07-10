package com.smcpartners.shape.shapeserver.frameworks.data.dao.shape;

import com.smcpartners.shape.shapeserver.frameworks.data.dao.CrudDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;

import java.util.List;

/**
 * Responsible:<br/>
 * 1. User data dao<br/>
 * <p>
 * Created by johndestefano on 9/10/15.
 * <p>
 * Changes:<b/>
 */
public interface UserDAO extends CrudDAO<UserDTO, String> {

    /**
     * Check to see if the id has already been used
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */
    BooleanValueDTO checkUserId(String userId) throws DataAccessException;

    /**
     * Validate the users ID and password. Returning null indicates an invalid user.
     *
     * @param userId
     * @param password
     * @return
     * @throws DataAccessException
     */
    UserDTO validateUser(String userId, String password) throws DataAccessException;

    /**
     * Change the password for a user
     *
     * @param userId
     * @param oldpassword
     * @param newpassword
     * @return
     * @throws DataAccessException
     */
    UserDTO changePassword(String userId, String oldpassword, String newpassword) throws DataAccessException;

    /**
     * Changes the users password to the given new password
     *
     * @param userId
     * @param newPassword
     * @throws DataAccessException
     */
    void forcePasswordChange(String userId, String newPassword) throws DataAccessException;

    /**
     * Find all users
     *
     * @return
     * @throws DataAccessException
     */
    List<UserDTO> findAll() throws DataAccessException;

    /**
     * Inactivate the user
     *
     * @throws DataAccessException
     */
    void inactivateUser(String userId) throws DataAccessException;

    /**
     * Activate a user
     *
     * @throws DataAccessException
     */
    void activateUser(String userId) throws DataAccessException;

    /**
     * Sets the active status of the user
     *
     * @param userId
     * @param status
     * @throws DataAccessException
     */
    void setActiveStatus(String userId, boolean status) throws DataAccessException;

    /**
     * Change the user's role
     *
     * @param userId
     * @param role
     * @throws DataAccessException
     */
    void changeUserRole(String userId, SecurityRoleEnum role) throws DataAccessException;

    /**
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */
    BooleanValueDTO isActive(String userId) throws DataAccessException;

    /**
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */

    boolean isGeneratedPwd(String userId) throws DataAccessException;

    /**
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */

    boolean isExpired(String userId) throws DataAccessException;

    /**
     * Sets the rest password flag on the user account
     *
     *
     * @param userId
     * @return
     * @throws DataAccessException
     */
    BooleanValueDTO setResetPwd(String userId) throws DataAccessException;

    /**
     * Find users by org id
     *
     * @param orgId
     * @return
     * @throws DataAccessException
     */
    List<UserDTO> findByOrg(int orgId) throws DataAccessException;

    /**
     * Find users by email
     *
     * @param emailAddress
     * @return
     * @throws DataAccessException
     */
    UserDTO findByEmail(String emailAddress) throws DataAccessException;

    /**
     *
     * @param userId
     * @param b
     * @throws DataAccessException
     */

    void resetPasswordToggle(String userId, boolean b) throws DataAccessException;

    /**
     *
     * @param userId
     * @param question1
     * @param question2
     * @param answer1
     * @param answer2
     * @throws DataAccessException
     */

    void addUserSecurityQuestions(String userId, String question1, String question2,
                                  String answer1, String answer2) throws DataAccessException;

    /**
     * Choice will always be 1 or 2
     *
     * @param choice
     * @throws DataAccessException
     */
    void setUserResetPwdChallenge(String userId, int choice) throws DataAccessException;
}
