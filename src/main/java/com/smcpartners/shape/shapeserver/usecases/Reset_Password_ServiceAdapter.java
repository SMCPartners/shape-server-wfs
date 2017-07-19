package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.email.SendMailService;
import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Reset_Password_Service;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.MailDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.PasswordUpdateRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.PasswordResetException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.RandomPasswordGenerator;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Adapter for framework services. Any user can reset their password
 * <p>
 * Created by jjdestef3 on 12/21/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public class Reset_Password_ServiceAdapter implements Reset_Password_Service {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @EJB
    private SendMailService sms;

    /**
     * Default Password
     */
    public Reset_Password_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/user/reset_password")
    @Produces("application/json")
    @Consumes("application/json")
    @Logged
    public BooleanValueDTO resetPassword(PasswordUpdateRequestDTO userReq) throws UseCaseException {
        try {
            // Request needs the organization id
            String userId = userReq.getUserId();
            String question = userReq.getQuestion();
            String answer = userReq.getAnswer();

            UserDTO user;
            if (!userId.equals("")) {
                user = userDAO.findById(userId);
            } else {
                throw new PasswordResetException("Fields are not filled out or entered incorrectly");
            }

            // Get user data
            // Find matching question and check answer
            if (question.equalsIgnoreCase(user.getQuestionOne())) {
                if (!answer.equalsIgnoreCase(user.getAnswerOne())) {
                    throw new PasswordResetException("Response does not match.");
                }
            }

            if (question.equalsIgnoreCase(user.getQuestionTwo())) {
                if (!answer.equalsIgnoreCase(user.getAnswerTwo())) {
                    throw new PasswordResetException("Response does not match.");
                }
            }

            // Generate a new temporary password
            String newPassword = RandomPasswordGenerator.generateApplicationDefaultPwd();
            resetPassword(userId, newPassword);

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "resetPassword", e.getMessage(), e);
            if (e instanceof PasswordResetException) {
                throw (PasswordResetException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }

    /**
     * Rest the users password
     *
     * @param userId
     * @param newPassword
     * @throws Exception
     */
    public void resetPassword(String userId, String newPassword) throws Exception {
        try {
            UserDTO uDTO = userDAO.findById(userId);
            userDAO.resetPasswordToggle(userId, true);
            MailDTO mail = new MailDTO();
            mail.setToEmail(uDTO.getEmail());
            mail.setSubject("Your password has been reset");
            mail.setMessage("Your password has been reset and changed to the temporary password: " + newPassword + "\n"
                    + "Please log in using your temporary password. You will be prompted to change this " +
                    "password after a successful authentication");
            sms.sendEmailMsg(mail);
            userDAO.forcePasswordChange(userId, newPassword);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "resetPassword", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
