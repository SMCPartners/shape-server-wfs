package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Change_Password_Service;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.CreateUserRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.SecurityUtils;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible: Update the User's password</br>
 * 1. The user must provide the old password and a new password.</br>
 * 2. The new password can't be the same as the old password.</br>
 * 3. Must be a valid user.</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Change_Password_ServiceAdapter implements Change_Password_Service {
    @Inject
    private Logger log;

    @EJB
    UserDAO dao;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.changePasswordError")
    String changePasswordError;

    @Inject
    UserExtras userExtras;

    /**
     * Constructor
     */
    public Change_Password_ServiceAdapter() {
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

            // Check valid user, valid new password, new and old are not the same
            if (dao.validateUser(userId, userPwd) != null &&
                    SecurityUtils.checkPasswordCompliance(newPwd) &&
                    dao.validateUser(userId, newPwd) == null) {
                dao.changePassword(userId, userPwd, newPwd);
                dao.resetPasswordToggle(userId, false);
                if (user.getQuestionOne() != null && user.getQuestionTwo() != null) {
                    dao.addUserSecurityQuestions(userId, user.getQuestionOne(),
                            user.getQuestionTwo(), user.getAnswerOne(), user.getAnswerTwo());
                }
            } else {
                throw new UseCaseException(changePasswordError);
            }

            //returns true if new password uses correct regex and is a valid user
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "changeUserPassword", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
