package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.email.SendMailService;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Send_Password_Reset_Service;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.MailDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.ResetPasswordRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.RandomPasswordGenerator;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:</br>
 * 1. Take the user id, challenge question and responds and validate them against the user account.</br>
 * 2. If valid send an email with a temp password to the users email address. </br>
 * 3. Force a password reset on the next authentication. </br>
 * <p>
 * Created by johndestefano on 6/20/17.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Send_Password_Reset_ServiceAdapter implements Send_Password_Reset_Service {

    @EJB
    UserDAO userDAO;

    @EJB
    SendMailService sendEmailMsg;

    /**
     * Constructor
     */
    public Send_Password_Reset_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/sendpasswordreset")
    @Produces("application/json")
    @Consumes("application/json")
    public BooleanValueDTO sendPasswordReset(ResetPasswordRequestDTO resetPasswordRequestDTO) throws UseCaseException {
        try {
            String userId = resetPasswordRequestDTO.getUserId();
            String email = resetPasswordRequestDTO.getEmail();
            UserDTO user = userDAO.findById(userId);

            // Check the userid and the email
            if (user != null &&
                    user.getEmail().equalsIgnoreCase(email)) {

                // Find the questions
                String question = resetPasswordRequestDTO.getChallengeQuestion();
                String answer = resetPasswordRequestDTO.getChallengeQuestionResponse();

                // Check answer to question
                if (question.equalsIgnoreCase(user.getQuestionOne())) {
                    if (!answer.equalsIgnoreCase(user.getAnswerOne())) {
                        throw new Exception("Response to question doesn't match.");
                    }
                } else {
                    if (!answer.equalsIgnoreCase(user.getAnswerTwo())) {
                        throw new Exception("Response to question doesn't match.");
                    }
                }

                // Reset the password and send an email
                String newPassword = RandomPasswordGenerator.generateApplicationDefaultPwd();
                userDAO.resetPasswordToggle(userId, true);
                MailDTO mail = new MailDTO();
                mail.setToEmail(email);
                mail.setSubject("Your password has been reset");
                mail.setMessage("Your password has been reset and changed to the temporary password: " + newPassword + ".\n"
                        + "Please log in using your temporary password. You will be prompted to change this " +
                        "password after a successful authentication.");
                sendEmailMsg.sendEmailMsg(mail);
                userDAO.forcePasswordChange(userId, newPassword);

                // return
                return new BooleanValueDTO(true);
            } else {
                throw new Exception("Can't find the user");
            }
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }
}
