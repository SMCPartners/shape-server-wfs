package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.response.OrgsMeasureYearAvgDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Responsible:</br>
 * 1. Support REST framework </br>
 * <p>
 * Created by johndestefano on 6/12/17.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Path("/common")
public interface Find_All_Aggregate_Comparison_Service {

    @GET
    @NoCache
    @Path("/show/aggregateAllComparison/{measureId}")
    @Produces("application/json")
    OrgsMeasureYearAvgDTO showAggregateComparison(@PathParam("measureId") int measureId) throws UseCaseException;
}
