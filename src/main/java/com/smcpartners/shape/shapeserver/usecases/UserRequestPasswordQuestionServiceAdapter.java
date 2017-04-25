package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.UserRequestPasswordQuestionService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.UserPasswordQuestionResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.MathUtils;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. User can request a password change for himself and needs to answer a question
 * previously answered.The question returned will be save in the users record for
 * checking purposes by the UserRequestPasswordchangeService.<br/>
 * <p>
 * Created by johndestefano on 4/5/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class UserRequestPasswordQuestionServiceAdapter implements UserRequestPasswordQuestionService {
    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private MathUtils mathUtils;

    @Context
    private UserExtras userExtras;

    /**
     * Constructor
     */
    public UserRequestPasswordQuestionServiceAdapter() {
    }

    //TODO: Is this safe? Anyone could change anyone else's password?
    @Override
    @GET
    @Path("/password_question/{userId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public UserPasswordQuestionResponseDTO requestPasswordQuestion(@PathParam("userId") String userId) throws UseCaseException {
        try {
            // The user requesting the change must be the same user as the change is for
            if (userId.equalsIgnoreCase(userExtras.getUserId())) {
                UserPasswordQuestionResponseDTO retDTO = new UserPasswordQuestionResponseDTO();
                retDTO.setUserId(userId);

                // Look up user and select random question
                UserDTO userDTO = userDAO.findById(userId);

                // Generate a random number between 1 and 2
                int choice = mathUtils.getRandomNumberInRange(1,2);
                if (choice == 1) {
                    retDTO.setPasswordQuestion(userDTO.getQuestionOne());
                } else {
                    retDTO.setPasswordQuestion(userDTO.getQuestionTwo());
                }

                // Make sure to persist the choice
                userDAO.setUserResetPwdChallenge(userId, choice);

                // Return the data
                return retDTO;
            } else {
                throw new NotAuthorizedToPerformActionException();
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "requestPasswordQuestion", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
