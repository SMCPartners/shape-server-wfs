package com.smcpartners.shape.shapeserver.shared.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:</br>
 * 1. Supports the validation, creation, and extension of JWTs</br
 * <p>
 * Created by johndestefano on 9/14/15.
 * </p>
 * <p>
 * Changes:<br>
 * 1.
 * </p>
 */
@ApplicationScoped
public class JWTUtils {

    @Inject
    private Logger log;

    public static final String USERID = "userId";
    public static final String ROLE = "role";
    public static final String ORGID = "orgId";
    public static final String X_XSRF_TOKEN = "xxToken";

    @Inject
    @ConfigurationValue("com.smc.server-core.security.appKey")
    private String appKey;

    @Inject
    @ConfigurationValue("com.smc.server-core.security.expireLength")
    private Long expireLength;

    private SecretKey key;

    private SecureRandom secureRandom;


    public JWTUtils() {
    }

    @PostConstruct
    protected void postConstruct() {
        byte[] encodedKey = Base64.getDecoder().decode((this.appKey).getBytes());
        this.key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HS512");
        secureRandom = new SecureRandom();
    }

    /**
     * Generate a key
     *
     * @return
     */
    public String genKey() {
        Key key = MacProvider.generateKey();
        String eKey = new String(Base64.getEncoder().encode(key.getEncoded()));
        String alg = key.getAlgorithm();
        return new String(eKey);
    }

    /**
     * Generate a token. Setting neverExpire to false but providing 0 as the expire length
     * will result in a never expire token.
     *
     * @param userId
     * @param role
     * @param orgId
     * @param neverExpire
     * @return
     * @throws Exception
     */
    public String generateForBearerToken(String userId, String role, int orgId, boolean neverExpire) throws Exception {
        return Jwts.builder()
                .setSubject(userId)
                .setHeaderParam(ROLE, role)
                .setHeaderParam(ORGID, "" + orgId)
                .setExpiration(neverExpire || expireLength.longValue() == 0 ? null : new Date(this.calcExpire()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * Generate a token. Setting neverExpire to false but providing 0 as the expire length
     * will result in a never expire token.
     *
     * @param userId
     * @param role
     * @param orgId
     * @param neverExpire
     * @return
     * @throws Exception
     */
    public DoubleCookieTokenPair generateForDoubleCookieToken(String userId, String role, int orgId, boolean neverExpire) throws Exception {
        String xxToken = new BigInteger(130, secureRandom).toString(32);
        String jwtToken = Jwts.builder()
                .setSubject(userId)
                .setHeaderParam(ROLE, role)
                .setHeaderParam(ORGID, "" + orgId)
                .setHeaderParam(X_XSRF_TOKEN, xxToken)
                .setExpiration(neverExpire || expireLength.longValue() == 0 ? null : new Date(this.calcExpire()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return new DoubleCookieTokenPair(xxToken, jwtToken);
    }

    /**
     * Validate the token
     *
     * @param token
     * @return
     * @throws Exception
     */
    private Jws<Claims> validateToken(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }

    /**
     * Is the presented token valid
     *
     * @param token
     * @return
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SignatureException se) {
            return false;
        }
    }

    /**
     * Get users role
     *
     * @param token
     * @return
     * @throws Exception
     */
    public String getRole(String token) throws Exception {
        try {
            Jws<Claims> claims = this.validateToken(token);
            String role = (String) claims.getHeader().get(ROLE);
            return role;
        } catch (SignatureException e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "getRole", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * Get users OrgId
     *
     * @param token
     * @return
     * @throws Exception
     */
    public int getOrgId(String token) throws Exception {
        try {
            Jws<Claims> claims = this.validateToken(token);
            String orgId = (String) claims.getHeader().get(ORGID);
            return Integer.parseInt(orgId);
        } catch (SignatureException e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "getOrgId", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * Return jwt values
     *
     * @param token
     * @return
     * @throws Exception
     */
    public Map<String, String> getValues(String token) throws Exception {
        Map<String, String> retMap = new HashMap<>();
        Jws<Claims> claims = this.validateToken(token);
        String role = (String) claims.getHeader().get(ROLE);
        String orgId = (String) claims.getHeader().get(ORGID);
        String user = claims.getBody().getSubject();
        String xxToken = (String)claims.getHeader().get(X_XSRF_TOKEN);
        retMap.put(USERID, user);
        retMap.put(ROLE, role);
        retMap.put(ORGID, orgId);
        if (xxToken != null && xxToken.length() > 0) {
            retMap.put(X_XSRF_TOKEN, xxToken);
        }
        return retMap;
    }

    /**
     * Adds the factor to the current date to calculate an expiration time for a token.
     *
     * @return
     */
    private long calcExpire() {
        Date d = new Date();
        long expDate = d.getTime() + expireLength.longValue();
        return expDate;
    }

    /**
     * Used for testing
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Key key = MacProvider.generateKey();
        String eKey = new String(Base64.getEncoder().encode(key.getEncoded()));
        String alg = key.getAlgorithm();
        System.out.println("eKey: " + eKey);
        System.out.println("alg: " + alg);
    }
}
