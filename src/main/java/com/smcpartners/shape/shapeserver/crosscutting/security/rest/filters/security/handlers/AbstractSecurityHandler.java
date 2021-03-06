package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.exceptions.DataAccessException;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UserNeedsPwdResetException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UserNotActiveException;
import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Map;

/**
 * Responsibility: Provide base functionality for the multiple security handling approaches
 * such as Bearer Token security and Double Cookie security.</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 * <p>
 * Changes:</br>
 * 1. Added check for reset password flag - johndesetfano - 07/20/2017 </br>
 * </p>
 */
public abstract class AbstractSecurityHandler {

    @Inject
    JWTUtils jwtUtils;

    @Inject
    UserExtras userExtras;

    @Inject
    UserDAO userDAO;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.authHeaderError")
    String authHeaderError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.inactiveUserError")
    String inactiveUserError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNotFoundError")
    String userNotFoundError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNotAuthorizedError")
    String userNotAuthorizedError;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.userNeedsPwdResetError")
    String userNeedsPwdResetError;

    /**
     * Default constructor
     */
    public AbstractSecurityHandler() {
    }

    /**
     * Gets the authorization header. Throws an exception if its not there.
     *
     * @param requestContext
     * @return
     * @throws NotAuthorizedException
     */
    protected String getAuthHeader(ContainerRequestContext requestContext) throws NotAuthorizedException {
        String authHearder = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHearder == null || authHearder.length() == 0) {
            throw new NotAuthorizedException(
                    authHeaderError,
                    Response.status(Response.Status.UNAUTHORIZED));
        }
        return authHearder;
    }

    /**
     * Extracts the auth token from the authorization header value
     *
     * @param authorizationHeader
     * @return
     * @throws NotAuthorizedException
     */
    protected String getAuthToken(String authorizationHeader) throws NotAuthorizedException {
        String[] authData = authorizationHeader.trim().split("\\s+");
        if (authData != null && authData.length > 1) {
            return authData[1].trim();
        } else {
            throw new NotAuthorizedException(
                    authHeaderError,
                    Response.status(Response.Status.UNAUTHORIZED));
        }
    }

    /**
     * Gets the assertions from the JWT Token
     *
     * @param token
     * @return
     * @throws Exception
     */
    protected Map<String, String> getTokenValues(String token) throws Exception {
        return jwtUtils.getValues(token);
    }

    /**
     * Looks up the user record in the database and makes sure the user is
     * active and doesn't need a password reset.
     *
     * @param tokenValues
     * @return
     * @throws NotAuthorizedException
     * @throws UserNotActiveException
     * @throws DataAccessException
     */
    protected UserDTO findUserFromToken(Map<String, String> tokenValues)
            throws NotAuthorizedException, UserNotActiveException, DataAccessException {
        // Get the user id in the token
        String userId = tokenValues.get("userId");

        // Check user still active and password reset flag is false
        UserDTO userDTO = userDAO.findById(userId);
        if (userDTO == null) {
            throw new NotAuthorizedException(
                    userNotFoundError,
                    Response.Status.UNAUTHORIZED);
        } else if (!userDTO.isActive()) {
            throw new UserNotActiveException(
                    inactiveUserError,
                    Response.Status.UNAUTHORIZED);
        } else if (userDTO.isResetPwd()) {
            throw new UserNeedsPwdResetException(
                    userNeedsPwdResetError,
                    Response.Status.UNAUTHORIZED);
        }

        // Return the DTO
        return userDTO;
    }

    /**
     * Loads the request scoped userExtras
     *
     * @param userExtras
     * @param tokenValues
     */
    protected void populateUserExtras(UserExtras userExtras, Map<String, String> tokenValues) {
        userExtras.setOrgId(Integer.parseInt(tokenValues.get(JWTUtils.ORGID)));
        userExtras.setRole(SecurityRoleEnum.valueOf(tokenValues.get(JWTUtils.ROLE)));
        userExtras.setUserId(tokenValues.get(JWTUtils.USERID));
    }

    /**
     * Check the user's role against the allowed roles for the
     * target method or class. If they don't match throw and exception
     *
     * @param role
     * @throws NotAuthorizedException
     */
    protected void checkRoleAgainstMehtodRoles(String role, ResourceInfo resourceInfo) throws NotAuthorizedException {
        // Get the allowed roles from the target method or class
        // Method first then class
        Secure secure = resourceInfo.getResourceMethod().getAnnotation(Secure.class);
        if (secure == null) {
            secure = resourceInfo.getResourceClass().getAnnotation(Secure.class);
        }

        // Match against user's role and only role
        if (!(Arrays.asList(secure.value()).contains(SecurityRoleEnum.valueOf(role)))) {
            throw new NotAuthorizedException(
                    userNotAuthorizedError,
                    Response.status(Response.Status.UNAUTHORIZED));
        }
    }
}
