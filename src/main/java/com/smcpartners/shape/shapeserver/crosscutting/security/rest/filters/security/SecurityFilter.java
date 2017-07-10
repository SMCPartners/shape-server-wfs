package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.AbstractSecurityHandler;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.SecurityHandler;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.SecurityHandlerQualifier;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsibility: </br>
 * 1. Filter to handle security. It delegates the handling to an inject handler.
 * The injected handler is provided based on the useCookies value from the config file.
 * Currently there is a bearer token handler and a Double cookie handler providing
 * two method for security. Only one can be active at a time.</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 06/24/2017
 */
@Secure
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter extends AbstractSecurityHandler implements ContainerRequestFilter {

    @Inject
    private Logger log;

    @Inject
    @SecurityHandlerQualifier
    SecurityHandler securityHandler;

    @Context
    private ResourceInfo resourceInfo;

    /**
     * Default constructor
     */
    public SecurityFilter() {
    }

    /**
     * The filter method delegates security handling to the injected SecurityHandler implementation.
     *
     * @param requestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            securityHandler.processRequest(requestContext);
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "filter", e.getMessage(), e);

            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected ResourceInfo getResourceInfo() {
        return resourceInfo;
    }
}

