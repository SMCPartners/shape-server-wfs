package com.smcpartners.shape.shapeserver.usecases.helpers.authentication.login;

import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.utils.DoubleCookieTokenPair;
import com.smcpartners.shape.shapeserver.shared.utils.JWTUtils;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.inject.Inject;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/24/17
 */
public class DoubleCookieLoginHelper implements LoginHelper {

    @Inject
    JWTUtils jwtUtils;

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.sessionCookieName")
    String sessionCookieName;

    @Inject
    @ConfigurationValue("com.smc.server-core.security.jwtEmbededCookies.xxsrfCookieName")
    String xxsrfCookieName;

    /**
     * Constructor
     */
    public DoubleCookieLoginHelper() {
    }

    @Override
    /**
     * Return a response for the Double Cookie login policy
     *
     */
    public Response loginResponse(UserDTO user, boolean neverExpires) throws Exception {
        // Generate the JWT token and the X-XSRF-TOKEN
        DoubleCookieTokenPair pair = jwtUtils.generateForDoubleCookieToken(user.getId(), user.getRole(), user.getOrganizationId(), neverExpires);

        // Create two Cookies
        // Session cookie holds JWT token, expires when to browser closes, is httpOnly, and can not be read by browser javascript (secure)
        NewCookie sessionCookie = new NewCookie(sessionCookieName, pair.getJwtToken(), null, null, null,-1, true, true);

        // XSRF cookie hold the XSRF token, and expires when the browser closes
        NewCookie xCookie = new NewCookie(xxsrfCookieName, pair.getXToken());

        // Return the response
        return Response.status(Response.Status.OK)
                .entity("{ \"resetRequired\":" + requirePasswordReset(user) + "}")
                .cookie(sessionCookie, xCookie).build();
    }
}
