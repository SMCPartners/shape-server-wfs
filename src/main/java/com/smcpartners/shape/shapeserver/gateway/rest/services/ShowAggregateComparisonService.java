package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Responsible:</br>
 * 1. Support REST framework </br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public interface ShowAggregateComparisonService {

    @GET
    @NoCache
    @Path("/show/aggregateComparison/{measureId}/{year}")
    @Produces("application/json")
    List<List<Object>> showAggregateComparison(@PathParam("measureId") int measureId,
                                               @PathParam("year") int year) throws UseCaseException;
}


