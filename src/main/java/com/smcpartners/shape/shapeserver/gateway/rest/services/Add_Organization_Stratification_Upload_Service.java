package com.smcpartners.shape.shapeserver.gateway.rest.services;

import com.smcpartners.shape.shapeserver.shared.dto.shape.response.FileUploadResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.*;

/**
 * Responsible:<br/>
 * 1. Support REST framework
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public interface Add_Organization_Stratification_Upload_Service {

    @POST
    @Path("/stratification/add/upload")
    @Produces("application/json")
    @Consumes("multipart/form-data")
    FileUploadResponseDTO addStratificationUpload(MultipartFormDataInput input, @HeaderParam("content-length") long contentLength) throws UseCaseException;
}
