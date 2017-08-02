package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Login_Service;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.LoginRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login.LoginHelper;
import com.smcpartners.shape.shapeserver.usecases.helpers.authentication.LoginHelperQualifier;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Login service support</br>
 * 2. Support for bear token JWT</br>
 * 3. Support for double cookie authentication -
 * https://stackoverflow.com/questions/27067251/where-to-store-jwt-in-browser-how-to-protect-against-csrf </br>
 * <p>
 * Created by johndestefano on 9/14/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1. Added changes for allowing bearer or session cookie authentication - 6/23/2017 - johndestefano</br>
 * </p>
 */
@Path("/common")
public class Login_ServiceAdapter implements Login_Service {

    @Inject
    private Logger log;

    @EJB
    UserDAO userDAO;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNotAuthorizedError")
    String userNotAuthorizedError;

    @Inject
    @LoginHelperQualifier
    LoginHelper loginHelper;

    /**
     * Default Constructor
     */
    public Login_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/authentication")
    @Produces("application/json")
    @Consumes("application/json")
    @Logged
    public Response login(LoginRequestDTO loginRequest) throws UseCaseException {
        try {
            UserDTO ue = userDAO.validateUser(loginRequest.getUserId(), loginRequest.getPassword());

            // If the user is valid and active return a token
            if (ue != null) {
                boolean isGenPwd = userDAO.isGeneratedPwd(ue.getId());
                boolean isExpired = userDAO.isExpired(ue.getId());
                if (ue.isActive()) {
                    if (isGenPwd && isExpired) {
                        throw new Exception("Password has expired, please use Forgot Password to generate a new one");
                    }
                    return loginHelper.loginResponse(ue, false);
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Inactive").build();
                }
            } else {
                throw new NotAuthorizedException(
                        userNotAuthorizedError,
                        Response.status(Response.Status.UNAUTHORIZED));
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "authentication", e.getMessage(), e);
            if (e instanceof NotAuthorizedException) {
                throw (NotAuthorizedException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
