package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.email.SendMailService;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Forgot_Username_Service;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.MailDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.NameStringValDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.RandomPasswordGenerator;
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
 * Responsible:</br>
 * 1. Retrieve the users email address. Look up the user from it and check the account status.</br>
 * 2. Send the user an email with their userid. </br>
 * <p>
 * Created by johndestefano on 6/23/17.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public class Forgot_Username_ServiceAdapter implements Forgot_Username_Service {
    @Inject
    private Logger log;

    @EJB
    UserDAO userDAO;

    @EJB
    SendMailService sendEmailMsg;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.noActiveUserAccountError")
    String noActiveUserAccountError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.noUserWithEmailExistsError")
    String noUserWithEmailExistsError;


    /**
     * Constructor
     */
    public Forgot_Username_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/forgotusername")
    @Produces("application/json")
    @Consumes("application/json")
    public BooleanValueDTO forgotUsername(NameStringValDTO emailAddress) throws UseCaseException {
        try {
            // Find the account by email address. The email address is unique
            UserDTO user;
            try {
                user = userDAO.findByEmail(emailAddress.getVal());
            } catch (Exception ne) {
                throw new Exception(noUserWithEmailExistsError);
            }

            // Check the account status
            // Send an email to the user with their username
            if (user.isActive()) {
                MailDTO mail = new MailDTO();
                mail.setToEmail(user.getEmail());
                mail.setSubject("User name");
                mail.setMessage("Your user name is " + user.getId() + ".");
                sendEmailMsg.sendEmailMsg(mail);
            } else {
                throw new Exception(noActiveUserAccountError);
            }

            // Return
            return BooleanValueDTO.get(true);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "forgotUsername", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
