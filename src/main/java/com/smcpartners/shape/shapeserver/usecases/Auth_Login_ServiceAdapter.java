package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Auth_Login_Service;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.LoginRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Framework support for use case</br
 * <p>
 * Created by johndestefano on 9/14/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
@Path("/common")
public class Auth_Login_ServiceAdapter implements Auth_Login_Service {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @Inject
    private JWTUtils jwtUtils;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNotAuthorizedError")
    private String userNotAuthorizedError;

    /**
     * Default Constructor
     */
    public Auth_Login_ServiceAdapter(){
    }

    @Override
    @POST
    @Path("/login")
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
                        throw new Exception ("Password has expired, please use Forgot Password to generate a new one");
                    }
                    String var = ue.isResetPwd() ? "true" : "false";
                    String token = jwtUtils.generateToken(ue.getId().toUpperCase(), ue.getRole(), ue.getOrganizationId(), true);
                    return Response.status(Response.Status.OK).entity("{\"token\":\"" + token + "\", \"resetRequired\":"
                            + var + "}").header("Authorization", "Bearer " + token).build();
                }else{
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Inactive").build();
                }
            } else {
                throw new NotAuthorizedException(
                        userNotAuthorizedError,
                        Response.status(Response.Status.UNAUTHORIZED));
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "login", e.getMessage(), e);
            if (e instanceof NotAuthorizedException) {
                throw (NotAuthorizedException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
