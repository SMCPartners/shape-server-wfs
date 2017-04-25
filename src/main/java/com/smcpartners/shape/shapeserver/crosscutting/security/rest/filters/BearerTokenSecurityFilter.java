package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UserNotActiveException;
import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Responsibility: </br>
 * 1. Applied when the matched method or method class has the RolesAllowed annotation (see the JWTSecurityContextFeature)</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/19/17
 */
@Secure
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BearerTokenSecurityFilter implements ContainerRequestFilter {

    private static Pattern tokenPattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    @Inject
    private Logger log;

    @Inject
    private JWTUtils jwtUtils;

    @Inject
    private UserExtras userExtras;

    @Inject
    private UserDAO userDAO;

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.authHeaderError")
    private String authHeaderError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.inactiveUserError")
    private String inactiveUserError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNotFoundError")
    private String userNotFoundError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNotAuthorizedError")
    private String userNotAuthorizedError;

    /**
     * Default constructor
     */
    public BearerTokenSecurityFilter() {
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            String token = null;
            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null) {
                if (authorizationHeader != null) {
                    String[] authData = authorizationHeader.trim().split("\\s+");
                    if (authData != null && authData.length > 1) {
                        token = authData[1].trim();
                    } else {
                        throw new NotAuthorizedException(
                                authHeaderError,
                                Response.status(Response.Status.UNAUTHORIZED));
                    }
                } else {
                    throw new NotAuthorizedException(
                            authHeaderError,
                            Response.status(Response.Status.UNAUTHORIZED));
                }

                // Get the requester's data
                Map<String, String> tokenValues = jwtUtils.getValues(token);
                String userId = tokenValues.get("userId");

                // Check user still active
                UserDTO userDTO = userDAO.findById(userId);
                if (userDTO == null) {
                    throw new NotAuthorizedException(
                            inactiveUserError,
                            Response.Status.UNAUTHORIZED);
                } else if (userDTO.isActive() == false) {
                    throw new UserNotActiveException(
                            inactiveUserError,
                            Response.Status.UNAUTHORIZED);
                }

                // Finish getting attributes
                String role = tokenValues.get("role");
                int orgId = Integer.parseInt(tokenValues.get("orgId"));

                // Check roles, method roles override class roles if both present
                // Method first then class
                // Has to be on method or class for this to be called
                Secure secure = resourceInfo.getResourceMethod().getAnnotation(Secure.class);
                if (secure == null) {
                    secure = resourceInfo.getResourceClass().getAnnotation(Secure.class);
                }

                // Match against users one and only role
                if (!(Arrays.asList(secure.value()).contains(SecurityRoleEnum.valueOf(role)))) {
                    throw new NotAuthorizedException(
                            userNotAuthorizedError,
                            Response.status(Response.Status.UNAUTHORIZED));
                }

                // Create UserExtras
                UserExtras userExtras = new UserExtras();
                userExtras.setOrgId(orgId);
                userExtras.setRole(SecurityRoleEnum.valueOf(role));
                userExtras.setUserId(userId);

                // push to context
                ResteasyProviderFactory.pushContext(UserExtras.class, userExtras);

            } else {
                throw new NotAuthorizedException(
                        authHeaderError,
                        Response.status(Response.Status.UNAUTHORIZED));
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "filter", e.getMessage(), e);
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}

