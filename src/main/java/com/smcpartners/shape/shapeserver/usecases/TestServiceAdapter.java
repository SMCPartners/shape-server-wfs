package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.gateway.rest.services.TestService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/9/17
 */
@Path("/test")
public class TestServiceAdapter implements TestService  {

    @Inject
    @ConfigurationValue("com.smc.server-core.mail.MAIL_SERVER_ADDRESS")
    private String mailAdd;

    @Inject
    private UserExtras userExtras;

    public TestServiceAdapter() {
    }

    @Override
    @GET
    @Path("/testmethod")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public String test() {
        return "Hello World: " + userExtras.getUserId();
    }
}
