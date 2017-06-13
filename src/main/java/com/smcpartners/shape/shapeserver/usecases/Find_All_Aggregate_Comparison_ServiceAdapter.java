package com.smcpartners.shape.shapeserver.usecases;

import com.diffplug.common.base.Errors;
import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.OrganizationMeasureDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Find_All_Aggregate_Comparison_Service;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.NameDoubleValDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.OrgAvgAggregate;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.OrgsMeasureYearAvgDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Responsibility: </br>
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
    private UserDAO userDAO;

    @EJB
    private OrganizationDAO oDAO;

    @EJB
    private OrganizationMeasureDAO organizationMeasureDAO;

    @Inject
    private UserExtras userExtras;

    /**
     * Constructor
     */
    public Find_All_Aggregate_Comparison_ServiceAdapter() {
    }

    /**
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
            Map<Integer, List<NameDoubleValDTO>> aggMap = organizationMeasureDAO.getAvgForAllByYearByMeasure(measureId);

            // Populate the aggregate data in the return object
            aggregateAll(measureId, retObj, aggMap);

            // Now figure out what other organizations need to be pulled in.
            // This depends on the users role.
            aggreateUserData(aggMap, retObj);

            return retObj;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "showAggregateComparison", e.getMessage(), e);
            throw new UseCaseException(e.getMessage());
        }
    }

    /**
     * Aggregate to global totals data
     *
     * @param measureId
     * @param retObj
     * @param aggMap
     * @throws Exception
     */
    private void aggregateAll(int measureId, OrgsMeasureYearAvgDTO retObj, Map<Integer, List<NameDoubleValDTO>> aggMap) throws Exception {
        // Get the year span set from the map.
        // Look at all the years in the aggMap values list
        // and add them to an ordered list
        Set<String> yearSet = new TreeSet<>();
        Map<String, List<Double>> aggTotalsByYear = new TreeMap<>();
        aggMap.forEach((k, v) -> {
            v.forEach(n -> {
                // Year list
                yearSet.add(n.getKey());

                // Used to calculate overall by year
                List<Double> dobLst = aggTotalsByYear.get(n.getKey());
                if (dobLst == null) {
                    dobLst = new ArrayList<>();
                    aggTotalsByYear.put(n.getKey(), dobLst);
                }
                dobLst.add(n.getDoubleVal());
            });
        });
        retObj.setYearsSpanLst(yearSet.stream().collect(Collectors.toList()));

        // Get the aggregate year totals form the map
        // Do this by walking the aggMap and creating a map
        // of doubles (average for particular organization)
        // that is keyed by the year. Create a map to hold the
        // year (key) and average of all organizations for the
        // year.
        Map<String, Double> avgByYearMap = new TreeMap<>();
        for (String s : yearSet) avgByYearMap.put(s, 0.0);
        aggTotalsByYear.forEach((k, val) -> {
            double avg = val.stream().mapToDouble(f -> f.doubleValue()).average().getAsDouble();
            avgByYearMap.put(k, avg);
        });
        retObj.setAggAvgDataByYear(avgByYearMap.values().stream().collect(Collectors.toList()));
    }

    /**
     * Based on the user's organization(s) create a return list for a specific measure
     * of the avg by year.
     *
     * @param aggMap
     * @param retObj
     * @throws Exception
     */
    private void aggreateUserData(Map<Integer, List<NameDoubleValDTO>> aggMap, OrgsMeasureYearAvgDTO retObj) throws Exception {
        // Get requesting users role
        SecurityRoleEnum reqUserRole = userExtras.getRole();

        // If admin or dph user they can see any organization
        // Create a holder for the years
        // Walk the List<NameDoubleValDTO> comparing it to the
        // years span list. For missing items insert an entry in the list with a 0
        // value

        // Check the security role of the user. If there user is not an ADMIN or DPH user then
        // filter the aggMap for the org that is related to the user organization
        final ConcurrentHashMap<Integer, List<NameDoubleValDTO>> cMap = new ConcurrentHashMap<>(aggMap);
        if (SecurityRoleEnum.ADMIN != reqUserRole && SecurityRoleEnum.DPH_USER != reqUserRole) {
            int orgId = userExtras.getOrgId();
            cMap.entrySet().forEach(e -> {
                if (e.getKey() != orgId) {
                    cMap.remove(e.getKey());
                }
            });
        }

        // Build the return objects
        retObj.setOrgsMeasureYearAvgDTOS(new ArrayList<>());
        cMap.keySet().forEach(Errors.rethrow().wrap(k -> {
            // Need a map to hold
            Map<String, Double> avgByYearMap = new TreeMap<>();
            for (String s : retObj.getYearsSpanLst()) avgByYearMap.put(s, 0.0);

            // Get the first item in the list
            List<NameDoubleValDTO> nDDTOs = aggMap.get(k);
            nDDTOs.forEach(nDDTO -> {
                String listYear = nDDTO.getKey();
                avgByYearMap.put(listYear, nDDTO.getDoubleVal());
            });

            // Create entry in return object
            OrgAvgAggregate orgAvgAgg = new OrgAvgAggregate();
            String orgName = oDAO.findById(k).getName();
            orgAvgAgg.setOrgId(k);
            orgAvgAgg.setOrgName(orgName);
            orgAvgAgg.setMeasureYearAvgDTOS(avgByYearMap.entrySet().stream().map(e -> {
                return new NameDoubleValDTO(e.getKey(), e.getValue());
            }).collect(Collectors.toList()));

            // Add to return obj
            retObj.getOrgsMeasureYearAvgDTOS().add(orgAvgAgg);
        }));
    }

}
