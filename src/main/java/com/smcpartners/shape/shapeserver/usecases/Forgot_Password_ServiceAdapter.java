package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.email.SendMailService;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Forgot_Password_Service;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.ForgotPasswordRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.ForgotPasswordResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Take the user id and the email associated with the id. If they are
 * valid then return the user id and one of the user challenge questions. The
 * user must be active. </br>
 *
 * <p>
 * Created by johndestefano on 6/20/17.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Forgot_Password_ServiceAdapter implements Forgot_Password_Service {
    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    /**
     * Constructor
     */
    public Forgot_Password_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/forgotpassword")
    @Produces("application/json")
    @Consumes("application/json")
    public ForgotPasswordResponseDTO forgotUserPassword(ForgotPasswordRequestDTO forgotPwdRequset) throws UseCaseException {
        try {
            String userId = forgotPwdRequset.getUserId();
            String email = forgotPwdRequset.getUserEmail();
            ForgotPasswordResponseDTO forgotPasswordResponseDTO = new ForgotPasswordResponseDTO();

            // Get the user account
            // If found then return one of the users questions as long as the account is active
            UserDTO userDTO = userDAO.findById(userId);
            if (userDTO != null && userDTO.getEmail().equalsIgnoreCase(email) && userDTO.isActive()) {
                forgotPasswordResponseDTO.setUserId(userId);
                int qRand = (new Random()).nextInt(2) + 1;
                String userQuestion = null;
                if (qRand == 1) {
                    forgotPasswordResponseDTO.setRandomQuestion(userDTO.getQuestionOne());
                } else {
                    forgotPasswordResponseDTO.setRandomQuestion(userDTO.getQuestionTwo());
                }

                return forgotPasswordResponseDTO;
            } else {
                throw new Exception("Can't find the user account.");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "forgotUserPassword", e.getMessage(), e);

            // Account for not finding the user
            // TODO: This is brittle, do we need a specific exception from the data layer?
            if (e instanceof NullPointerException) {
                throw new UseCaseException("There is no user with that ID.");
            } else {
                throw new UseCaseException(e.getMessage());
            }

        }
    }
}
