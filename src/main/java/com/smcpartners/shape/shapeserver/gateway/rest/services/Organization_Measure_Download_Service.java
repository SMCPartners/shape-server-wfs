package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/24/17
 */
@Path("/admin")
public interface Organization_Measure_Download_Service {

    @GET
    @Path("/download/organization_measure/{orgMeasureId}")
    @Produces("application/vnd.ms-excel")
    Response downloadOrgMeasureFile(@PathParam("orgMeasureId") int orgMeasureId) throws UseCaseException;
}
