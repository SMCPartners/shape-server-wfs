package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Request_Password_Change_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.UserPasswordResetRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.PasswordResetException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.MathUtils;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. A user can request a passwprd reset. The request must be for the logged in user. The
 * user must send the old password which must match the current password, the new password, and
 * the answer to the provided question (provided by previous call to Request_Password_Question_Service.<br/>
 * <p>
 * Created by johndestefano on 4/5/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Request_Passwordchange_ServiceAdapter implements Request_Password_Change_Service {
    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private MathUtils mathUtils;

    @Inject
    private UserExtras userExtras;

    /**
     * Constructor
     */
    public Request_Passwordchange_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/password_change")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public BooleanValueDTO requestPasswordChange(UserPasswordResetRequestDTO pwdResetReq) throws UseCaseException {
        try {
            // The user requesting the change must be the same user as the change is for
            String userId = userExtras.getUserId();
            if (pwdResetReq.getUserId().equalsIgnoreCase(userId)) {
                // Get the user and check password
                UserDTO userDTO = userDAO.validateUser(userId, pwdResetReq.getOldPassword());
                if (userDTO == null) {
                    throw new PasswordResetException("Incorrect old password");
                }

                // Compare the answer returned to the question sent
                String answer = null;
                if (userDTO.getUserResetPwdChallenge() == 1) {
                    answer = userDTO.getAnswerOne();
                } else if (userDTO.getUserResetPwdChallenge() == 2) {
                    answer = userDTO.getAnswerTwo();
                } else {
                    // Can't send back anything else
                    throw new PasswordResetException("Answers don't match.");
                }

                // If answer doesn't match throw exception
                if (!pwdResetReq.getQuestionAnswer().equals(answer)) {
                    throw new PasswordResetException("Answer doesn't match question.");
                }

                // Set the new password for the user
                // This will also set the user request password change flag to 0
                userDAO.forcePasswordChange(userId, pwdResetReq.getNewPassword());

                // Return value
                return BooleanValueDTO.get(true);
            } else {
                throw new IllegalAccessException("Your account is not allowed to reset this user's password");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "requestPasswordChange", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (PasswordResetException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
