package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers;

import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import java.util.Map;

/**
 * Responsibility: Implement the Bear Token security policy</br>
 * 1. Uses a jwt token found in the HTTP Authorization header to validate the user,
 * check the users roles against the allowed roles for the invocation target (method or
 * type), and populate the UserExtras request scoped bean. This bean is available in the
 * invocation target and carries the user id and role information.</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 */
public class BearerTokenSecurityHandlerImpl extends AbstractSecurityHandler implements SecurityHandler {

    public BearerTokenSecurityHandlerImpl() {
    }

    @Override
    public void processRequest(ContainerRequestContext requestContext, ResourceInfo resourceInfo) throws Exception {
        // Get the authorization header
        String authHeader = getAuthHeader(requestContext);

        // Parse out the token from the header
        String token = getAuthToken(authHeader);

        // Parse the token to get access to its values
        Map<String, String> tokenValues = this.getTokenValues(token);

        // This validates the user in the database and makes sure they are active
        findUserFromToken(tokenValues);

        // Check the user's roles against the allowed roles of the target method or class
        checkRoleAgainstMehtodRoles(tokenValues.get(JWTUtils.ROLE), resourceInfo);

        // Populate the request scoped object which will be used downstream
        // by the target class
        populateUserExtras(userExtras, tokenValues);
    }
}
