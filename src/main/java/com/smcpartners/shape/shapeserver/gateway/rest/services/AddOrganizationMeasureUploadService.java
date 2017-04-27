package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.response.FileUploadResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
public interface AddOrganizationMeasureUploadService {

    @POST
    @Path("/measure/add/upload")
    @Produces("application/json")
    @Consumes("multipart/form-data")
    FileUploadResponseDTO addMeasureUpload(MultipartFormDataInput input) throws UseCaseException;
}
