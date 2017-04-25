package com.smcpartners.shape.shapeserver.gateway.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by johndestefano on 4/9/17.
 */
@Path("/test")
public interface TestService {

    @GET
    @Path("/testmethod")
    String test();
}
