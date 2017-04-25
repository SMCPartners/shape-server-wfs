
package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.response.ListViewDTO;
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
public interface ShowListViewService {

    @GET
    @NoCache
    @Path("/show/listView/{orgId}/{measureId}/{year}")
    @Produces("application/json")
    List<ListViewDTO> showListView(@PathParam("orgId") int orgId, @PathParam("measureId") int measureId,
                                   @PathParam("year") int year) throws UseCaseException;
}
