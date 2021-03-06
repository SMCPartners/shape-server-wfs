package com.smcpartners.shape.shapeserver.gateway.rest.services;


import com.smcpartners.shape.shapeserver.shared.dto.shape.ProviderDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.IntEntityResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:<br/>
 * 1. Support REST framework
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public interface Add_Provider_Service {

    @POST
    @Path("/provider/add")
    @Produces("application/json")
    @Consumes("application/json")
    IntEntityResponseDTO addProvider(ProviderDTO prov) throws UseCaseException;
}
