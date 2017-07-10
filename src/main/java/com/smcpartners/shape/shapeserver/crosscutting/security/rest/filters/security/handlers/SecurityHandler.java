package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers;

import javax.ws.rs.container.ContainerRequestContext;

public interface SecurityHandler {
    void processRequest(ContainerRequestContext requestContext) throws Exception;
}
