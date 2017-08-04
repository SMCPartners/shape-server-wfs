package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Aggregate_Comparison_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.NameDoubleValDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.OrgAvgAggregateDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.OrgsMeasureYearAvgDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Responsibility: Find all aggregate measure comparisons</br>
 * 1. Bring back aggregate measure data for all organizations user has relationship with.</br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 6/12/17
 */
@Path("/common")
public class Find_All_Aggregate_Comparison_ServiceAdapter implements Find_All_Aggregate_Comparison_Service {

    @Inject
    private Logger log;

    @EJB
    OrganizationDAO oDAO;

    @EJB
    OrganizationMeasureDAO organizationMeasureDAO;

    @Inject
    UserExtras userExtras;

    /**
     * Constructor
     */
    public Find_All_Aggregate_Comparison_ServiceAdapter() {
    }

    /**
     * Returns the data needed to create an aggregate view for a measure. This is used to
     * compare the agregate totals to individual organizations. There is always aggregate
     * data and organization specific. The organization data might be all organizations
     * for the ADMIN and DPH_ADMIN. Other users will only see there organizations data.
     *
     * @param measureId
     * @return
     * @throws UseCaseException
     */
    @Override
    @GET
    @NoCache
    @Path("/show/aggregateAllComparison/{measureId}")
    @Produces("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.DPH_USER, SecurityRoleEnum.ORG_ADMIN, SecurityRoleEnum.REGISTERED})
    @Logged
    public OrgsMeasureYearAvgDTO showAggregateComparison(@PathParam("measureId") int measureId) throws UseCaseException {
        try {
            OrgsMeasureYearAvgDTO retObj = new OrgsMeasureYearAvgDTO();
            retObj.setMeasureId(measureId);

            // Determine what to return based on the users role
            // ADMIN and DPH_ADMIN get everything.
            // Others get only the organization they belong to.
            // Get requesting users role
            SecurityRoleEnum reqUserRole = userExtras.getRole();
            int orgId = 0;
            if (SecurityRoleEnum.ADMIN != reqUserRole && SecurityRoleEnum.DPH_USER != reqUserRole) {
                orgId = userExtras.getOrgId();
            }

            // Pull back the data
            Map<String, Map> retMap = organizationMeasureDAO.getAvgForAllByYearByMeasure(measureId, orgId);
            Map<String, List<Double>> aggMap = retMap.get("AGG");
            Map<Integer, OrgAvgAggregateDTO> orgAggMap = retMap.get("ORG");

            // Populate the aggregate data in the return object
            retObj.setYearsSpanLst(aggMap.keySet().stream().collect(Collectors.toList()));
            Map<String, Double> avgByYearMap = new TreeMap<>();
            for (String s : retObj.getYearsSpanLst()) avgByYearMap.put(s, 0.0);
            aggMap.forEach((k, val) -> {
                double avg = val.stream().mapToDouble(f -> f.doubleValue()).average().getAsDouble();
                avgByYearMap.put(k, avg * 100);
            });
            retObj.setAggAvgDataByYear(avgByYearMap.values().stream().collect(Collectors.toList()));

            // Populate organizations in return
            // Need to fill in blanks
            orgAggMap.forEach((k, v) -> {
                // Make an ordered map
                Map<String, NameDoubleValDTO> nDDTOMap = new TreeMap<>();
                for (String s : retObj.getYearsSpanLst()) nDDTOMap.put(s, new NameDoubleValDTO(s, 0.0));

                // For each list item put it in the map
                List<NameDoubleValDTO> ndLst = v.getMeasureYearAvgDTOS();
                ndLst.forEach(item -> {
                    String yearKey = item.getKey();
                    item.setDoubleVal(item.getDoubleVal() * 100);
                    nDDTOMap.put(yearKey, item);
                });

                // Replace the old list with the new list
                v.setMeasureYearAvgDTOS(nDDTOMap.values().stream().collect(Collectors.toList()));
            });
            retObj.setOrgsMeasureYearAvgDTOS(orgAggMap.values().stream().collect(Collectors.toList()));

            // Return object
            return retObj;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "showAggregateComparison", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }

}