package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
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
 * Responsible: Return a challenge question so the user can reset their ID </br>
 * 1. Take the user id and the email associated with the id. If they are
 * valid then return the user id and one of the user challenge questions. The
 * user must be active. </br>
 * <p>
 * <p>
 * Created by johndestefano on 6/20/17.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
//TODO: Should change to GET?
@Path("/common")
public class Forgot_Password_ServiceAdapter implements Forgot_Password_Service {

    @EJB
    UserDAO userDAO;

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
                throw new Exception("Either the email or username is incorrect, please check both and try again.");
            }
        } catch (Exception e) {
            // Account for not finding the user
            // TODO: This is brittle, do we need a specific exception from the data layer?
            if (e instanceof DataAccessException) {
                throw new UseCaseException("There is no user with that ID.");
            } else {
                throw new UseCaseException(e.getMessage());
            }

        }
    }
}
