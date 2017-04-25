package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Responsible:<br/>
 * 1. Support REST framework
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/common")
public interface FindAllOrganizationMeasuresByOrganizationService {

    @GET
    @NoCache
    @Path("/organization_measure/findAllByOrg/{orgId}")
    @Produces("application/json")
    List<OrganizationMeasureDTO> findAllOrganizationMeasuresByOrg(@PathParam("orgId") int orgId) throws UseCaseException;
}
