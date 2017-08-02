package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers;

import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Cookie;
import java.util.Map;

/**
 * Responsibility: Provide security using JWT tokens and cookies</br>
 * 1. WORK IN PROGRESS, NOT READY FOR IMPLEMENTATION</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 */
public class DoubleCookieSecurityHandlerImpl extends AbstractSecurityHandler implements SecurityHandler {

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.sessionCookieName")
    private String cookieName;

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.xXsrfTokenJwtAssertionName")
    private String xXsrfTokenJwtAssertionName;

    public DoubleCookieSecurityHandlerImpl() {
    }

    @Override
    public void processRequest(ContainerRequestContext requestContext, ResourceInfo resourceInfo) throws Exception {
        // Get the token
        String token = getTokenFromCookie(requestContext);

        // Parse the token to get access to its values
        Map<String, String> tokenValues = this.getTokenValues(token);

        // Validate that the header X token is the same as what's in the JWT token
        validateXToken(tokenValues, requestContext);

        // This validates the user in the database and makes sure they are active
        findUserFromToken(tokenValues);

        // Check the user's roles against the allowed roles of the target method or class
        checkRoleAgainstMehtodRoles(tokenValues.get(JWTUtils.ROLE), resourceInfo);

        // Populate the request scoped object which will be used downstream
        // by the target class
        populateUserExtras(userExtras, tokenValues);
    }

    /**
     * Get the JWT token stored in the cookie
     *
     * @param requestContext
     * @return
     * @throws NotAuthorizedToPerformActionException
     */
    protected String getTokenFromCookie(ContainerRequestContext requestContext) throws NotAuthorizedToPerformActionException {
        Cookie securityCookie = requestContext.getCookies().get(cookieName);
        if (securityCookie == null) {
            throw new NotAuthorizedToPerformActionException();
        } else {
            return securityCookie.getValue();
        }
    }

    /**
     * When using the double cookie security method the
     * token in the JWT token must match the request header token
     *
     * @param tokenValues
     * @param requestContext
     * @throws NotAuthorizedToPerformActionException
     */
    protected void validateXToken(Map<String, String> tokenValues, ContainerRequestContext requestContext) throws NotAuthorizedToPerformActionException{
        String xxToken = tokenValues.get(JWTUtils.X_XSRF_TOKEN);
        String headerXxToken = requestContext.getHeaderString(xXsrfTokenJwtAssertionName);
        if (xxToken == null
                || xxToken.length() == 0
                || headerXxToken == null
                || headerXxToken.length() == 0
                || !xxToken.equals(headerXxToken)) {
            throw new NotAuthorizedToPerformActionException();
        }
    }
}
