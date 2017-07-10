package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security;

import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.BearerTokenSecurityHandlerImpl;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.DoubleCookieSecurityHandlerImpl;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.SecurityHandler;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers.SecurityHandlerQualifier;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Responsibility: </br>
 * 1. Providing the correct security handler based on the useCookies parameter in the config file</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 */
@ApplicationScoped
public class SecurityHandlerProducer {

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.useCookies")
    private boolean useCookies;

    @Produces
    @SecurityHandlerQualifier
    public SecurityHandler getSecurityHandler(@New BearerTokenSecurityHandlerImpl bearerTokenSecurityHandler,
                                              @New DoubleCookieSecurityHandlerImpl doubleCookieSecurityHandler) {
        if (useCookies) {
            return doubleCookieSecurityHandler;
        } else {
            return bearerTokenSecurityHandler;
        }
    }

}
