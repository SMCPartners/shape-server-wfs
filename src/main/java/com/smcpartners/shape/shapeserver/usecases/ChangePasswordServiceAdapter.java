package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.ChangePasswordService;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.CreateUserRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.SecurityUtils;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Request a password change. The user must provide the old password and a new password.</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class ChangePasswordServiceAdapter implements ChangePasswordService {
    @Inject
    private Logger log;

    @EJB
    private UserDAO dao;

    @Inject
    private UserExtras userExtras;

    /**
     * Constructor
     */
    public ChangePasswordServiceAdapter() {
    }

    @Override
    @POST
    @Path("/changepassword")
    @Produces("application/json")
    @Consumes("application/json")
    public BooleanValueDTO changeUserPassword(CreateUserRequestDTO user) throws UseCaseException {
        try {

            String userId = user.getId();
            String userPwd = user.getPassword();
            String newPwd = user.getNewPassword();

            //check new password validity
            boolean validPassword = SecurityUtils.checkPasswordCompliance(newPwd);
            //check if valid user
            if (dao.validateUser(userId, userPwd) != null) {
                //check if valid password with correct regex
                if (validPassword) {
                    dao.changePassword(userId, userPwd, newPwd);
                    dao.resetPasswordToggle(userId, false);
                    if (user.getQuestionOne() != null && user.getQuestionTwo() != null) {
                        dao.addUserSecurityQuestions(userId, user.getQuestionOne(),
                                user.getQuestionTwo(), user.getAnswerOne(), user.getAnswerTwo());
                    }
                }else{
                    throw new UseCaseException("The new password you entered did not meet the required format.");
                }
            }else{
                throw new UseCaseException("Your current password is not correct.");
            }
            //returns true if new password uses correct regex and is a valid user
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "changeUserPassword", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
