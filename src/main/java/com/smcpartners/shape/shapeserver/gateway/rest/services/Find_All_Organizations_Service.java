package com.smcpartners.shape.shapeserver.gateway.rest.services;


import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("/admin")
public interface Find_All_Organizations_Service {

    @GET
    @NoCache
    @Path("/organization/findAll")
    @Produces("application/json")
    List<OrganizationDTO> findAllOrganizations() throws UseCaseException;
}
