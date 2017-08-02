package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.logout;

import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.inject.Inject;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * Responsibility: Implements logout when using double cookie security</br>
 * 1. </br>
 * Created By: johndestefano
 * Date: 6/25/17
 */
public class DoubleCookieLogoutHelper implements LogoutHelper {

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.sessionCookieName")
    String sessionCookieName;

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.xxsrfCookieName")
    String xxsrfCookieName;

    /**
     * Default constructor
     */
    public DoubleCookieLogoutHelper() {
    }

    /**
     * Need to invalidate the session and xsrf cookies
     *
     * @return
     * @throws Exception
     */
    @Override
    public Response logoutResponse() throws Exception {
        // Create two Cookies
        // Session cookie holds JWT token, expires when the browser closes, is httpOnly, and can not be read by browser javascript (secure)
        NewCookie sessionCookie = new NewCookie(sessionCookieName, null, null, null, null,0, false, true);

        // XSRF cookie hold the XSRF token, and expires when the browser closes
        NewCookie xCookie = new NewCookie(xxsrfCookieName, null, null, null, null,0, false, false);

        // Return the response
        return Response.status(Response.Status.OK).cookie(sessionCookie, xCookie).build();
    }
}
