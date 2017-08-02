package com.smcpartners.shape.shapeserver.shared.producers;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

/**
 * Respondsible: CDI Producer for logger </br>
 * </br>
 * Changes:</br>
 * 1. </br>
 */
public class LogProducer {

    /**
     * Using CDI return a java.util.logging.Logger when Logger is injected.
     * Uses the injection point to discover the injected class to get an
     * appropriately named logger.
     *
     * @param injectionPoint
     * @return
     */
    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
