package com.smcpartners.shape.shapeserver.gateway.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Respondsible:</br>
 * 1. A class extending {@link Application} and annotated with @ApplicationPath is the Java EE 6
 * "no XML" approach to activating JAX-RS.
 * <p>
 * Changes:
 * 1.</br>
 * </p>
 */
@ApplicationPath("/shape")
public class JaxRsActivator extends Application {
    public JaxRsActivator() {
    }
}
