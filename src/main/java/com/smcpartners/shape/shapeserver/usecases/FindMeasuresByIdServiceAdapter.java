package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.FindMeasuresByIdService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.MeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Takes a list of Organization Measure DTOs from FindAllOrganizationMeasuresByOrg service and returns the specific
 * measures for just that organization. Since there will be duplicates, the service adds them to a Hashset and returns
 * them back as a list to remove duplicates.
 * <p>
 * Created by bhokanson on 12/1/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class FindMeasuresByIdServiceAdapter implements FindMeasuresByIdService {

    @Inject
    private Logger log;

    @EJB
    private MeasureDAO measureDAO;

    @EJB
    private UserDAO userDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Default Constructor
     */
    public FindMeasuresByIdServiceAdapter() {
    }

    @Override
    @POST
    @Path("/find/measureById")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public List<MeasureDTO> findMeasuresById(List<OrganizationMeasureDTO> orgM) throws UseCaseException {
        try {

            // create LinkedHashSet
            LinkedHashSet<MeasureDTO> newSet = new LinkedHashSet<>();
            //make sure list isn't empty
            if (orgM != null && orgM.size() > 0) {
                //loop through HashSet and find the Measure by Id, add to Measure List
                for (OrganizationMeasureDTO omd : orgM) {
                    //add to HashSet, which will not allow duplicates
                    newSet.add(measureDAO.findById(omd.getMeasureId()));
                }
                //convert HashSet to ArrayList to return (may not be necessary)
                return new ArrayList<>(newSet);
            } else {
                throw new Exception("List has no data");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "findMeasuresById", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }
}
