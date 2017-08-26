package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureFileUploadDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Organization_Measure_Download_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.OrganizationMeasureFileUploadRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.util.Base64;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/24/17
 */
@Path("/admin")
public class Organization_Measure_Download_ServiceAdapter implements Organization_Measure_Download_Service {

    @EJB
    OrganizationMeasureDAO organizationMeasureDAO;

    @EJB
    OrganizationMeasureFileUploadDAO organizationMeasureFileUploadDAO;

    @Inject
    UserExtras userExtras;

    /**
     * Default constructor
     */
    public Organization_Measure_Download_ServiceAdapter() {
    }

    @Override
    @GET
    @Path("/download/organization_measure/{orgMeasureId}")
    //@Produces("application/vnd.ms-excel")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public Response downloadOrgMeasureFile(@PathParam("orgMeasureId") int orgMeasureId) throws UseCaseException {
        try {
            // File the organization for this orgMeasureId
            OrganizationMeasureDTO orgMeasDTO = organizationMeasureDAO.findById(orgMeasureId);

            // If this is an org admin they can only download files for their organization
            if (userExtras.getRole() == SecurityRoleEnum.ORG_ADMIN &&
                    orgMeasDTO.getId() != orgMeasureId) {
                throw new NotAuthorizedToPerformActionException();
            }

            // Get the file upload data and create a new file form the contents
            OrganizationMeasureFileUploadRequestDTO dto = organizationMeasureFileUploadDAO
                    .getOrgMeasureFileUploadForOrgMeasureIdAndUploadDt(orgMeasureId, orgMeasDTO.getFileUploadDate());

            // uploaded file data
            byte[] fileBytes = Base64.decode(dto.getUploadedB64File());

            // Construct response
            Response response;
            Response.ResponseBuilder builder = Response.ok(fileBytes);
            builder.header("Content-Disposition", "attachment; filename=" + dto.getFileName());
            response = builder.build();
            return response;

        } catch (Exception e) {
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
