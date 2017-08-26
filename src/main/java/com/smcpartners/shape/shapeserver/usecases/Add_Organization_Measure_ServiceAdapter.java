package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Add_Organization_Measure_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.IntEntityResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Responsible: Add an Organization Measure<br/>
 * 1. ADMIN can add for any organization measure.
 * ORG_ADMIN and REGISTERED can only add for their organizations<br/>
 * 2. Can only have 1 measure type per organization per year.</br>
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 * 1. Add rule for one measure type per organization per year.</br>
 */
@Path("/common")
public class Add_Organization_Measure_ServiceAdapter implements Add_Organization_Measure_Service {

    @EJB
    OrganizationMeasureDAO organizationMeasureDAO;

    @Inject
    UserExtras userExtras;

    @Inject
    @ConfigurationValue("com.smc.server-core.errorMsgs.onlyOneMeasureTypePerOrgPerYearError")
    String onlyOneMeasureTypePerOrgPerYearError;


    /**
     * Default Constructor
     */
    public Add_Organization_Measure_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization_measure/add")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public IntEntityResponseDTO addOrganizationMeasure(OrganizationMeasureDTO org) throws UseCaseException {
        try {
            // ADMIN can add for any organization organizations
            // ORG_ADMIN and REGISTERED can only add for their organizations
            // The date for the report is now

            // Users role
            Date now = new Date();

            // Data needed for check
            // If there's already one there then throw an error
            if (organizationMeasureDAO.checkMeasureForYearForOrgAlreadyEntered(org.getMeasureId(),
                    org.getOrganizationId(), org.getReportPeriodYear())) {
                throw new Exception(onlyOneMeasureTypePerOrgPerYearError);
            }

            OrganizationMeasureDTO orgDTO = null;
            // ADMIN role
            if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                org.setRpDate(now);
                orgDTO = organizationMeasureDAO.create(org);
                return IntEntityResponseDTO.makeNew(orgDTO.getId());
            } else {
                // For other users only allow add for their organization
                if (org.getOrganizationId() == userExtras.getOrgId()) {
                    org.setRpDate(now);
                    orgDTO = organizationMeasureDAO.create(org);
                    return IntEntityResponseDTO.makeNew(orgDTO.getId());
                } else {
                    throw new NotAuthorizedToPerformActionException();
                }
            }
        } catch (Exception e) {
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }
}
