package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Inactivate_Organization_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.IntEntityIdRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Responsible:<br/>
 * 1. Only the ADMIN can inactivate an organization
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Inactivate_Organization_ServiceAdapter implements Inactivate_Organization_Service {

    @EJB
    OrganizationDAO organizationDAO;

    /**
     * Default Constructor
     */
    public Inactivate_Organization_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization/inactivate")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public BooleanValueDTO inactivateOrganization(IntEntityIdRequestDTO id) throws UseCaseException {
        try {
            // Only ADMIN can inactivate there organization
            organizationDAO.changeOrganizationActiveStatus(id.getEntId(), false);

            // Return value
            return new BooleanValueDTO(true);
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }
}
