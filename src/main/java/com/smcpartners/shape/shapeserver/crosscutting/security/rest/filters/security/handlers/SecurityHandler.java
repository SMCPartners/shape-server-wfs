package com.smcpartners.shape.shapeserver.crosscutting.security.rest.filters.security.handlers;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;

/**
 * Responsibility: Class that handle security policies need to implement</br>
 *
 */
public interface SecurityHandler {
    void processRequest(ContainerRequestContext requestContext, ResourceInfo resourceInfo) throws Exception;
}
