package com.smcpartners.shape.shapeserver.usecases;



import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Add_Organization_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.IntEntityResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible: Add a new Organization<br/>
 * 1. Only ADMIN can add an organization
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Add_Organization_ServiceAdapter implements Add_Organization_Service {

    @EJB
    OrganizationDAO organizationDAO;

    @Inject
    UserExtras userExtras;


    public Add_Organization_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/organization/add")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public IntEntityResponseDTO addOrganization(OrganizationDTO org) throws UseCaseException {
        try {
            // Only ADMIN can add organizations
            OrganizationDTO orgDTO = organizationDAO.create(org);
            return IntEntityResponseDTO.makeNew(orgDTO.getId());
        } catch (Exception e) {
            throw new UseCaseException(e.getMessage());
        }
    }
}
