package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Activate_Provider_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.IntEntityIdRequestDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. ADMIN and ORG_ADMIN can activate a provider but ORG_ADMIN only for their organization
 * <p>
 * Created by johndestefano on 11/4/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class Activate_Provider_ServiceAdapter implements Activate_Provider_Service {

    @Inject
    private Logger log;

    @Inject
    private UserExtras userExtras;

    public Activate_Provider_ServiceAdapter() {
    }

    @Override
    @POST
    @Path("/provider/activate")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN})
    @Logged
    public BooleanValueDTO activateProvider(IntEntityIdRequestDTO id) throws UseCaseException {
        return BooleanValueDTO.get(true);
    }
}
