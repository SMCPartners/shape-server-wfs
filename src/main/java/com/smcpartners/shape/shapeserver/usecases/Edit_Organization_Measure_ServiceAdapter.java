package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Edit_Organization_Measure_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:<br/>
 * 1. Only ADMIN or ORG_ADMIN can edit organization measures. ORG_ADMIN and REGISTERED user
 * can only edit their organization.</br>
 * 2. Can only have one measure type per organization per year so when editing org, measure, and year must be the same.</br>
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
//TODO: Should a registered user be able ot edit or delete an organization measure
@Path("/common")
public class Edit_Organization_Measure_ServiceAdapter implements Edit_Organization_Measure_Service {

    @EJB
    OrganizationMeasureDAO organizationMeasureDAO;

    @Inject
    UserExtras userExtras;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.orgMeasureEditMustMatchError")
    String orgMeasureEditMustMatchError;

    /**
     * Default Constructor
     */
    public Edit_Organization_Measure_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization_measure/edit")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public BooleanValueDTO editOrganizationMeasure(OrganizationMeasureDTO org) throws UseCaseException {
        try {
            // Data needed for check
            // Look up the old one and make sure that
            // orgid, measureid and year are the same
            // If there's already one there then throw an error
            OrganizationMeasureDTO currentOMDTO = organizationMeasureDAO.findById(org.getId());
            if (currentOMDTO.getOrganizationId() != org.getOrganizationId() ||
                    currentOMDTO.getMeasureId() != org.getMeasureId() ||
                    currentOMDTO.getReportPeriodYear() != org.getReportPeriodYear()) {
                throw new Exception(orgMeasureEditMustMatchError);
            }

            // This is not a file upload but the initial record may have
            // been created by an upload.
            org.setFileUploadDate(currentOMDTO.getFileUploadDate());

            // Only ADMIN or ORG_ADMIN can edit organization measures
            // ORG_ADMIN can only edit there organization
            if (SecurityRoleEnum.ADMIN == userExtras.getRole()) {
                organizationMeasureDAO.update(org, org.getId());
            } else {
                // Not the ADMIN. Can only do for their organization
                if (userExtras.getOrgId() == org.getOrganizationId()) {
                    organizationMeasureDAO.update(org, org.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
