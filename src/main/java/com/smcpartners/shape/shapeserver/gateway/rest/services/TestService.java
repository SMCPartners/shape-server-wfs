package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by johndestefano on 4/9/17.
 */
@Path("/test")
public interface TestService {

    @GET
    @Path("/testmethod")
    String test();

    /**
     * Find a user
     *
     * @return
     * @throws UseCaseException
     */
    @GET
    @NoCache
    @Path("/find/measure_by_id/{measureId}")
    @Produces("application/json")
    String findMeasureById(@PathParam("measureId") int measureId) throws UseCaseException;
}
