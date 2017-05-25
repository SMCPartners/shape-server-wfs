package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDetailDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/common")
public interface Find_All_Organization_Measures_Detail_By_Organization_Service {

    @GET
    @NoCache
    @Path("/organization_measure_detail/findAllByOrg/{orgId}")
    @Produces("application/json")
    List<OrganizationMeasureDetailDTO> findAllOrganizationMeasuresDetailByOrg(@PathParam("orgId") int orgId) throws UseCaseException;
}
